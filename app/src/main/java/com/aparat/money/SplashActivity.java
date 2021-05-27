package com.aparat.money;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 5 seconds
                    sleep(100);
                    if(FirebaseAuth.getInstance().getCurrentUser() != null){

                        Intent a = new Intent(getBaseContext(), ProfileActivity.class);
                        startActivity(a);
                        finish();
                    }
                    // After 5 seconds redirect to another intent
                    else {
                        Intent i = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(i);

                        //Remove activity

                    }
                } catch (Exception e) {
                }
            }
        };
        // start thread
        background.start();
    }
}