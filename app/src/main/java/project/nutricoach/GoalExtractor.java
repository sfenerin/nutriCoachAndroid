package project.nutricoach;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by trinasarkar on 6/10/17.
 */

public class GoalExtractor {

    public String goal = null;
    public String goalFood = null;

    public GoalExtractor() {

    }

    public boolean foundGoal(String input) throws IOException, JSONException {

        JSONObject responseObject = getResponse(input);
        JSONObject entities = responseObject.getJSONObject("entities");

        if (entities.has("goal")) {
            JSONObject userGoal = entities.getJSONArray("goal").getJSONObject(0);
            goal = userGoal.getString("value");

            if (userGoal.getJSONObject("entities").has("item_type")) {
                goalFood = userGoal.getJSONObject("entities").getJSONArray("item_type").getJSONObject(0).getString("value");
                return true;
            }
        }
        return false;
    }

    public String getGoal() { return goal; }

    public String getGoalFood() { return goalFood; }

    public boolean hasGoal() { return goal != null; }

    public boolean hasGoalFood() { return goalFood != null; }

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

}
