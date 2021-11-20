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

import java.util.Arrays;

public class AddWorkout extends AppCompatActivity {
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
    // default variables for error handling regarding the text views
    String defaultExerciseTime;
    String defaultRestTime;
    String defaultRecovery;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);

        // Find each view by their ID
        workoutName = findViewById(R.id.workout_name);
        numberOfSets = findViewById(R.id.number_of_sets);
        exercises = findViewById(R.id.exercises);
        exerciseTime = findViewById(R.id.exercise_time);
        restTime = findViewById(R.id.rest);
        recovery = findViewById(R.id.recovery);
        okButton = findViewById(R.id.confirm_selections_addWorkouts);

        defaultExerciseTime = exerciseTime.getText().toString();
        defaultRestTime = restTime.getText().toString();
        defaultRecovery = exerciseTime.getText().toString();

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
                            String text = restTime.getText().toString();
                            restTime.setText(text + ": " + resultValue + " Minutes");
                        }
                        else if (currentField == "recovery")  {
                            String text = recovery.getText().toString();
                            recovery.setText(text + ": " + resultValue + " Minutes");
                        }else {
                            String text = exerciseTime.getText().toString();
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
                exercises.getText().toString(), exerciseTime.getText().toString(), restTime.getText().toString(), recovery.getText().toString()};

        if ( Arrays.asList(fields).contains("") ) {
            flag = true;
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }else if (defaultExerciseTime == fields[3] || defaultRestTime == fields[4] || defaultRecovery == fields[5]) {
            flag = true;
            Toast.makeText(this, "Please enter your intervals by clicking the respective texts", Toast.LENGTH_SHORT).show();
        }

        return flag;
    }

    private void sendData() {
        String[] fields = {workoutName.getText().toString(), numberOfSets.getText().toString(),
                exercises.getText().toString(), exerciseTime.getText().toString(), restTime.getText().toString(), recovery.getText().toString()};
    }

}