package project.nutricoach;

import android.text.format.DateUtils;

import com.google.firebase.auth.ActionCodeResult;
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
import java.util.Map;
import java.util.Objects;

/**
 * Created by anacarolinamexia on 5/20/17.
 */

public class User {

    private ArrayList<FoodDatabase> foodHistory;
    private HashMap<String,ChatMessage> messages;
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
        Calendar now = Calendar.getInstance();
        Calendar cdate = Calendar.getInstance();
        cdate.setTimeInMillis(date);

        now.add(Calendar.DATE, -1);

        return now.get(Calendar.YEAR) == cdate.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == cdate.get(Calendar.MONTH)
                && now.get(Calendar.DATE) == cdate.get(Calendar.DATE);
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
        mDatabase = FirebaseDatabase.getInstance().getReference();
        carbsToday -= food.getCarbs();
        fatToday -= food.getFat();
        proteinToday -= food.getProtein();
        caloriesToday -= food.getCalories();
        System.out.println("calories in log food"+ caloriesToday);
        lastUpdate = System.currentTimeMillis();
        updateTodayValues(food);
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

    public User(String email, String id, double age, boolean female, double height, double weight, double bmr, double calories, double protein, double fat, double carbs, int activity) {
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
        mDatabase = FirebaseDatabase.getInstance().getReference();


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
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(id).child("dataToday").child("caloriesToday").setValue(caloriesToday - food.getCalories());
        mDatabase.child("users").child(id).child("dataToday").child("fatToday").setValue(fatToday - food.getFat());
        mDatabase.child("users").child(id).child("dataToday").child("proteinToday").setValue(proteinToday- food.getProtein());
        mDatabase.child("users").child(id).child("dataToday").child("carbsToday").setValue(carbsToday-food.getCarbs());
        mDatabase.child("users").child(id).child("dataToday").child("lastUpdate").setValue(System.currentTimeMillis());
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
        mDatabase.child("users").child(id).child("dataToday").child("caloriesToday").setValue(calories);
        mDatabase.child("users").child(id).child("dataToday").child("fatToday").setValue(fat);
        mDatabase.child("users").child(id).child("dataToday").child("proteinToday").setValue(protein);
        mDatabase.child("users").child(id).child("dataToday").child("carbsToday").setValue(carbs);
        mDatabase.child("users").child(id).child("dataToday").child("lastUpdate").setValue(System.currentTimeMillis());
    }



}
