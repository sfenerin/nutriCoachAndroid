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

/**
 * Created by Shawn on 5/18/2017.
 */

public class FoodItemExtractor {

    String foodItem;
    String servingSize;
    double servingCount;

    ArrayList<String> item_types = new ArrayList<String>();
    ArrayList<Integer> item_counts = new ArrayList<Integer>();
    ArrayList<String> item_sizes = new ArrayList<String>();

    public FoodItemExtractor(){

    }

    //parse the input string, which would be something like "I had 2 slices of bacon".
    // then if we are doing this from that example, servingCount should be set to 2
    // and servingSize should be "slice"

    public boolean foundFoodItem(String input) throws IOException, JSONException {
        JSONObject responseObject = getResponse(input);
        Log.d("Response from wit.ai: ", responseObject.toString(2));
        JSONObject entities = responseObject.getJSONObject("entities");

//        TODO: Check for non-food inputs and return false
// <<<<<<< HEAD
//         String item_type = entities.getJSONArray("item_type").getJSONObject(0).getString("value");

//         int item_count = 1;
//         if (entities.has("item_count"))
//             entities.getJSONArray("item_count").getJSONObject(0).getInt("value");
// //        TODO: handle numbers as word, like "two" instead of "2"

//         String item_size = "";
//         if (entities.has("item_size"))
//             item_size = entities.getJSONArray("item_size").getJSONObject(0).getString("value");

//         foodItem = item_type;
//         servingCount = item_count;
//         servingSize = item_size;
// =======

        String item_type = "";
        for (int i = 0; i < entities.getJSONArray("item_type").length(); i++) {
            item_type = entities.getJSONArray("item_type").getJSONObject(i).getString("value");
            item_types.add(item_type);
        }

        System.out.println(item_types);

        double item_count = 1.0;
        if (entities.has("item_count")) {
            for (int i = 0; i < entities.getJSONArray("item_count").length(); i++) {
                item_count = entities.getJSONArray("item_count").getJSONObject(i).getInt("value");
                item_counts.add((int)item_count);
            }
        }

        System.out.println(item_counts);
//        TODO: handle numbers as word, like "two" instead of "2"

        String item_size = "";
        if (entities.has("item_size")) {
            for (int i = 0; i < entities.getJSONArray("item_size").length(); i++) {
                item_size = entities.getJSONArray("item_size").getJSONObject(i).getString("value");
                item_sizes.add(item_size);
            }
        }

        System.out.println(item_sizes);

        foodItem = "";
        servingSize = "";
        servingCount = 1;
// >>>>>>> 3e8f0fd5e399709b46cef507fb853e23ad1c3e45

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

}
