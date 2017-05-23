package project.nutricoach;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Shawn on 5/18/2017.
 */

public class FoodItemExtractor {
    String foodItem;
    String servingSize;
    int servingCount;

    public FoodItemExtractor(){

    }

    //parse the input string, which would be something like "I had 2 slices of bacon".
    // then if we are doing this from that example, servingCount should be set to 2
    // and servingSize should be "slice"

    public boolean foundFoodItem(String input) throws IOException {
        foodItem = input;
        getResponse("I had "+ input);
        servingCount = 1;//placeholder
        servingSize = ""; //placeholder
        System.out.println(getResponse("I had "+ input));
        return true;
    }

    public String getResponse(String input) throws IOException {
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

        String output;
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            System.out.println(output);
        }

        conn.disconnect();
        return "";
    }


    public String getFoodItem(){
        return foodItem;
    }

    public String getServingSize() {return servingSize; }

    public int getServingCount(){ return servingCount; }
}
