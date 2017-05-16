package project.nutricoach;
import com.fatsecret.platform.services.FatsecretService;
import com.fatsecret.platform.model.CompactFood;
import com.fatsecret.platform.services.Response;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Shawn on 5/15/2017.
 */

public class NutriResponse {
    String key = "8a87c3522c2c4298a57ad80a4a24354c";
    String secret = "6561cc4a8711477fbc696bd1dcfea90f";
    String url = "http://platform.fatsecret.com/rest/server.api";
    FatSecretAPI api;
    public NutriResponse(){
        api = new FatSecretAPI(key, secret);

    }

    public String process(String input) throws UnsupportedEncodingException, JSONException {

        String response = api.getFoodItems(input).toString(2);
        return response;
//        FatsecretService service = new FatsecretService(key, secret);
//        Response<CompactFood> foodResponse = service.searchFoods(input);
//        List<CompactFood> foods = foodResponse.getResults();
//        return foods.get(0).getName() + " " + foods.get(0).getDescription();
//        return "";
    }
}
