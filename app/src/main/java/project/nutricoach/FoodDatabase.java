package project.nutricoach;

import java.util.ArrayList;

/**
 * Created by anacarolinamexia on 5/24/17.
 */

public class FoodDatabase {

    private String id;
    private boolean sentiment;
    private int frequency;
    private ArrayList<Object> timeStamps;

    public FoodDatabase (){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSentiment() {
        return sentiment;
    }

    public void setSentiment(boolean sentiment) {
        this.sentiment = sentiment;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public FoodDatabase(String id, boolean sentiment, int frequency, ArrayList<Object>timeStamps){
        this.id= id;
        this.sentiment= sentiment;
        this.frequency = frequency;
        this.timeStamps= new ArrayList<Object>();
    }

}
