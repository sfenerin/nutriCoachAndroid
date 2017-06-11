package project.nutricoach;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

/**
 * Created by anacarolinamexia on 6/8/17.
 */

public class ProgressActivity extends AppCompatActivity {
    private ProgressBar firstBar = null;
    private TextView foodView = null;
    private TextView daysLeftView = null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        foodView= (TextView) findViewById(R.id.goalText);
        daysLeftView = (TextView) findViewById(R.id.daysLeftView);
        firstBar = (ProgressBar) findViewById(R.id.progressBar);

        getCurrentUser();


        Button homeButton = (Button) findViewById(R.id.userHome);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(ProgressActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        Button profileButton = (Button) findViewById(R.id.userProfile);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(ProgressActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
//        getCurrentUser();
//
//        Button submitButton = (Button) findViewById(R.id.SubmitButtonProfile);
//        submitButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                getUserDetails();
//            }
//        });

    }
    private void getCurrentUser(){
        FirebaseUser cUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference cDatabase= mDatabase.child("users/"+cUser.getUid() + "/weekly");


        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Food food= dataSnapshot.child("goalFood").getValue(Food.class);
                firstBar.setMax(7);
                if (food == null) {
                    foodView.setText("You haven't set a goal yet!");
                    firstBar.setProgress(7);
                    daysLeftView.setText("Set your goal!");
                }
                else{
                    foodView.setText(food.getName());
                    firstBar.setProgress(Integer.parseInt( dataSnapshot.child("daysPast").getValue().toString()));
                    int daysLeft = 7 - Integer.parseInt( dataSnapshot.child("daysPast").getValue().toString());
                    daysLeftView.setText("You have " + daysLeft + " days left to complete your goal");
                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        };
        cDatabase.addValueEventListener(userListener);
        }

    }

