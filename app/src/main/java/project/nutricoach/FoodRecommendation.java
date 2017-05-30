package project.nutricoach;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

/**
 * Created by Shawn on 5/22/2017.
 */

public class FoodRecommendation {
    private String name;
    private User user;
    private FatSecretAPI api;

    private Double remainingCals;
    private Double remainingFats;
    private Double remainingCarbs;

    private Double remainingProtein;

    String[] fatFoods = {"avocado","cheese","Chocolate","eggs","fish","nutts","yogurt"};
    String[] proteinFoods = {"eggs","chicken breast","oats","beef","tuna","lentils","fish","peanuts"};
    String[] calorieFoods = {"granola","cheese","peanut butter","dried fruit","avocado","cheese","dried fruit","bread"};

    public FoodRecommendation(double cals, double fats, double carbs, double protein,User user,FatSecretAPI api){
        this.remainingCals = cals;
        this.remainingCarbs = carbs;
        this.remainingFats = fats;
        this.remainingProtein = protein;

        this.user = user;
        this.api = api;
    }

    public FoodRecommendation(User user, FatSecretAPI api){
        this.remainingCals = user.getCalories();
        this.remainingCarbs = user.getCarbs();
        this.remainingFats = user.getFat();
        this.remainingProtein = user.getProtein();

        this.user = user;
        this.api = api;
    }

    public String getFoodRecommendation() throws IOException, JSONException {
        String recommendation = "";
        if( remainingProtein/user.getProtein() > remainingFats/user.getFat() && remainingProtein/user.getProtein() > remainingCals/user.getCalories() ){
             recommendation = macroRecommendation(proteinFoods).getName();
        } else if ( remainingFats/user.getFat() > remainingProtein/user.getProtein() && remainingFats/user.getFat() > remainingCals/user.getCalories()){
            recommendation = macroRecommendation(fatFoods).getName();
        } else {
            recommendation = macroRecommendation(calorieFoods).getName();
        }
        if (recommendation.equals("")){
            recommendation = macroRecommendation(calorieFoods).getName();
        }
        return recommendation;
    }

    private Food macroRecommendation(String[] foods){
        int rnd = new Random().nextInt(foods.length);
        String query = foods[rnd];
        try {
            Food food =  getGenericFoodInfo(query);
            return food;
        } catch (UnsupportedEncodingException e) {e.printStackTrace();} catch (JSONException e) {e.printStackTrace();}

        return null;
    }

    private Food getGenericFoodInfo(String foodQuery) throws UnsupportedEncodingException, JSONException {
        Log.d("Recommendation Response", api.getFoodItems(foodQuery).toString(2));
        JSONArray responseArray = api.getFoodItems(foodQuery).getJSONObject("result").getJSONObject("foods").getJSONArray("food");
        for(int i = 0; i < responseArray.length(); i ++){
            String foodType = responseArray.getJSONObject(i).get("food_type").toString();
            if(notGeneric(foodType)){
                String food_id = responseArray.getJSONObject(i).get("food_id").toString().replaceAll("\\s","");
                Food foodItem = new Food(food_id, api);
                if(fitsMacro(foodItem)){
                    return foodItem;
                }
            }
        }

        String food_id = responseArray.getJSONObject(0).get("food_id").toString().replaceAll("\\s","");
        return new Food(food_id,api);
    }
    public boolean fitsMacro(Food food){
        double calories = food.getCalories();
        double protein = food.getProtein();
        double fats = food.getFat();
        double carbs = food.getCarbs();

        if(protein < remainingProtein && calories < remainingCals && fats < remainingFats && carbs < remainingCarbs){
            Log.d("recommended Food",food.getName());
            return true;
        }
        return false;
    }
    private boolean notGeneric(String foodType){return !foodType.equals("Generic");}


    private String oldRecommendation() throws IOException, JSONException {
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
