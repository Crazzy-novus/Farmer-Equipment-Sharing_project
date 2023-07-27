package com.example.framerfriend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;


public class HomePageActivity extends AppCompatActivity {

    ImageView addnewuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_page);

        addnewuser = findViewById(R.id.new_user);
        addnewuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomePageActivity.this,"Add new user", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), UserDetailsActivity.class);
                startActivity(intent);

            }
        });
    }
}