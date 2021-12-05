package com.example.healthhawk;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.sql.SQLException;
import java.util.ArrayList;

public class Workouts extends AppCompatActivity {
    private static final String ACTIVITY_NAME = "Main" ;
    FloatingActionButton button;
    ArrayList<String[]> workoutInformationList;
    RecyclerView recyclerView;
    WorkoutsAdapter adapter;
    AppDatabase appDatabase = new AppDatabase(this);

    // Initialize a database variable to read and write to
    DatabaseHelper db = new DatabaseHelper(this);
    Cursor cursor;
    ContentValues values;
    SQLiteDatabase dbWrite;
    SQLiteDatabase dbRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts);

        workoutInformationList = new ArrayList<String[]>();
        button = findViewById(R.id.add_workout_button);

        // initialize readable database and cursor.
        // then add any prior workouts already in the database to the list
        try {
            appDatabase.open();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        dbRead = db.getReadableDatabase();
        cursor = initCursor(dbRead);
        PopulateView populateView = new PopulateView();
        populateView.execute();

        // initialize writable database and cursor.
        dbWrite = db.getWritableDatabase();

        // set adapter
        recyclerView = findViewById(R.id.recyclerView_workouts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WorkoutsAdapter(this, workoutInformationList, this);
        recyclerView.setAdapter(adapter);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddWorkout();
            }
        });
    }
    // Create launcher variable inside onAttach or onCreate or global
    ActivityResultLauncher<Intent> launchAddWorkout = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        String[] fields = data.getStringArrayExtra(AddWorkout.RESULT_VALUE);
                        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.workouts_constraint_layout), fields[0] + " Workout Has Been Added Successfully", Snackbar.LENGTH_SHORT);
                        mySnackbar.show();
                        // Update view and database
                        insertToDatabase(fields);
                        updateView(fields);
                    }
                }
            });

    // Get a projection of the workouts database to work with
    public Cursor initCursor(SQLiteDatabase db) {
        return db.query(
                DatabaseHelper.WORKOUTS_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    // Insert user input into database
    public void insertToDatabase(String[] fields) {
        appDatabase.addWorkout(fields);
    }

    // helper function to delete item from recyclerview and database
    public void deleteDatabaseEntry(String name, int position) {
        // delete record in database containing selected workout name
        appDatabase.deleteWorkoutsDatabaseEntry(name, position);

        // delete item from recycler view
        workoutInformationList.remove(position);
        adapter.notifyDataSetChanged();
    }

    // Opens activity which allows user to add their workout information
    public void openAddWorkout() {
        Intent intent = new Intent(this, AddWorkout.class);
        String[] workoutNames = getNames();
        intent.putExtra("Workout Names", workoutNames);
        launchAddWorkout.launch(intent);
    }

    // Helper function which generates all the names for each workout in database
    private String[] getNames() {
        String[] workoutNames = new String[workoutInformationList.size()];
        for(int i = 0; i < workoutInformationList.size(); i++) {
            workoutNames[i] = workoutInformationList.get(i)[0];
        }
        return workoutNames;
    }

    // Update recycler view
    public void updateView(String[] fields) {
        // Add fields to lists
        workoutInformationList.add(fields);
        // log count
        Log.i(ACTIVITY_NAME, "count is; " + workoutInformationList.size());
        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView_workouts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        WorkoutsAdapter adapter = new WorkoutsAdapter(this, workoutInformationList, this);
        recyclerView.setAdapter(adapter);
    }

    public void onDestroy() {
        super.onDestroy();
        db.close();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }

    // Private async class which populates the recycler view with data stored in database
    private class PopulateView extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... Strings) {
            appDatabase.addWorkoutsFromDatabase(workoutInformationList);
            return null;
        }
    }
}