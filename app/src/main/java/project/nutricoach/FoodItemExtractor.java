package project.nutricoach;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

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
        servingCount = 1;//placeholder
        servingSize = ""; //placeholder
        System.out.println(getResponse("I had "+ input));
        return true;
    }

    public String getResponse(String input) throws IOException {
        return "";
//        String url = "https://api.wit.ai/message";
//        String key = "D7GCTRMY2GDZORZDG5MGWEAWQSEZGYME";
//
//        String param1 = "20170523";
//        String param2 = input;
//        String charset = "UTF-8";
//
//        String query = String.format("v=%s&q=%s",
//                URLEncoder.encode(param1, charset),
//                URLEncoder.encode(param2, charset));
//        URL Url = new URL(url + "?" + query);
//        System.out.println(Url.toString());
//
//        URLConnection connection = new URL(url + "?" + query).openConnection();
//        connection.setRequestProperty ("Authorization", "Bearer " + key);
//        connection.setRequestProperty("Accept-Charset", charset);
//        InputStream response = connection.getInputStream();
//        return response.toString();
    }


    public String getFoodItem(){
        return foodItem;
    }

    public String getServingSize() {return servingSize; }

    public int getServingCount(){ return servingCount; }
}
