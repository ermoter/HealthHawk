package com.example.healthhawk;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Workouts extends AppCompatActivity {
    private static final String ACTIVITY_NAME = "Main" ;
    FloatingActionButton button;
    ArrayList<String[]> workoutInformationList;
    RecyclerView recyclerView;
    WorkoutsAdapter adapter;

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
    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Toast.makeText(Workouts.this, "worked", Toast.LENGTH_SHORT).show();
                        Intent data = result.getData();
                        String[] fields = data.getStringArrayExtra(AddWorkout.RESULT_VALUE);

                        // Update view and database
                        insertToDatabase(fields, dbWrite);
                        updateView(fields);
                    }
                }
            });

    // Get a projection of the workouts database to work with
    public Cursor initCursor(SQLiteDatabase db) {
        return db.query(
                DatabaseHelper.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    // Insert user input into database
    public void insertToDatabase(String[] fields, SQLiteDatabase db) {
        // Get appropriate fields from fields array
        String workoutName = fields[0];
        String sets = fields[1];
        String exercises = fields[2];
        String exerciseTime = fields[3];
        String restTime = fields[4];
        String recoveryTime = fields[5];

        // Add these values to the database
        values = new ContentValues();
        values.put(DatabaseHelper.KEY_WORKOUT_NAME, workoutName);
        values.put(DatabaseHelper.KEY_SETS, sets);
        values.put(DatabaseHelper.KEY_EXERCISES, exercises);
        values.put(DatabaseHelper.KEY_EXERCISE_TIME, exerciseTime);
        values.put(DatabaseHelper.KEY_REST_TIME, restTime);
        values.put(DatabaseHelper.KEY_RECOVERY_TIME, recoveryTime);
        long newRowId = db.insert(DatabaseHelper.TABLE_NAME, null, values);

    }

    // helper function to delete item from recyclerview and database
    public void deleteDatabaseEntry(String name, int position) {
        String selection = DatabaseHelper.KEY_WORKOUT_NAME + " = ?";
        String[] selectionArgs = { "" + name };

        // delete record in database containing selected workout name
        int deletedRows = dbRead.delete(DatabaseHelper.TABLE_NAME, selection, selectionArgs );

        // delete item from recycler view
        workoutInformationList.remove(position);
        adapter.notifyDataSetChanged();

    }

    // Opens activity which allows user to add their workout information
    public void openAddWorkout() {
        Intent intent = new Intent(this, AddWorkout.class);
        String[] workoutNames = getNames();
        intent.putExtra("Workout Names", workoutNames);
        launchSomeActivity.launch(intent);
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
            String nameCol = DatabaseHelper.KEY_WORKOUT_NAME;
            String setsCol = DatabaseHelper.KEY_SETS;
            String exercisesCol = DatabaseHelper.KEY_EXERCISES;
            String exerciseTimeCol = DatabaseHelper.KEY_EXERCISE_TIME;
            String restCol = DatabaseHelper.KEY_REST_TIME;
            String recoveryCol = DatabaseHelper.KEY_RECOVERY_TIME;
            // Loop through each element in cursor, and add to list
            while(cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(nameCol));
                String sets = cursor.getString(cursor.getColumnIndexOrThrow(setsCol));
                String exercises = cursor.getString(cursor.getColumnIndexOrThrow(exercisesCol));
                String exerciseTime = cursor.getString(cursor.getColumnIndexOrThrow(exerciseTimeCol));
                String rest = cursor.getString(cursor.getColumnIndexOrThrow(restCol));
                String recovery = cursor.getString(cursor.getColumnIndexOrThrow(recoveryCol));

                String[] record =  {id, sets, exercises, exerciseTime, rest, recovery};
                workoutInformationList.add(record);

            }
            cursor.close();

            return null;
        }
    }
}