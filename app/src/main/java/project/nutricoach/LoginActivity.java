package project.nutricoach;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by anacarolinamexia on 5/15/17.
 */

public class LoginActivity extends Activity {
    private Button loginButton;
    private EditText userText;
    private EditText passText;
    private Button signUpButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        setUpNotifications();

        mAuth = FirebaseAuth.getInstance();

        userText= (EditText) findViewById(R.id.userText);
        passText= (EditText) findViewById(R.id.userPassword);

        userText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                userText.setText("");
            }
        });

        passText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                passText.setText("");
            }
        });


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
//                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
//                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        loginButton= (Button) findViewById(R.id.LoginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
               login();
            }
        });

        signUpButton= (Button)  findViewById(R.id.SignInButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    private void setUpNotifications(){
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        initNotification(ReminderLunch.class,alarmManager, 13,0,0);

        initNotification(ReminderDinner.class,alarmManager, 6,54,1);

        initNotification(ReminderNight.class,alarmManager, 20,0,2);

        Log.d("NotifcationManager","set each notification");
    }

    public void initNotification(Class reminder,AlarmManager alarmManager,int hour, int minute,int requestCode){
        Intent notifyIntent = new Intent(this,reminder);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestCode, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = initCalendar(hour,minute);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,  calendar.getTimeInMillis(),  AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private Calendar initCalendar(int hour, int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

    private void login(){



        String email = userText.getText().toString();
        String password= passText.getText().toString();
//        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//        startActivity(intent);


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {


//                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            System.out.println("PRoblem" + task.getException());
//                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed."+ task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }

                        // ...
                    }
                });
    }

//    private void signUp(){
//
//        System.out.println("IN HERE ");
//
//        String email = userText.getText().toString();
//        String password= passText.getText().toString();
//
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            Intent intent = new Intent(LoginActivity.this, InfoActivity.class);
//                            intent.putExtra("USER_EMAIL", email);
//                            startActivity(intent);
//                            FirebaseUser user = mAuth.getCurrentUser();
//                        } else {
//
//                            System.out.println(task.getException());
//                            Toast.makeText(LoginActivity.this, "Authentication failed."+ task.getException(),
//                                    Toast.LENGTH_SHORT).show();
//                        }
//
//                        // ...
//                    }
//                });
//    }

}


