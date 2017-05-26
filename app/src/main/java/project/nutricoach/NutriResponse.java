package project.nutricoach;
import android.util.Log;

import com.fatsecret.platform.services.FatsecretService;
import com.fatsecret.platform.model.CompactFood;
import com.fatsecret.platform.services.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
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

        if(foodFinder.foundFoodItem(input)){
            String foodQuery = foodFinder.getFoodItem();

            Log.d("FoodQuery", foodQuery);
            String servingSize = foodFinder.getServingSize(); //need actual serving size
            double servingCount = foodFinder.getServingCount();
            Food food = updateAndStoreInfo(foodQuery, servingSize, servingCount);
            response = "You ate " + food.getServingDescription() + " of " + foodQuery +". It contains " + food.getCalories() + " calories and " + food.getProtein() + "g of protein.";
            response += "\nYou have " + user.getCaloriesToday() + " calories left to eat today.";

            return response;


        } else {
            response = "Sorry, I didn't quite get that. Could you try rephrasing or being more specific?";
        }
        return response;
    }


    private Food updateAndStoreInfo(String foodQuery, String serving, double servingCount) throws JSONException, UnsupportedEncodingException {
        Food food = null;
        if (serving.equals("")) {
            JSONObject foodInfo = getGenericFoodInfo(foodQuery);
            food = logFoodServing(foodInfo, 1);
        } else {
//            gets specific food info, with serving size
            JSONObject foodInfo = getSpecificFoodInfo(foodQuery, serving, servingCount);
            food = logFoodServing(foodInfo, 1);
        }

        return food;
    }

    private JSONObject getSpecificFoodInfo(String foodQuery, String servingSize, double servingCount) throws UnsupportedEncodingException, JSONException {
        JSONArray responseArray = api.getFoodItems(foodQuery).getJSONObject("result").getJSONObject("foods").getJSONArray("food");
        return getGenericFoodInfo(foodQuery); //placeholder

    }


    private Food logFoodServing(JSONObject foodInfo, int servings) throws JSONException, UnsupportedEncodingException {
        JSONObject parsedFood = foodInfo.getJSONObject("result").getJSONObject("food");
        Food food = new Food((String)parsedFood.get("food_id"), api);
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