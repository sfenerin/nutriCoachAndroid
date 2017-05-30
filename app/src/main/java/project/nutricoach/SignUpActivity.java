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

public class SignUpActivity extends Activity {
    private EditText userText;
    private EditText passText;
    private EditText passTextConfirm;
    private Button signUpButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);


        mAuth = FirebaseAuth.getInstance();

        userText= (EditText) findViewById(R.id.userTestEmail);
        passText= (EditText) findViewById(R.id.userTestPassword);
        passTextConfirm=  (EditText) findViewById(R.id.userTestPasswordConfirm);

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


        signUpButton= (Button)  findViewById(R.id.SignUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                signUp();
//                Intent intent = new Intent(SignUpActivity.this, InfoActivity.class);
//                startActivity(intent);

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




    private void signUp() {

        System.out.println("IN HERE ");

        String email = userText.getText().toString();
        String password = passText.getText().toString();
        String confirmPassword = passTextConfirm.getText().toString();

        if (password.compareTo(confirmPassword)!=0) {
            Toast.makeText(SignUpActivity.this, "Passwords Must Match",
                    Toast.LENGTH_SHORT).show();
//            return false;


        }
        else if(isSecurePassword(password)){
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(SignUpActivity.this, InfoActivity.class);
                                intent.putExtra("USER_EMAIL", email);
                                startActivity(intent);
                                FirebaseUser user = mAuth.getCurrentUser();
                            } else {

                                System.out.println(task.getException());
                                Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        } else{
            Toast.makeText(SignUpActivity.this, "Passwords Must be at least 8 Letters and have at least one letter and one Digit",
                    Toast.LENGTH_SHORT).show();
//            return false;

        }
    }

    private boolean isSecurePassword(String password) {

        boolean hasLetter = false;
        boolean hasDigit = false;

        if (password.length() >= 8) {
            for (int i = 0; i < password.length(); i++) {
                char x = password.charAt(i);
                if (Character.isLetter(x)) {

                    hasLetter = true;
                }

                else if (Character.isDigit(x)) {

                    hasDigit = true;
                }

                // no need to check further, break the loop
                if(hasLetter && hasDigit){

                    break;
                }

            }
            if (hasLetter && hasDigit) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

}



