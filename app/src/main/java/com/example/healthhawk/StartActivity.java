package com.example.healthhawk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        getSupportActionBar().hide();

    }

    protected void onStart() {
        super.onStart();

    }

    protected void onResume() {
        super.onResume();
        Runnable r = new Runnable() {
            @Override
            public void run(){
                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 3000);

    }
}