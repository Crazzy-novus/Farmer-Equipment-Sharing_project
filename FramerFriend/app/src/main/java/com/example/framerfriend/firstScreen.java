package com.example.framerfriend;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class firstScreen extends AppCompatActivity {
    TextView FarmerFriend;

    LottieAnimationView lottie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);




        FarmerFriend = findViewById(R.id.FarmerFriend);
        lottie = findViewById(R.id.lottie);

        FarmerFriend.animate().translationY(-1400).setDuration(0).setStartDelay(0);
        lottie.animate().translationX(00).setDuration(0).setStartDelay(0);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        },1500);
    }
}