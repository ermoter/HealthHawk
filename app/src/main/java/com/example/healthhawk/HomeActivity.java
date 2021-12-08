/*
Name:   HomeActivity
Author: Sebastian Koller
Version: 3.0 (12/3/2021)
Status: IN PROGRESS
 */
package com.example.healthhawk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;

public class HomeActivity extends AppCompatActivity {

    /* ------------ Class Members ------------ */
    TextView welcomeTextView;
    AppDatabase database;
    String currentUserEmail;
    Button goToWorkouts;

    /* ------------ Lifecycle Methods ------------ */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        assignElements();
        setWelcomeMessage();
    }

    /* ------------ Setup ------------ */
    protected void assignElements()
    {
        welcomeTextView = (TextView) this.findViewById(R.id.welcomeTextView);
        database = new AppDatabase(this);
        try { database.open(); } catch (SQLException throwables) { throwables.printStackTrace(); }
        currentUserEmail = getIntent().getStringExtra("USER_ID");

        goToWorkouts = (Button) this.findViewById(R.id.goToWorkoutsButton);
        goToWorkouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWorkouts();
            }
        });
    }

    /* ------------ Custom Functions ------------ */
    protected void setWelcomeMessage()
    {
        welcomeTextView.setText("Welcome " + database.getName(currentUserEmail) );
    }

    protected void openWorkouts() {
        Intent intent = new Intent(this, Workouts.class);
        startActivity(intent);
    }
}