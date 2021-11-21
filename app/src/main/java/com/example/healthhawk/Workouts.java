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
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Workouts extends AppCompatActivity {
    private static final String ACTIVITY_NAME = "Main" ;
    FloatingActionButton button;
    ArrayList<String[]> workoutNamesList;
    RecyclerView recyclerView;

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

        workoutNamesList = new ArrayList<String[]>();
        button = findViewById(R.id.add_workout_button);

        // initialize readable database and cursor.
        // then add any prior workouts already in the database, to
        dbRead = db.getReadableDatabase();
        cursor = initCursor(dbRead);
        addDatabaseItems(cursor);

        // initialize writable database and cursor.
        dbWrite = db.getWritableDatabase();

        // set adapter
        recyclerView = findViewById(R.id.recyclerView_workouts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        WorkoutsAdapter adapter = new WorkoutsAdapter(this, workoutNamesList);
        recyclerView.setAdapter(adapter);

        // implement swipe to delete feature
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Log.i("main", "The position is" + viewHolder.getAdapterPosition());
                workoutNamesList.remove(viewHolder.getAdapterPosition());
                adapter.notifyDataSetChanged();
            }
        }).attachToRecyclerView(recyclerView);


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

    protected  void addDatabaseItems(Cursor cursor) {
        String nameCol = DatabaseHelper.KEY_WORKOUT_NAME;
        String setsCol = DatabaseHelper.KEY_SETS;
        String exercisesCol = DatabaseHelper.KEY_EXERCISES;
        String exerciseTimeCol = DatabaseHelper.KEY_EXERCISE_TIME;
        String restCol = DatabaseHelper.KEY_REST_TIME;
        String recoveryCol = DatabaseHelper.KEY_RECOVERY_TIME;
        // Loop through each element in cursor, and retreive the message
        // Also log the message
        while(cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(nameCol));
            String sets = cursor.getString(cursor.getColumnIndexOrThrow(setsCol));
            String exerciseTime = cursor.getString(cursor.getColumnIndexOrThrow(exerciseTimeCol));
            String coolDownSets = cursor.getString(cursor.getColumnIndexOrThrow(restCol));
            String coolDownExercises = cursor.getString(cursor.getColumnIndexOrThrow(recoveryCol));
            String exercises = cursor.getString(cursor.getColumnIndexOrThrow(exercisesCol));
            String[] record =  {id, sets, exerciseTime, coolDownSets, coolDownExercises, exercises};
            workoutNamesList.add(record);
            //Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(
            //cursor.getColumnIndexOrThrow( ChatDatabaseHelper.KEY_MESSAGE)));
        }

        // Log the amount of columns in the database, and then print out what they are
        // Log.i(ACTIVITY_NAME,"Cursor’s  column count =" + cursor.getColumnCount() );
        // for (int i = 0; i < cursor.getColumnCount(); i++) {Log.i(ACTIVITY_NAME,cursor.getColumnName(i));}
        cursor.close();

    }

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

    public void openAddWorkout() {
        Intent intent = new Intent(this, AddWorkout.class);
        launchSomeActivity.launch(intent);
    }

    // consider making a class for the fields to help ie like the yt video did with usernames
    public void updateView(String[] fields) {
        // Add fields to lists
        workoutNamesList.add(fields);
        // log count
        Log.i(ACTIVITY_NAME, "count is; " + workoutNamesList.size());
        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView_workouts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        WorkoutsAdapter adapter = new WorkoutsAdapter(this, workoutNamesList);
        recyclerView.setAdapter(adapter);


    }



    public void onDestroy() {
        super.onDestroy();
        db.close();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }

    private class PopulateView extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {

            return null;
        }
    }
}