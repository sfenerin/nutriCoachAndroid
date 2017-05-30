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
import java.net.HttpURLConnection;
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

    private int remainingCals;
    private int remainingFats;
    private int remainingCarbs;

    private int remainingProtein;

    String[] fatFoods = {"avocado","cheese","Chocolate","eggs","fish","nutts","yogurt"};
    String[] proteinFoods = {"eggs","chicken breast","oats","beef","tuna","lentils","fish","peanuts"};
    String[] calorieFoods = {"granola","cheese","peanut butter","dried fruit","avocado","cheese","dried fruit","bread","lentils"};

    public FoodRecommendation(double cals, double fats, double carbs, double protein,User user,FatSecretAPI api){
        this.remainingCals = (int)cals;
        this.remainingCarbs = (int)carbs;
        this.remainingFats = (int)fats;
        this.remainingProtein = (int)protein;

        this.user = user;
        this.api = api;
    }

    public FoodRecommendation(User user, FatSecretAPI api){
        this.remainingCals =(int) user.getCalories();
        this.remainingCarbs = (int)user.getCarbs();
        this.remainingFats = (int)user.getFat();
        this.remainingProtein = (int)user.getProtein();

        this.user = user;
        this.api = api;
    }


    public Food getFoodRecommendationEDAMAN() throws IOException, JSONException {
        String url= "https://api.edamam.com/search?app_id=80e54b0e&app_key=636dfa8becce66d566a51a4219a79b65&from=0&to=3&calories=gte 0, lte "+ remainingCals;
        Log.d("user work",""+ user.isVegeterian());
        if(user.isVegan()){
            return dietRestriction(url,"&health=vegan");
        }

        if(user.isVegeterian()) {
            return dietRestriction(url,"&health=vegetarian");
        }

        if(user.isGlutenFree()){
            return dietRestriction(url,"&health=gluten-free");
        }

        return dietRestriction(url, "");
    }

    private Food dietRestriction(String url, String filter) throws IOException, JSONException {
        int rnd = new Random().nextInt(calorieFoods.length);
        String query = calorieFoods[rnd];
        url = url+"&q=" + query + filter;
        JSONObject response = getResponse(url);
        Log.d("EDAMAN",response.toString(2));
        return processObject(response);
    }

    private Food processObject(JSONObject obj) throws JSONException {
        int count = obj.getInt("count");
        JSONArray recipes = obj.getJSONArray("hits");
        int rnd = new Random().nextInt(recipes.length());
        JSONObject curFood = recipes.getJSONObject(rnd);
        Food food = new Food(curFood);
        return food;
    }

    private JSONObject getResponse(String urlString) throws IOException, JSONException {
        String modString = urlString.replace(" ", "%20");
        Log.d("modInput",modString);
        URL url = new URL(modString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStream input = conn.getInputStream();
                StringBuilder responseStrBuilder = new StringBuilder();
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
                String inputStr;
                while ((inputStr = streamReader.readLine()) != null) {
                    responseStrBuilder.append(inputStr);
                }
               JSONObject response = new JSONObject(responseStrBuilder.toString());
        conn.disconnect();
        return response;
    }
    public String getFoodRecommendationFatSecret() throws IOException, JSONException {
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
