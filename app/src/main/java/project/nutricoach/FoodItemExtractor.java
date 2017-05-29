package project.nutricoach;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.Arrays;
/**
 * Created by Shawn on 5/18/2017.
 */

public class FoodItemExtractor {

    public static final int MIN_FOOD_QUERY_COUNT = 2;

    String foodItem;
    String servingSize;
    double servingCount;
    String sentiment;

    ArrayList<String> item_types = new ArrayList<String>();
    ArrayList<Integer> item_counts = new ArrayList<Integer>();
    ArrayList<String> item_sizes = new ArrayList<String>();
    ArrayList<String> sentiments = new ArrayList<String>();

    ArrayList<FoodQuery> foodQueries = new ArrayList<FoodQuery>();

    public FoodItemExtractor(){

    }

    public boolean foundFoodItem(String input) throws IOException, JSONException {

        StringTokenizer st = new StringTokenizer(input, ",");
        JSONObject responseObject = getResponse(input);
//        Log.d("Response from wit.ai: ", responseObject.toString(2));

        JSONObject entities = responseObject.getJSONObject("entities");

        if (!entities.has("item_type")) return false;

        //format foodquery objects for inputs that are comma-separated
        while (st.hasMoreTokens()) {
            String food = st.nextToken();
            Log.d("food: ", food);
            FoodQuery foodQuery = new FoodQuery();

            //set foodquery's item type
            for (int i = 0; i < entities.getJSONArray("item_type").length(); i++) {
                String item_type = entities.getJSONArray("item_type").getJSONObject(i).getString("value");
                if (food.contains(item_type)) {
                    foodQuery.setItemType(item_type);
                    break;
                }
            }

            //set foodquery's serving count
            if (entities.has("item_count")) {
                for (int i = 0; i < entities.getJSONArray("item_count").length(); i++) {
                    double count = entities.getJSONArray("item_count").getJSONObject(i).getDouble("value");
                    if (food.contains(Double.toString(count))) {
                        foodQuery.setServingCount(count);
                        break;
                    }
                }
            }

            //set foodquery's serving size
            if (entities.has("item_size")) {
                for (int i = 0; i < entities.getJSONArray("item_size").length(); i++) {
                    String item_size = entities.getJSONArray("item_size").getJSONObject(i).getString("value");
                    if (food.contains(item_size)) {
                        foodQuery.setServingSize(item_size);
                        break;
                    }
                }
            }
            foodQuery.printFoodQueryInfo();
            foodQueries.add(foodQuery);
        }


//        if (entities.has("positive")) {
//            for (int i = 0; i < entities.getJSONArray("positive").length(); i++) {
//                sentiment = entities.getJSONArray("positive").getJSONObject(i).getString("value");
//                sentiments.add(sentiment);
//            }
//        }
//
//        if (entities.has("negative")) {
//            for (int i = 0; i < entities.getJSONArray("negative").length(); i++) {
//                sentiment = entities.getJSONArray("negative").getJSONObject(i).getString("value");
//                sentiments.add(sentiment);
//            }
//        }

        return true;
    }

    public JSONObject getResponse(String input) throws IOException, JSONException {
        String modInput = input.replace(" ","%20");
        URL url = new URL("https://api.wit.ai/message?v=20170523&q=" + modInput);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", "Bearer D7GCTRMY2GDZORZDG5MGWEAWQSEZGYME");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

        String output = br.readLine();
        JSONObject json = new JSONObject(output);
        conn.disconnect();
        return json;
    }

    public String getFoodItem() {return foodItem; }

    public String getServingSize() {return servingSize; }

    public double getServingCount(){ return servingCount; }

    public ArrayList<String> getTypes() {return item_types; }

    public ArrayList<Integer> getCounts() {return item_counts; }

    public ArrayList<String> getSizes() {return item_sizes; }

    public ArrayList<FoodQuery> getFoodQueries() { return foodQueries; }

}
