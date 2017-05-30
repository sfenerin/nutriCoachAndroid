package project.nutricoach;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anacarolinamexia on 5/20/17.
 */

public class InfoActivity extends Activity {

    private double calories ;
    private double protein;
    private double fat;
    private double carbs;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Button submitButton = (Button) findViewById(R.id.SubmitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                getUserDetails();
            }
        });

    }
    private void getUserDetails(){

        String name = ((EditText)findViewById(R.id.userName)).getText().toString();

        double age= Float.parseFloat(((EditText) findViewById(R.id.userAge)).getText().toString());
        double height= Float.parseFloat(((EditText) findViewById(R.id.userHeight)).getText().toString());
        double weight= Float.parseFloat(((EditText) findViewById(R.id.userWeight)).getText().toString());
        boolean female= ((RadioButton) findViewById(R.id.radioFemale)).isChecked();
        boolean vegan = ((CheckBox)findViewById(R.id.veganCB)).isChecked();
        boolean vegetarian = ((CheckBox)findViewById(R.id.vegetarianCB)).isChecked();
        boolean glutenFree= ((CheckBox)findViewById(R.id.glutenCB)).isChecked();
        boolean dairyFree= ((CheckBox)findViewById(R.id.lactoseCB)).isChecked();
        boolean eggFree= ((CheckBox)findViewById(R.id.eggCB)).isChecked();
        boolean peanutFree= ((CheckBox)findViewById(R.id.peanutCB)).isChecked();
        boolean treeNutFree= ((CheckBox)findViewById(R.id.treeCB)).isChecked();
        boolean soyFree= ((CheckBox)findViewById(R.id.soyCB)).isChecked();
        boolean fishFree= ((CheckBox)findViewById(R.id.fishCB)).isChecked();
        boolean shellfishFree= ((CheckBox)findViewById(R.id.shellfishCB)).isChecked();



        RadioGroup radioButtonGroup = (RadioGroup)findViewById(R.id.radioActivity);
        int radioButtonID = ((RadioGroup)findViewById(R.id.radioActivity)).getCheckedRadioButtonId();
        View radioButton = radioButtonGroup.findViewById(radioButtonID);
        int activity = radioButtonGroup.indexOfChild(radioButton);
        System.out.println("Activity: " + activity);
        double [] activityMultiplier = {1, 1.2, 1.375, 1.55, 1.95};
        double bmr;

        if(!female){
            bmr = 10 * .453592 * weight + 6.25 * 2.54 * height - 5 * age + 5;
        }else{
            bmr = 10 * .453592 * weight + 6.25 * 2.54 * height - 5 * age - 161;
        }

         calories = bmr* activityMultiplier[activity];
         protein = .453592 * weight * (.8 + (activity * .25));
         fat =  .25*calories / 9;
         carbs = .55*calories / 4;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        User newUser = new User(name,getIntent().getStringExtra("USER_EMAIL"), user.getUid(), age,  female, height, weight, bmr, calories,  protein, fat,  carbs, activity, vegan, vegetarian, glutenFree, dairyFree, eggFree, peanutFree, treeNutFree, soyFree, fishFree, shellfishFree);
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(user.getUid()).setValue(newUser);

        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> userValues = toMap();
        childUpdates.put("/users/" + user.getUid() + "/dataToday/", userValues);
        mDatabase.updateChildren(childUpdates);
        Intent intent = new Intent(InfoActivity.this, MainActivity.class);
        startActivity(intent);

    }

    private Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("carbsToday", carbs);
        result.put("fatToday", fat);
        result.put("proteinToday", protein);
        result.put("caloriesToday", calories);
        result.put("lastUpdate", System.currentTimeMillis());
        return result;
    }
}

