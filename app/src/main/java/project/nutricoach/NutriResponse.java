package project.nutricoach;
import com.fatsecret.platform.services.FatsecretService;
import com.fatsecret.platform.model.CompactFood;
import com.fatsecret.platform.services.Response;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Shawn on 5/15/2017.
 */

public class NutriResponse implements Callable<String> {
    String key = "8a87c3522c2c4298a57ad80a4a24354c";
    String secret = "6561cc4a8711477fbc696bd1dcfea90f";
    FatSecretAPI api;
    String input;
    public NutriResponse(String input){
        api = new FatSecretAPI(key, secret);
        this.input = input;
    }

    public String process(String input) throws UnsupportedEncodingException, JSONException {
        JSONArray responseArray = api.getFoodItems(input).getJSONObject("result").getJSONObject("foods").getJSONArray("food");
        Log.d("results:", responseArray.toString(2));
        String response = responseArray.getJSONObject(0).toString(2);
//        JSONObject result = api.getFoodItems(input);
//        String response = api.getFoodItems(input).toString(2);
        return response;
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