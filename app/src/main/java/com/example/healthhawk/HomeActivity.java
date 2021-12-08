/*
Name:   HomeActivity
Author: Sebastian Koller
Version: 3.0 (12/3/2021)
Status: IN PROGRESS
 */
package com.example.healthhawk;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;

import java.sql.SQLException;

public class HomeActivity extends AppCompatActivity
{

    /* ------------ Class Members ------------ */
    TextView helloTextView;

    AppDatabase database;
    String currentUserEmail;
    BottomNavigationView botNavView;

    /* ------------ Lifecycle Methods ------------ */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        assignElements();
        setListeners();
        setupNavigation();
        setWelcomeMessage();

        // ANYTHING YOU NEED TO DO TO ACTION BAR
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setTitle("Home");
        //actionBar.hide();
    }

    @Override
    protected void onResume() {
        super.onResume();
        botNavView.setSelectedItemId(R.id.action_home);
    }

    // WHAT YOU NEED TO DO THE HELP MENUS STARTS HERE
    //
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.help_menu, menu);
        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help_menu_item:
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setMessage(
                        "Name:      HomeActivity\n" +
                        "Version:   3.0\n" +
                        "Author:    Sebastian Koller\n\n" +
                        "Description:\n" +
                        "Use the bottom navigation bar to navigate through activities");
                builder.setTitle("Activity Information");
                builder.setNeutralButton("Done", null);
                builder.show();
        }
        return super.onOptionsItemSelected(item);
    }
    //
    // WHAT YOU NEED TO DO THE HELP MENUS ENDS HERE


    /* ------------ Setup ------------ */
    protected void assignElements()
    {
        helloTextView = (TextView) this.findViewById(R.id.welcomeTextView);

        database = new AppDatabase(this);
        try { database.open(); } catch (SQLException throwables) { throwables.printStackTrace(); }
        currentUserEmail = getIntent().getStringExtra("USER_ID");
    }

    protected void setListeners()
    {

    }

    protected void setupNavigation()
    {
        botNavView = (BottomNavigationView) this.findViewById(R.id.botNavView);
        botNavView.setSelectedItemId(R.id.action_home);

        // Navigation Bar
        botNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                Intent intent;
                switch (item.getItemId())
                {
                    // User Clicks Workout
                    case R.id.action_date:
//                        intent = new Intent(HomeActivity.this,Workouts.class);
//                        intent.putExtra("USER_ID",currentUserEmail);
//                        startActivity(intent);
                        break;
                    case R.id.action_workout:
                        intent = new Intent(HomeActivity.this,Workouts.class);
                        intent.putExtra("USER_ID",currentUserEmail);
                        startActivity(intent);
                        break;
                    case R.id.action_home:

                        break;
                    case R.id.action_food:
                        intent = new Intent(HomeActivity.this,FoodAndCaloriesFunctionality.class);
                        intent.putExtra("USER_ID",currentUserEmail);
                        startActivity(intent);
                        break;
                    case R.id.action_stats:
                        intent = new Intent(HomeActivity.this,StatsActivity.class);
                        intent.putExtra("USER_ID",currentUserEmail);
                        startActivity(intent);
                        break;

                }
            return true;
            }
        });

    }

    /* ------------ Custom Functions ------------ */
    protected void setWelcomeMessage()
    {
        String name = database.getName(currentUserEmail);
        String helloMsg = "Hello " + name;
        String welcomeMsg = "Welcome " + name;

        // TextView
        helloTextView.setText(helloMsg);

        // Snackbar
        View pLayout = findViewById(R.id.coordinatorLayout);
        Snackbar snack = Snackbar.make(pLayout,welcomeMsg,Snackbar.LENGTH_SHORT).setAction("Action",null);
        View view = snack.getView();
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;

        view.setLayoutParams(params);

        snack.show();
    }

}