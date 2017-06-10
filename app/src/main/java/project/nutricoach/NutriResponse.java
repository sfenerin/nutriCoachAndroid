package project.nutricoach;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * Created by Shawn on 5/15/2017.
 */

public class NutriResponse implements Callable<String> {
    String key = "8a87c3522c2c4298a57ad80a4a24354c";
    String secret = "6561cc4a8711477fbc696bd1dcfea90f";
    FatSecretAPI api;
    String input;
    User user;

    public NutriResponse(String input){
        api = new FatSecretAPI(key, secret);
        this.input = input;
    }
    public NutriResponse(String input, User user){
        api = new FatSecretAPI(key, secret);
        this.input = input;
        this.user=user;
    }

    private String process(String input) throws IOException, JSONException {
        String response = "";
        FoodItemExtractor foodFinder = new FoodItemExtractor();
        RequestExtractor requestFinder = new RequestExtractor();
        GoalExtractor goalFinder = new GoalExtractor();

        //handles food logging
        if (foodFinder.foundFoodItem(input)) {
            ArrayList<FoodQuery> foodQueries = foodFinder.getFoodQueries();
            for (FoodQuery foodQuery : foodQueries) {
                String servingSize = foodQuery.getServingSize();
                double servingCount = foodQuery.getServingCount();
                Food food = updateAndStoreInfo(foodQuery.getFoodItem(), servingSize, servingCount);
                String servingInfo = "";
                if (servingCount != 1) {
                    servingInfo = servingCount + " " + servingSize + " of ";
                }
                response += "You ate " + servingInfo + foodQuery.getFoodItem() +". It contains " + (int)food.getCalories() + " calories and " + (int)food.getProtein() + "g of protein.\n";
            }
            if (user.getCaloriesToday() >= 0) {
                response += "\nYou have " + (int)user.getCaloriesToday() + " calories left to eat today.";
            } else {
                response += "\nYou are " + (int)Math.abs(user.getCaloriesToday()) + " calories over your daily goal.";

            }

            return response;
            //handles request
        } else if (requestFinder.foundRequest(input)) {
            //Recommendation request
            if (requestFinder.getRecommendationRequest() != null) {
                FoodRecommendation fr = new FoodRecommendation(user, api);
                Food rec = fr.getFoodRecommendationEDAMAN();
                response = "How about " + rec.getName() + "?\n You can find the recipe here: " + rec.getRecipeUrl();
                //Macro request
            } else if (requestFinder.hasMacroRequest()) {
                if (requestFinder.getMacroRequest().contains("cal")) {
                    long caloriesEaten = Math.round(user.getCalories() - user.getCaloriesToday());
                    if (caloriesEaten >= 0) {
                        response = "Looks like you're on track for your goal today! You've had " + caloriesEaten + " calories today.";
                    } else {
                        response = "Oops! Looks like you've had " + Math.abs(caloriesEaten) + " today. Try to eat less calories tomorrow!";
                    }
                }
                if (requestFinder.getMacroRequest().contains("carb"))
                    response = "You've eaten " + (Math.round(user.getCarbs() - user.getCarbsToday())) + "g of carbohydrates today.";
                if (requestFinder.getMacroRequest().contains("protein"))
                    response = "You've eaten " + (Math.round(user.getProtein() - user.getProteinToday())) + "g of protein today.";
                if (requestFinder.getMacroRequest().contains("fat"))
                    response = "You've eatinen " + (Math.round(user.getFat() - user.getFatToday())) + "g of fat today";
                //Goal request
            } else if (requestFinder.hasGoalRequest()) {
                response = "Goal request: " + requestFinder.getGoalRequest();
            } else {
                response = "Unknown request";
            }
        //handles goal setting
        } else if (goalFinder.foundGoal(input)) {
            response = "GOAL SET: " + goalFinder.getGoal() + " GOAL FOOD: " + goalFinder.getGoalFood();

        } else {
            response = "Sorry, I didn't quite get that. Could you try rephrasing or being more specific?";

        }
        return response;
    }


    private Food updateAndStoreInfo(String foodQuery, String serving, double servingCount) throws JSONException, UnsupportedEncodingException {
        Food food = null;
        if (serving.equals("")) {
            JSONObject foodInfo = getGenericFoodInfo(foodQuery);
            food = logFoodServing(foodInfo, serving, servingCount);
        } else {
//            gets specific food info, with serving size
            JSONObject foodInfo = getSpecificFoodInfo(foodQuery, serving, servingCount);
            food = logFoodServing(foodInfo, serving, servingCount);
        }

        return food;
    }

    private JSONObject getSpecificFoodInfo(String foodQuery, String servingSize, double servingCount) throws UnsupportedEncodingException, JSONException {
        JSONArray responseArray = api.getFoodItems(foodQuery).getJSONObject("result").getJSONObject("foods").getJSONArray("food");
        String food_id = responseArray.getJSONObject(0).get("food_id").toString().replaceAll("\\s","");
        Log.d("specific food info", api.getFoodItem(Long.parseLong(food_id)).toString(2));
        JSONObject foodObj = api.getFoodItem(Long.parseLong(food_id)).getJSONObject("result").getJSONObject("food");
        if(foodObj.getJSONObject("servings").get("serving") instanceof JSONObject){
            JSONObject nutritionInfo = foodObj.getJSONObject("servings").getJSONObject("serving");
            if (!correctServingSize(nutritionInfo, servingSize)) return getGenericFoodInfo(foodQuery);
        } else {
            JSONArray servingsArray = foodObj.getJSONObject("servings").getJSONArray("serving");
            boolean correctServingSize = false;
            for(int i = 0; i < servingsArray.length(); i++) {
                JSONObject nutritionInfo = servingsArray.getJSONObject(i);
                if (correctServingSize(nutritionInfo, servingSize)) correctServingSize = true;
            }
            if(!correctServingSize) return getGenericFoodInfo(foodQuery);
        }
        return api.getFoodItem(Long.parseLong(food_id));
//        return getGenericFoodInfo(foodQuery); //placeholder

    }


    private boolean correctServingSize(JSONObject foodObj, String servingSize) throws JSONException {
        String foodDescription = foodObj.getString("measurement_description");
        if (foodDescription.contains(servingSize)) return true;
        else if (servingSize.charAt(servingSize.length() - 1) == 's') {
            if (foodDescription.contains(servingSize.substring(0, servingSize.length() - 1))) return true;
        }
        return false;
    }

    private Food logFoodServing(JSONObject foodInfo, String serving, double servings) throws JSONException, UnsupportedEncodingException {
        JSONObject parsedFood = foodInfo.getJSONObject("result").getJSONObject("food");
        Food food;
        if (serving.equals(""))
            food = new Food((String)parsedFood.get("food_id"), "", servings, api);
        else {
            food = new Food((String)parsedFood.get("food_id"), serving, servings, api);
        }
        //To-do : change sentiment

        user.logFood(food, true);
//        user.setWeeklyGoal(food);

        return food;
    }

    private JSONObject getGenericFoodInfo(String foodQuery) throws UnsupportedEncodingException, JSONException {
        Log.d("response Array:", api.getFoodItems(foodQuery).toString(2));
        JSONArray responseArray = api.getFoodItems(foodQuery).getJSONObject("result").getJSONObject("foods").getJSONArray("food");
        for(int i = 0; i < responseArray.length(); i ++){
            String foodType = responseArray.getJSONObject(i).get("food_type").toString();
            if(notGeneric(foodType)){
                String food_id = responseArray.getJSONObject(i).get("food_id").toString().replaceAll("\\s","");
                Log.d("Generic food: ", api.getFoodItem(Long.parseLong(food_id)).toString(2));
                return api.getFoodItem(Long.parseLong(food_id));
            }
        }
        String food_id = responseArray.getJSONObject(0).get("food_id").toString().replaceAll("\\s","");
        return api.getFoodItem(Long.parseLong(food_id));
    }

    private boolean notGeneric(String foodType){return !foodType.equals("Generic");}

    @Override
    public String call() throws JSONException, IOException {
        try {
            String response = process(input);
            return response;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}