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

    private String process(String input) throws UnsupportedEncodingException {
        String response = "";
        FoodItemExtractor foodFinder = new FoodItemExtractor();

        if(foodFinder.foundFoodItem(input)){
            String foodQuery = foodFinder.getFoodItem();
            JSONObject foodInfo = null;
            try {
                foodInfo = getFoodInfo(foodQuery);
                updateAndStoreInfo(foodInfo);
                response = foodInfo.toString(2);
            } catch (JSONException e) {
                response = "Sorry, I didn't quite get that. Could you try rephrasing or being more specific?";
            }
        } else {
            response = "Sorry, I didn't quite get that. Could you try rephrasing or being more specific?";
        }
        return response;
    }

    private boolean updateAndStoreInfo(JSONObject foodInfo) throws JSONException {
        JSONObject parsedFoodInfo = foodInfo.getJSONObject("result").getJSONObject("food");
//        Log.v("parsed food: ", parsedFoodInfo.toString(2));
        Log.v("parsed food: ", parsedFoodInfo.toString(2));
        return true;
    }

    private JSONObject getFoodInfo(String foodQuery) throws UnsupportedEncodingException, JSONException {
        JSONArray responseArray = api.getFoodItems(foodQuery).getJSONObject("result").getJSONObject("foods").getJSONArray("food");
        for(int i = 0; i < responseArray.length(); i ++){
            String foodType = responseArray.getJSONObject(i).get("food_type").toString();
            if(notGeneric(foodType)){

                String food_id = responseArray.getJSONObject(i).get("food_id").toString().replaceAll("\\s","");
                Log.d("result", api.getFoodItem(Long.parseLong(food_id)).toString(2));
                return api.getFoodItem(Long.parseLong(food_id));
            }
        }
        String food_id = responseArray.getJSONObject(0).get("food_id").toString().replaceAll("\\s","");
        return api.getFoodItem(Long.parseLong(food_id));
    }

    private boolean notGeneric(String foodType){return !foodType.equals("Generic");}

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
        }
        return "";
    }
}