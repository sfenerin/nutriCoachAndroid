package project.nutricoach;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Shawn on 5/22/2017.
 */

public class FoodRecommendation {
    private String name;

    private Double remainingCals;
    private Double remainingFats;
    private Double remainingCarbs;

    private Double remainingProtein;

    public FoodRecommendation(double cals, double fats, double carbs, double protein){
        this.remainingCals = cals;
        this.remainingCarbs = carbs;
        this.remainingFats = fats;
        this.remainingProtein = protein;
    }

    public String getFoodRecommendation() throws IOException, JSONException {

        URL url = new URL("http://api.nal.usda.gov/ndb/nutrients/?format=json&api_key=giHMM0wkOojqHjrgJ5IaEe2SIGC3s4jvXDn2UpeQ&nutrients=205&nutrients=204&nutrients=208&nutrients=203&subset=1");
        URLConnection conn = url.openConnection();
        InputStream input = conn.getInputStream();
        StringBuilder responseStrBuilder = new StringBuilder();
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
        String inputStr;
        while ((inputStr = streamReader.readLine()) != null)
            responseStrBuilder.append(inputStr);
        JSONObject response = new JSONObject(responseStrBuilder.toString());
//        Log.d("FoodRecommendationList", response.toString(2));

        //randomly grab from array of foods.. sort out alcohol
        JSONArray foods = response.getJSONObject("report").getJSONArray("foods");
        for(int i = 0; i < foods.length(); i++){
            JSONObject curFood = foods.getJSONObject(i);
            JSONArray nutrients = curFood.getJSONArray("nutrients");
            double calories = nutrients.getJSONObject(0).getDouble("value");

            double protein = nutrients.getJSONObject(1).getDouble("value");

            double fats = nutrients.getJSONObject(2).getDouble("value");

            double carbs = nutrients.getJSONObject(3).getDouble("value");

            if(protein < remainingProtein && calories < remainingCals && fats < remainingFats && carbs < remainingCarbs
                    && !curFood.getString("name").contains("1.0 fl oz")){

                Log.d("recommended Food",curFood.getString("name"));
                return curFood.getString("name");
            }

        }
        return "";
    }

    public String getRecipe(){
        return "";
    }


}
