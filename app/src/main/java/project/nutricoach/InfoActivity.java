package project.nutricoach;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by anacarolinamexia on 5/20/17.
 */

public class InfoActivity extends Activity {

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

        double age= Float.parseFloat(((EditText) findViewById(R.id.userAge)).getText().toString());
        double height= Float.parseFloat(((EditText) findViewById(R.id.userHeight)).getText().toString());
        double weight= Float.parseFloat(((EditText) findViewById(R.id.userWeight)).getText().toString());
        boolean female= ((RadioButton) findViewById(R.id.radioFemale)).isChecked();
        RadioGroup radioButtonGroup = (RadioGroup)findViewById(R.id.radioActivity);
        int radioButtonID = ((RadioGroup)findViewById(R.id.radioActivity)).getCheckedRadioButtonId();
        View radioButton = radioButtonGroup.findViewById(radioButtonID);
        int activity = radioButtonGroup.indexOfChild(radioButton);
        double [] activityMultiplier = {0, 1, 1.2, 1.375, 1.55, 1.95};
        double bmr;

        if(!female){
            bmr = 10 * .453592 * weight + 6.25 * 2.54 * height - 5 * age + 5;
        }else{
            bmr = 10 * .453592 * weight + 6.25 * 2.54 * height - 5 * age - 161;
        }

        double calories = bmr* activityMultiplier[activity];
        double protein = .453592 * weight * (.8 + activity - 1) * .23;
        double fat =  .25*calories / 9;
        double carbs = .55*calories / 4;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        User newUser = new User(getIntent().getStringExtra("USER_EMAIL"), user.getUid(), age,  female, height, weight, bmr, calories,  protein, fat,  carbs, activity);
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(user.getUid()).setValue(newUser);

        Intent intent = new Intent(InfoActivity.this, MainActivity.class);
        startActivity(intent);

    }
}

