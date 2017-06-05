package project.nutricoach;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by anacarolinamexia on 6/5/17.
 */

public class ProfileActivity  extends AppCompatActivity{
    private User currentUser;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getCurrentUser();

        Button submitButton = (Button) findViewById(R.id.SubmitButtonProfile);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                getUserDetails();
            }
        });

    }

    private void getCurrentUser(){
        FirebaseUser cUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference cDatabase= mDatabase.child("users/"+cUser.getUid());


        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("here");
                currentUser = dataSnapshot.getValue(User.class);
                populateFields();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        cDatabase.addValueEventListener(userListener);
    }

    private void populateFields(){
        EditText nameText=  (EditText) findViewById(R.id.userNameProfile);
        nameText.setText(currentUser.getName());

        EditText ageText=  (EditText) findViewById(R.id.userAgeProfile);
        ageText.setText(""+currentUser.getAge());

        EditText weightText=  (EditText) findViewById(R.id.userWeightProfile);
        weightText.setText(""+currentUser.getWeight());

        EditText heightText= (EditText) findViewById(R.id.userHeightProfile);
        heightText.setText("" + currentUser.getHeight());

        RadioButton female= (RadioButton) findViewById(R.id.radioFemaleProfile);
        female.setChecked(currentUser.isFemale());

        RadioButton male = (RadioButton) findViewById(R.id.radioMaleProfile);
        male.setChecked(!currentUser.isFemale());

        RadioGroup activityGroup = (RadioGroup) findViewById(R.id.radioActivityProfile);
        activityGroup.getChildAt(currentUser.getActivity()-1).setSelected(true);


        CheckBox vegan = ((CheckBox)findViewById(R.id.veganCBP));
        vegan.setChecked(currentUser.isVegan());

        CheckBox vegetarian = ((CheckBox)findViewById(R.id.vegetarianCBP));
        vegetarian.setChecked(currentUser.isVegeterian());

        CheckBox glutenFree = ((CheckBox)findViewById(R.id.glutenCBP));
        glutenFree.setChecked(currentUser.isGlutenFree());

        CheckBox dairyFree = ((CheckBox)findViewById(R.id.lactoseCBP));
        dairyFree.setChecked(currentUser.isDairyFree());

        CheckBox eggFree = ((CheckBox)findViewById(R.id.eggCBP));
        eggFree.setChecked(currentUser.isEggFree());

        CheckBox peanutFree = ((CheckBox)findViewById(R.id.peanutCBP));
        peanutFree.setChecked(currentUser.isPeanutFree());

        CheckBox treeNutFree = ((CheckBox)findViewById(R.id.treeCBP));
        treeNutFree.setChecked(currentUser.isTreeNutFree());

        CheckBox soyFree = ((CheckBox)findViewById(R.id.soyCBP));
        soyFree.setChecked(currentUser.isSoyFree());

        CheckBox fishFree = ((CheckBox)findViewById(R.id.fishCBP));
        fishFree.setChecked(currentUser.isFishFree());

        CheckBox shellFishFree = ((CheckBox)findViewById(R.id.shellfishCBP));
        shellFishFree.setChecked(currentUser.isShellfishFree());

    }
    private void getUserDetails(){
        currentUser.setName(((EditText)findViewById(R.id.userNameProfile)).getText().toString());
        currentUser.setAge(Float.parseFloat(((EditText) findViewById(R.id.userAgeProfile)).getText().toString()));
        currentUser.setHeight(Float.parseFloat(((EditText) findViewById(R.id.userHeightProfile)).getText().toString()));
        currentUser.setWeight(Float.parseFloat(((EditText) findViewById(R.id.userWeightProfile)).getText().toString()));
        currentUser.setFemale(((RadioButton) findViewById(R.id.radioFemaleProfile)).isChecked());
        currentUser.setVegan(((CheckBox)findViewById(R.id.veganCBP)).isChecked());
        currentUser.setVegeterian((((CheckBox)findViewById(R.id.vegetarianCBP)).isChecked()));
        currentUser.setGlutenFree(((CheckBox)findViewById(R.id.glutenCBP)).isChecked());
        currentUser.setDairyFree(((CheckBox)findViewById(R.id.lactoseCBP)).isChecked());
        currentUser.setEggFree(((CheckBox)findViewById(R.id.eggCBP)).isChecked());
        currentUser.setPeanutFree(((CheckBox)findViewById(R.id.peanutCBP)).isChecked());
        currentUser.setTreeNutFree(((CheckBox)findViewById(R.id.treeCBP)).isChecked());
        currentUser.setSoyFree(((CheckBox)findViewById(R.id.soyCBP)).isChecked());
        currentUser.setFishFree(((CheckBox)findViewById(R.id.fishCBP)).isChecked());
        currentUser.setShellfishFree(((CheckBox)findViewById(R.id.shellfishCBP)).isChecked());

        boolean female= ((RadioButton) findViewById(R.id.radioFemaleProfile)).isChecked();
        double weight= Float.parseFloat(((EditText) findViewById(R.id.userWeightProfile)).getText().toString());
        double height= Float.parseFloat(((EditText) findViewById(R.id.userHeightProfile)).getText().toString());
        double age= Float.parseFloat(((EditText) findViewById(R.id.userAgeProfile)).getText().toString());
        double calories, protein,fat, carbs;


        RadioGroup radioButtonGroup = (RadioGroup)findViewById(R.id.radioActivityProfile);
        int radioButtonID = ((RadioGroup)findViewById(R.id.radioActivityProfile)).getCheckedRadioButtonId();
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

        currentUser.setCalories(calories);
        currentUser.setProtein(protein);
        currentUser.setFat(fat);
        currentUser.setCarbs(carbs);

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentUser.updateAllFields();

        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);

    }
}
