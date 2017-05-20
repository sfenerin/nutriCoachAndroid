package project.nutricoach;
import android.util.Log;

import com.fatsecret.platform.services.FatsecretService;
import com.fatsecret.platform.model.CompactFood;
import com.fatsecret.platform.services.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Shawn on 5/15/2017.
 */

public class NutriResponse implements Callable<String> {
    String key = "8a87c3522c2c4298a57ad80a4a24354c";
    String secret = "6561cc4a8711477fbc696bd1dcfea90f";
    String url = "http://platform.fatsecret.com/rest/server.api";
    FatSecretAPI api;
    String input;
    public NutriResponse(String input){
        api = new FatSecretAPI(key, secret);
        this.input = input;
    }

    private String process(String input) throws UnsupportedEncodingException, JSONException {
        String response = "";

        FoodItemExtractor foodFinder = new FoodItemExtractor();
        if(foodFinder.foundFoodItem(input)){
            String foodQuery = foodFinder.getFoodItem();
            JSONObject foodInfo = getFoodInfo(foodQuery);
            storeInfo(foodInfo);
            response = foodInfo.toString(2);
        } else {
            response = "Sorry, I didn't quite get that. Could you try rephrasing or being more specific?";
        }
        return response;
    }

    private boolean storeInfo(JSONObject foodInfo){
        return true;
    }

    private JSONObject getFoodInfo(String foodQuery) throws UnsupportedEncodingException, JSONException {
        JSONObject foodList = api.getFoodItems(foodQuery);
        JSONArray responseArray = api.getFoodItems(input).getJSONObject("result").getJSONObject("foods").getJSONArray("food");
        Log.d("results", responseArray.toString(2));
        String food_id = responseArray.getJSONObject(0).get("food_id").toString().replaceAll("\\s","");
        return api.getFoodItem(Long.parseLong(food_id));
    }

    private boolean updateFoodInfo(){
        return true;
    }


    @Override
    public String call() {
        try {
            String response = process(input);
            return response;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}