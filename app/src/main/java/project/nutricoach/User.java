package project.nutricoach;

import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import 	java.text.DateFormat;

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


    public void logFood(Food food) {
        if(!DateUtils.isToday(lastUpdate)) { //reset nutrient counts for the day if it is a new day
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
        this.caloriesToday=calories;
        this.proteinToday = protein;
        this.carbsToday = carbs;

        this.foodLog = new HashMap<Integer, ArrayList<Object>>();

    }

    @Override
    public String toString(){
        return "User: " +  email + ", age: " + age + ", height: " + height + ", calories left: " + caloriesToday + ", last update: " + getLastUpdateFormatted();
    }
}
