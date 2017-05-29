package project.nutricoach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by anacarolinamexia on 5/24/17.
 */

public class FoodDatabase {

    private String id;
    private String name;
    private boolean sentiment;
    private int frequency;
    private HashMap<String, Object> timeStamps;

    public FoodDatabase (){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String,Object> getTimeStamps() {
        return timeStamps;
    }

    public void setTimeStamps(HashMap<String,Object> timeStamps) {
        this.timeStamps = timeStamps;
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

    public FoodDatabase(String name, String id, boolean sentiment, int frequency, HashMap<String,Object> timeStamps){
        this.name= name;
        this.id= id;
        this.sentiment= sentiment;
        this.frequency = frequency;
        this.timeStamps= timeStamps;
    }
    @Override
    public String toString(){

        return ("Name:" + name + " id: " + id + " sentiment: " + sentiment + " frequency"  + frequency );
    }

}
