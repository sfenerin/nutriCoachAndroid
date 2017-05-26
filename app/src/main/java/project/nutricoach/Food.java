package project.nutricoach;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by Katy on 5/22/17.
 */

public class Food {
    String ID;
    String name;
    double calories;
    double protein;
    double carbs;
    double fat;
    String servingDescription;

    public Food(String ID, String name, double calories, double protein, double carbs, double fat ) {
        this.ID = ID;
        this.name = name;
        this.protein = protein;
        this.calories = calories;
        this.carbs = carbs;
        this.fat = fat;
    }
    public Food(String foodID, FatSecretAPI api) throws UnsupportedEncodingException, JSONException {
        JSONObject foodObj = api.getFoodItem(Long.parseLong(foodID));
        foodObj = foodObj.getJSONObject("result").getJSONObject("food");

        this.ID = foodObj.getString("food_id");
        this.name = foodObj.getString("food_name");

        JSONObject nutritionInfo;
        if(foodObj.getJSONObject("servings").get("serving") instanceof JSONObject){
            nutritionInfo = foodObj.getJSONObject("servings").getJSONObject("serving");
        } else {
            JSONArray servingsArray = foodObj.getJSONObject("servings").getJSONArray("serving");
            nutritionInfo = servingsArray.getJSONObject(0);
        }
        this.servingDescription = nutritionInfo.getString("serving_description");
        this.calories = nutritionInfo.getDouble("calories");
        this.carbs = nutritionInfo.getDouble("carbohydrate");
        this.protein = nutritionInfo.getDouble("protein");
        this.fat = nutritionInfo.getDouble("fat");

    }

    public String getID() { return ID;}

    public String getName() { return name; }

    public double getCalories() {
        return calories;
    }

    public double getProtein() {
        return protein;
    }

    public double getFat() {
        return fat;
    }

    public double getCarbs() {
        return carbs;
    }

    public String getServingDescription() {
        return servingDescription;
    }

    @Override
    public String toString() {
        return "Food ID: " + getID() + " name: " + getName() + " calories: " + getCalories();
    }
}
