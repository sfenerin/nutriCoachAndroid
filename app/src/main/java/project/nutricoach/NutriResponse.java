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

        if (foodFinder.foundFoodItem(input)) {
            ArrayList<FoodQuery> foodQueries = foodFinder.getFoodQueries();
            for (FoodQuery foodQuery : foodQueries) {
                String servingSize = foodQuery.getServingSize(); //need actual serving size
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
            //handles recommendation requests
        } else if (requestFinder.foundRequest(input)) {
            if (requestFinder.getRecommendationRequest() != null) {
                response = "Recommendation request: " + requestFinder.getRecommendationRequest();
            } else if (requestFinder.getMacroRequest() != null) {
                if (requestFinder.getMacroRequest().contains("cal"))
                    response = "You've had " + (Math.round(user.getCalories())) + " calories today.";
                if (requestFinder.getMacroRequest().contains("carb"))
                    response = "You've eaten " + (Math.round(user.getCarbs()))+ "g of carbohydrates today.";
                if (requestFinder.getMacroRequest().contains("protein"))
                    response = "You've eaten " + (Math.round(user.getProtein())) + "g of protein today.";
            } else {
                response = "Macro request: " + requestFinder.getMacroRequest();
            }
        //handles recommendation requests
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
//        for(int i = 0; i < responseArray.length(); i ++){
//            String foodDescription = responseArray.getJSONObject(i).get("food_description").toString();
//            if(correctServingSize(foodDescription, servingSize)){
//                String food_id = responseArray.getJSONObject(i).get("food_id").toString().replaceAll("\\s","");
//                Log.d("Specific food: ", api.getFoodItem(Long.parseLong(food_id)).toString(2));
//                return api.getFoodItem(Long.parseLong(food_id));
//            }
//        }
        String food_id = responseArray.getJSONObject(0).get("food_id").toString().replaceAll("\\s","");
        Log.d("specific food info", api.getFoodItem(Long.parseLong(food_id)).toString(2));
        return api.getFoodItem(Long.parseLong(food_id));
//        return getGenericFoodInfo(foodQuery); //placeholder

    }




    private Food logFoodServing(JSONObject foodInfo, String serving, double servings) throws JSONException, UnsupportedEncodingException {
        JSONObject parsedFood = foodInfo.getJSONObject("result").getJSONObject("food");
        Food food;
        if (serving.equals(""))
            food = new Food((String)parsedFood.get("food_id"), "", servings, api);
        else {
            food = new Food((String)parsedFood.get("food_id"), serving, servings, api);
        }
        user.logFood(food, true);
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