package com.example.healthhawk;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.healthhawk.databinding.StatsBinding;

public class StatsActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private StatsBinding binding;

    /* ------------ Class Members ------------ */
    TextView helloTextView;

    AppDatabase database;
    String currentUserEmail;
    BottomNavigationView botNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = StatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        setupNavigation();


    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


    protected void setupNavigation()
    {
        botNavView = (BottomNavigationView) this.findViewById(R.id.botNavView);
        botNavView.setSelectedItemId(R.id.action_stats);

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
//                        intent = new Intent(StatsActivity.this,Workouts.class);
//                        intent.putExtra("USER_ID",currentUserEmail);
//                        startActivity(intent);
                        break;
                    case R.id.action_workout:

                        break;
                    case R.id.action_home:
//                        intent = new Intent(StatsActivity.this,HomeActivity.class);
//                        intent.putExtra("USER_ID",currentUserEmail);
//                        startActivity(intent);
                          finish();

                        break;
                    case R.id.action_food:
                        intent = new Intent(StatsActivity.this,FoodAndCaloriesFunctionality.class);
                        intent.putExtra("USER_ID",currentUserEmail);
                        startActivity(intent);
                        break;
                    case R.id.action_stats:
                        intent = new Intent(StatsActivity.this,StatsActivity.class);
                        intent.putExtra("USER_ID",currentUserEmail);
                        startActivity(intent);
                        break;

                }
                return true;
            }
        });

    }
}