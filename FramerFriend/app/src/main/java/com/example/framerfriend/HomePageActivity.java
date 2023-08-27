package com.example.framerfriend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;


public class HomePageActivity extends AppCompatActivity {

    ImageView addnewuser;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_page);

        // To get userId from login screen
        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);
        Toast.makeText(HomePageActivity.this,"Add new user** "+userId, Toast.LENGTH_SHORT).show();

        addnewuser = findViewById(R.id.new_user);
        addnewuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                Toast.makeText(HomePageActivity.this,"Add new user", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), UserDetailsActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);

            }
        });
    }
}