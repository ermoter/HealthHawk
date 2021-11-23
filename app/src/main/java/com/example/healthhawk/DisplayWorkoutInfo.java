package com.example.healthhawk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class DisplayWorkoutInfo extends AppCompatActivity {
    Button okButton;
    Button startButton;
    Intent intent;
    String[] fields;
    TextView workoutName;
    TextView numberOfSets;
    TextView timePerExercise;
    TextView restTime;
    TextView recoveryTime;
    TextView exercises;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_workout_info);

        intent = getIntent();
        fields = intent.getStringArrayExtra("xd1");
        position = intent.getIntExtra("position", 0);


        workoutName = findViewById(R.id.textView_workoutname);
        numberOfSets = findViewById(R.id.textView_sets);
        timePerExercise = findViewById(R.id.textView_exerciseTime);
        restTime = findViewById(R.id.textView_coolDownSets);
        recoveryTime = findViewById(R.id.textView_coolDown_exercise);
        exercises = findViewById(R.id.textView_exercises);
        populateFields(fields);

        okButton = findViewById(R.id.ok_button);
        startButton = findViewById(R.id.start_button);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            } // add start workout and edit button
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open timer activity
                openTimer(fields);
            }
        });

    }

    /** Populates all textviews according to information given by the user **/
    private void populateFields(String[] fields) {
        TextView[] textViews = {workoutName, numberOfSets, exercises, timePerExercise, restTime, recoveryTime};
        String[] captions = {"Workout Name: ", "Number Of Sets: ","List Of Exercises: ", "Time Per Exercise: ", "Rest Between Exercises: ",
                "Recovery Between Sets: "};
        for (int i = 0; i < fields.length; i ++) {
            if (textViews[i] == timePerExercise || textViews[i] == restTime || textViews[i] == recoveryTime ) {
                textViews[i].setText(captions[i] + fields[i] + " Minutes");
            }else {
                textViews[i].setText(captions[i] + fields[i]);
            }
        }
        return;
    }

    private void openTimer(String[] fields) {
        Intent intent = new Intent(this, Timer.class);
        intent.putExtra("timer", fields);
        startActivity(intent);
    }

    private void delete(int position, String name) {

    }
}