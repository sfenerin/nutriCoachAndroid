package project.nutricoach;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by anacarolinamexia on 6/8/17.
 */

public class ProgressActivity extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

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
}
