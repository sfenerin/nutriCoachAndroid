package project.nutricoach;

import android.text.format.DateUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Date;
import 	java.text.DateFormat;

/**
 * Created by anacarolinamexia on 5/20/17.
 */

public class User {


    private ArrayList<FoodDatabase> foodHistory;
    private HashMap<String,ChatMessage> messages;
    private String name;
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
    private boolean isVegan;
    private boolean isVegeterian;
    private Food goalFood;
    private boolean hasGoal = false;
    private int daysPast;
    public Food getGoalFood() {
        return goalFood;
    }

    public void setGoalFood(Food goalFood) {
        this.goalFood = goalFood;
    }

    public boolean hasGoal() {
        return hasGoal;
    }

    public void setHasGoal(boolean hasGoal) {
        this.hasGoal = hasGoal;
    }

    public boolean isVegan() {
        return isVegan;

    }

    public void setVegan(boolean vegan)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(id).child("vegan").setValue(vegan);
        isVegan = vegan;
    }


    public boolean isVegeterian() {
        return isVegeterian;
    }

    public void setVegeterian(boolean vegeterian) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(id).child("vegetarian").setValue(vegeterian);

        isVegeterian = vegeterian;
    }

    public boolean isGlutenFree() {
        return isGlutenFree;
    }

    public void setGlutenFree(boolean glutenFree) {
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabase.child("users").child(id).child("glutenFree").setValue(glutenFree);
        isGlutenFree = glutenFree;
    }

    private boolean isGlutenFree;
    private boolean isDairyFree;
    private boolean isEggFree;
    private boolean isPeanutFree;
    private boolean isTreeNutFree;
    private boolean isBooleanFree;
    private boolean isSoyFree;
    private boolean isFishFree;
    private boolean isShellfishFree;


    private DatabaseReference mDatabase;

    private double caloriesToday;
    private double proteinToday;
    private double fatToday;
    private double carbsToday;

    private long lastUpdate;
    private HashMap<Integer, ArrayList<Object>> foodLog;

    private  String email;
    private String id;

    public User() {

    }

    public void setCaloriesToday(double caloriesToday){
        System.out.println("Setting Calories" + caloriesToday);
        this.caloriesToday= caloriesToday;
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

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
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
        Calendar c1 = Calendar.getInstance(); // today
        c1.add(Calendar.DAY_OF_YEAR, -1); // yesterday

        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date(date)); // your date

        return(c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) ;
    }

    private void initialize(){
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("users").child(id).child("dataToday").child("caloriesToday").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                caloriesToday= (double) Double.parseDouble(snapshot.getValue().toString());
                System.out.println("calories today" + snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase.child("users").child(id).child("dataToday").child("proteinToday").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                proteinToday= (double) Double.parseDouble(snapshot.getValue().toString());
                System.out.println("protein today" + snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase.child("users").child(id).child("dataToday").child("fatToday").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                fatToday= (double) Double.parseDouble(snapshot.getValue().toString());
                System.out.println("fat today" + snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase.child("users").child(id).child("dataToday").child("carbsToday").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                caloriesToday= (double) Double.parseDouble(snapshot.getValue().toString());
                System.out.println("carbs today" + snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


    public void logFood(Food food, boolean sentiment) {

        initialize();
        updateTodayValues(food);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        addFoodToList(food, sentiment);

    }

    public void setProteinToday(double proteinToday) {
        this.proteinToday = proteinToday;
    }

    public void setFatToday(double fatToday) {
        this.fatToday = fatToday;
    }

    public void setCarbsToday(double carbsToday) {
        this.carbsToday = carbsToday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User(String name, String email, String id, double age, boolean female, double height, double weight, double bmr, double calories, double protein, double fat, double carbs, int activity, boolean isVegan, boolean isVegetarian, boolean isGlutenFree, boolean isDairyFree, boolean isEggFree, boolean isPeanutFree, boolean isTreeNutFree, boolean isSoyFree, boolean isFishFree, boolean isShellfishFree) {
        this.name= name;
        this.email = email;
        this.id = id;
        this.age = age;

        this.female = female;
        this.height = height;
        this.weight = weight;
        this.bmr = bmr;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.activity = activity;
        this.isVegan = isVegan;
        this.isVegeterian= isVegetarian;
        this.isDairyFree= isDairyFree;
        this.isGlutenFree = isGlutenFree;
        this.isEggFree=isEggFree;
        this.isPeanutFree= isPeanutFree;
        this.isTreeNutFree = isTreeNutFree;
        this.isSoyFree = isSoyFree;
        this.isFishFree= isFishFree;
        this.isShellfishFree= isShellfishFree;
        mDatabase = FirebaseDatabase.getInstance().getReference();


    }

    public boolean isDairyFree() {
        return isDairyFree;
    }

    public void setDairyFree(boolean dairyFree) {
        isDairyFree = dairyFree;
    }

    public boolean isEggFree() {
        return isEggFree;
    }

    public void setEggFree(boolean eggFree) {
        isEggFree = eggFree;
    }

    public boolean isPeanutFree() {
        return isPeanutFree;
    }

    public void setPeanutFree(boolean peanutFree) {
        isPeanutFree = peanutFree;
    }

    public boolean isTreeNutFree() {
        return isTreeNutFree;
    }

    public void setTreeNutFree(boolean treeNutFree) {
        isTreeNutFree = treeNutFree;
    }

    public boolean isBooleanFree() {
        return isBooleanFree;
    }

    public void setBooleanFree(boolean booleanFree) {
        isBooleanFree = booleanFree;
    }

    public boolean isSoyFree() {
        return isSoyFree;
    }

    public void setSoyFree(boolean soyFree) {
        isSoyFree = soyFree;
    }

    public boolean isFishFree() {
        return isFishFree;
    }

    public void setFishFree(boolean fishFree) {
        isFishFree = fishFree;
    }

    public boolean isShellfishFree() {
        return isShellfishFree;
    }

    public void setShellfishFree(boolean shellfishFree) {
        isShellfishFree = shellfishFree;
    }

    public HashMap<String, ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages( HashMap<String,ChatMessage> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "User: " + email + ", age: " + age + ", height: " + height + ", calories left: " + caloriesToday + ", last update: " + getLastUpdateFormatted();
    }


    private void updateTodayValues(Food food) {
        caloriesToday = caloriesToday - food.getCalories();
        fatToday= fatToday - food.getFat();
        proteinToday= proteinToday - food.getProtein();
        carbsToday = carbsToday - food.getCarbs();
        lastUpdate= System.currentTimeMillis();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(id).child("dataToday").child("caloriesToday").setValue(caloriesToday);
        mDatabase.child("users").child(id).child("dataToday").child("fatToday").setValue(fatToday);
        mDatabase.child("users").child(id).child("dataToday").child("proteinToday").setValue(proteinToday);
        mDatabase.child("users").child(id).child("dataToday").child("carbsToday").setValue(carbsToday);
        mDatabase.child("users").child(id).child("dataToday").child("lastUpdate").setValue(lastUpdate);
    }



    private void addFoodToList(Food food, boolean sentiment) {


        DatabaseReference highFoodReference = mDatabase.child("users/" + id + "/foodList/");

        ValueEventListener foodListener = new ValueEventListener() {
            boolean updated = false;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(food.getID()) && !updated) {
                    System.out.println("Item already exists update frequency");
                    int frequency = Integer.parseInt(dataSnapshot.child(food.getID()).child("frequency").getValue().toString());
                    mDatabase.child("users").child(id).child("foodList").child(food.getID()).child("frequency").setValue(frequency + 1);
                    DatabaseReference newKey = mDatabase.child("users").child(id).child("foodList").child(food.getID()).child("timeStamps").push();
                    newKey.setValue(System.currentTimeMillis());
                    updated = true;

                } else if (!dataSnapshot.hasChild(food.getID())) {
                    System.out.println("Need to add item");
                    FoodDatabase fdb = new FoodDatabase(food.getName(), food.getID(), sentiment, 0, null);
                    mDatabase.child("users").child(id).child("foodList").child(food.getID()).setValue(fdb);
                }
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        highFoodReference.addValueEventListener(foodListener);
    }

    public void addMessage(ChatMessage message){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference messageKey =  mDatabase.child("users").child(id).child("messages").push();
        messageKey.setValue(message);
    }

    public ArrayList<FoodDatabase> getFoodHistory() {

        System.out.println("returning food history");
        return foodHistory;
    }

    public void setFoodHistory(ArrayList<FoodDatabase> foodHistory) {
        this.foodHistory = foodHistory;
    }

    public void resetTodayValues(){
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if(hasGoal && daysPast<7){
            System.out.println("Setting goal");
            mDatabase.child("users").child(id).child("dataToday").child("caloriesToday").setValue(calories - (goalFood.getCalories()/6));
            mDatabase.child("users").child(id).child("dataToday").child("fatToday").setValue(fat - (goalFood.getCalories()/6));
            mDatabase.child("users").child(id).child("dataToday").child("proteinToday").setValue(protein - (goalFood.getCalories()/6));
            mDatabase.child("users").child(id).child("dataToday").child("carbsToday").setValue(carbs -(goalFood.getCarbs()/6));
            mDatabase.child("users").child(id).child("dataToday").child("lastUpdate").setValue(System.currentTimeMillis());
            this.daysPast ++;
        }
        else if(hasGoal && daysPast ==7){
            System.out.println("You can eat");
        }
        else {

            mDatabase.child("users").child(id).child("dataToday").child("caloriesToday").setValue(calories);
            mDatabase.child("users").child(id).child("dataToday").child("fatToday").setValue(fat);
            mDatabase.child("users").child(id).child("dataToday").child("proteinToday").setValue(protein);
            mDatabase.child("users").child(id).child("dataToday").child("carbsToday").setValue(carbs);
            mDatabase.child("users").child(id).child("dataToday").child("lastUpdate").setValue(System.currentTimeMillis());
        }
    }

    public void updateAllFields(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(id).child("name").setValue(name);
        mDatabase.child("users").child(id).child("height").setValue(height);
        mDatabase.child("users").child(id).child("weight").setValue(weight);
        mDatabase.child("users").child(id).child("age").setValue(age);
        mDatabase.child("users").child(id).child("calories").setValue(calories);
        mDatabase.child("users").child(id).child("protein").setValue(protein);
        mDatabase.child("users").child(id).child("activity").setValue(activity);
        mDatabase.child("users").child(id).child("bmr").setValue(bmr);
        mDatabase.child("users").child(id).child("fat").setValue(fat);
        mDatabase.child("users").child(id).child("carbs").setValue(carbs);
        mDatabase.child("users").child(id).child("glutenFree").setValue(isGlutenFree);
        mDatabase.child("users").child(id).child("glutenFree").setValue(isGlutenFree);
        mDatabase.child("users").child(id).child("vegan").setValue(isVegan);
        mDatabase.child("users").child(id).child("vegetarian").setValue(isVegeterian);
        mDatabase.child("users").child(id).child("fishFree").setValue(isFishFree);
        mDatabase.child("users").child(id).child("soyFree").setValue(isSoyFree);
        mDatabase.child("users").child(id).child("shellfishFree").setValue(isShellfishFree);
        mDatabase.child("users").child(id).child("dairyFree").setValue(isDairyFree);
        mDatabase.child("users").child(id).child("treeNutFree").setValue(isTreeNutFree);



    }
    public void cancelGoal(){
        this.hasGoal= false;
    }

    public void setWeeklyGoal(Food food){
        this.goalFood = food;
        this.hasGoal=true;
        this.daysPast= 0;
        resetTodayValues();
    }


}
