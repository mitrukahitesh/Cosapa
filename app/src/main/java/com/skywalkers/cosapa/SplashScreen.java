package com.skywalkers.cosapa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SplashScreen extends AppCompatActivity {

    public static final int SPLASH_TIME_OUT=2000;

    //Animation



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                Intent intent= new Intent(SplashScreen.this, user != null ? MainActivity.class : Onboarding.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}