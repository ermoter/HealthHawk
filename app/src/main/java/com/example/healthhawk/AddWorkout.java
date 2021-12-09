package com.example.healthhawk;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class AddWorkout extends AppCompatActivity {
    static final String RESULT_VALUE = "fields" ;
    // Key value for intent
    String currentField;
    // Prepare global variables for each field within the activity
    EditText workoutName;
    EditText numberOfSets;
    EditText exercises;
    TextView exerciseTime;
    TextView restTime;
    TextView recovery;
    Button okButton;
    // Place holders for the values the user enters when selecting their intervals
    String exerciseTimeValue = "";
    String restTimeValue = "";
    String recoveryValue = "";
    // list which populates recycler view for Workouts activity
    String[] workoutNames;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);

        // Receive workouts list from intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) workoutNames = bundle.getStringArray("Workout Names");

        // Find each view by their ID
        workoutName = findViewById(R.id.workout_name);
        numberOfSets = findViewById(R.id.number_of_sets);
        exercises = findViewById(R.id.exercises);
        exerciseTime = findViewById(R.id.exercise_time);
        restTime = findViewById(R.id.rest);
        recovery = findViewById(R.id.recovery);
        okButton = findViewById(R.id.confirm_selections_addWorkouts);

        // Set click listeners for each text view so that the intervals fragment is called
        exerciseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserInput("exerciseLength");
            }
        });
        restTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserInput("rest");
            }
        });

        recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserInput("recovery");
            }
        });

        // Set on click listener for submit button
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean errorFlag = checkInput();
                if (errorFlag != true) sendData();

            }
        });

    }

    // On activity result launcher for when interval data is returned back from fragment
    ActivityResultLauncher<Intent> intervalsActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        String resultValue = data.getStringExtra(IntervalsFragment.RESULT_VALUE);
                        if (currentField == "rest") {
                            restTimeValue = resultValue;
                            String text = restTime.getText().toString().split(":")[0];
                            restTime.setText(text + ": " + resultValue + " Minutes");
                        }
                        else if (currentField == "recovery")  {
                            recoveryValue = resultValue;
                            String text = recovery.getText().toString().split(":")[0];
                            recovery.setText(text + ": " + resultValue + " Minutes");
                        }else {
                            exerciseTimeValue = resultValue;
                            String text = exerciseTime.getText().toString().split(":")[0];
                            exerciseTime.setText(text + ": " + resultValue + " Minutes");
                        }
                    }
                }
            });

    /** Private helper function which calls a new activity that allows user to input
     * Their intervals using a number picker*/
    private void getUserInput(String fieldName) {
        currentField = fieldName;
        // Open activity fragment for result
        Intent intent = new Intent(AddWorkout.this, Intervals.class);
        intervalsActivityResultLauncher.launch(intent);
    }

    private boolean checkInput() {
        boolean flag = false;

        String[] fields = {workoutName.getText().toString(), numberOfSets.getText().toString(),
                exercises.getText().toString(), exerciseTimeValue, restTimeValue, recoveryValue};

        if ( Arrays.asList(fields).contains("") ) {
            flag = true;
            Toast.makeText(this, R.string.workouts_error_fields_not_filled, Toast.LENGTH_SHORT).show();
        }else if (inDatabase(fields[0]) == true) {
            flag = true;
            Toast.makeText(this, R.string.workouts_error_already_in_database, Toast.LENGTH_SHORT).show();
        }else if (isZero(fields[1])) { // check is the number of sets is greater than 0
            flag = true;
            Toast.makeText(this, R.string.workouts_error_zero_sets , Toast.LENGTH_SHORT).show();
        }else if (exerciseTimeValue.equals("0:00")) {
            flag = true;
            Toast.makeText(this, R.string.workouts_error_exercise_zero, Toast.LENGTH_SHORT).show();
        }

        return flag;
    }

    private void sendData() {
        String[] fields = {workoutName.getText().toString(), numberOfSets.getText().toString(),
                exercises.getText().toString(), exerciseTimeValue, restTimeValue, recoveryValue};
        Intent resultIntent = new Intent();
        resultIntent.putExtra(RESULT_VALUE, fields);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private boolean inDatabase(String name) {
        boolean flag = false;
        if ( Arrays.asList(workoutNames).contains(name) ) {
            flag = true;
        }
        return flag;
    }

    // Helper function which checks if a string value is equivalent to zero
    private boolean isZero(String value) {
        boolean flag = false;
        int val = Integer.parseInt(value);
        if (val == 0) flag = true;
        return flag;
    }


}