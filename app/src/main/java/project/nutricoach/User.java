package project.nutricoach;

import android.text.format.DateUtils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Date;
import 	java.text.DateFormat;
import java.util.Map;
import java.util.Objects;

/**
 * Created by anacarolinamexia on 5/20/17.
 */

public class User {

    private double age;
    private double height;
    private double weight;
    private double bmr;
    private double calories;
    private double protein;
    private double fat;
    private double carbs;
    private int activity;
    private boolean female;
    private DatabaseReference mDatabase;

    private double caloriesToday;
    private double proteinToday;
    private double fatToday;
    private double carbsToday;

    private long lastUpdate;
    private HashMap<Integer, ArrayList<Object>> foodLog;

    private String email;
    private String id;

    public User (){

    }

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getBmr() {
        return bmr;
    }

    public void setBmr(double bmr) {
        this.bmr = bmr;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }

    public boolean isFemale() {
        return female;
    }

    public void setFemale(boolean female) {
        this.female = female;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastUpdateFormatted() {
        Date date = new Date(lastUpdate);
        DateFormat formatter = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }

    public double getCaloriesToday() {
        return caloriesToday;
    }

    public double getProteinToday() {
        return proteinToday;
    }

    public double getFatToday() {
        return fatToday;
    }

    public double getCarbsToday() {
        return carbsToday;
    }

    public static boolean isYesterday(long date) {
        Calendar now = Calendar.getInstance();
        Calendar cdate = Calendar.getInstance();
        cdate.setTimeInMillis(date);

        now.add(Calendar.DATE,-1);

        return now.get(Calendar.YEAR) == cdate.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == cdate.get(Calendar.MONTH)
                && now.get(Calendar.DATE) == cdate.get(Calendar.DATE);
    }


    public void logFood(Food food, boolean sentiment) {
        mDatabase = FirebaseDatabase.getInstance().getReference();//sentiment is true if positive, false if negative
        if(isYesterday(lastUpdate)) { //reset nutrient counts for the day if it is a new day
            System.out.println("Not today");
            caloriesToday = getCalories();
            fatToday = getFat();
            carbsToday = getCarbs();
            proteinToday = getProtein();
        }

        carbsToday -= food.getCarbs();
        fatToday -= food.getFat();
        proteinToday -= food.getProtein();
        caloriesToday -= food.getCalories();
        lastUpdate = System.currentTimeMillis();
        updateDatabase(food, sentiment);

    }

    public User (String email, String id, double age, boolean female, double height, double weight, double bmr, double calories, double protein, double fat, double carbs, int activity){
        this.email=email;
        this.id =id;
        this.age=age;
        this.female=female;
        this.height= height;
        this.weight=weight;
        this.bmr=bmr;
        this.calories=calories;
        this.protein= protein;
        this.carbs= carbs;
        this.fat=fat;
        this.activity= activity;
        this.lastUpdate = System.currentTimeMillis();
        this.fatToday= fat;
        this.caloriesToday=calories;
        this.proteinToday = protein;
        this.carbsToday = carbs;
        this.foodLog = new HashMap<Integer, ArrayList<Object>>();

    }

    @Override
    public String toString(){
        return "User: " +  email + ", age: " + age + ", height: " + height + ", calories left: " + caloriesToday + ", last update: " + getLastUpdateFormatted();
    }

    private void updateDatabase(Food food, boolean sentiment){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> userValues = toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/users/" + id, userValues);

        // If has never eaten it before
        ArrayList<Object> timeStamps = new  ArrayList<Object>();
        timeStamps.add(System.currentTimeMillis());
        FoodDatabase fdb= new FoodDatabase(food.getID(),sentiment, 1,timeStamps);
        childUpdates.put("/users/" + id + "/foodList/" + food.getID(), fdb);

        // If has eaten it before
        mDatabase.updateChildren(childUpdates);
    }

    private Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email",email);
        result.put("id", id);
        result.put("age", age);
        result.put("female", female);
        result.put("height", height);
        result.put("weight", weight);
        result.put("bmr", bmr);
        result.put("calories", calories);
        result.put("protein", protein);
        result.put("carbs", carbs);
        result.put("fat",fat);
        result.put("activity", activity);
        result.put("carbsToday", carbsToday);
        result.put("fatToday", fatToday);
        result.put("proteinToday", proteinToday);
        result.put("caloriesToday",caloriesToday);
        result.put("lastUpdate",lastUpdate);
        return result;
    }
}
