package project.nutricoach;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.StringTokenizer;

/**
 * Created by trinasarkar on 5/29/17.
 */

public class RequestExtractor {


    String recommendationRequest = null;
    String macroRequest = null;

    public RequestExtractor() {

    }

    public boolean foundRequest(String input) throws IOException, JSONException {

        JSONObject responseObject = getResponse(input);
        JSONObject entities = responseObject.getJSONObject("entities");
        System.out.println("Entities: " + entities);
        if (entities.has("recommendation")) {
            recommendationRequest = entities.getJSONArray("recommendation").getJSONObject(0).getString("value");
            return true;
        } else if (entities.has("macro_request")) {
            macroRequest = entities.getJSONArray("macro_request").getJSONObject(0).getString("value");
            return true;
        }
        return false;
    }

    public JSONObject getResponse(String input) throws IOException, JSONException {
        String modInput = input.replace(" ", "%20");
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

    public String getRecommendationRequest() {
        return recommendationRequest;
    }

    public String getMacroRequest() {
        return macroRequest;
    }

}
