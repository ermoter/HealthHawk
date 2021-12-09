package com.example.healthhawk;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.healthhawk.databinding.StatsBinding;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.sql.SQLException;

public class StatsActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private StatsBinding binding;

    /* ------------ Class Members ------------ */
    AppDatabase database;
    String currentUserEmail;
    BottomNavigationView botNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new AppDatabase(this);
        try { database.open(); }
        catch (SQLException throwables) { throwables.printStackTrace(); }
        binding = (StatsBinding) StatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
//                new DataPoint(Integer.parseInt(sqLiteDatabase.getStats()), 5),
                new DataPoint(Integer.parseInt(database.getStats()), 5),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);

        setupNavigation();


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.help_menu, menu);
        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help_menu_item:
                AlertDialog.Builder builder = new AlertDialog.Builder(StatsActivity.this);
                builder.setMessage(
                        "Name:      StatsActivity\n" +
                                "Version:   3.0\n" +
                                "Author:    Eric Tran\n\n" +
                                "Description:\n" +
                                "Stats!");
                builder.setTitle("Activity Information");
                builder.setNeutralButton("Done", null);
                builder.show();
        }
        return super.onOptionsItemSelected(item);
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
                        intent = new Intent(StatsActivity.this,Workouts.class);
                        intent.putExtra("USER_ID",currentUserEmail);
                        startActivity(intent);

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