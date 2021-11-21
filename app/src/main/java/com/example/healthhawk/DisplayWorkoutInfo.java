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
    TextView coolDownSets;
    TextView coolDownExercise;
    TextView exercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_workout_info);

        intent = getIntent();
        fields = intent.getStringArrayExtra("xd1");

        workoutName = findViewById(R.id.textView_workoutname);
        numberOfSets = findViewById(R.id.textView_sets);
        timePerExercise = findViewById(R.id.textView_exerciseTime);
        coolDownSets = findViewById(R.id.textView_coolDownSets);
        coolDownExercise = findViewById(R.id.textView_coolDown_exercise);
        exercises = findViewById(R.id.textView_exercises);
        populateFields(fields);

        okButton = findViewById(R.id.ok_button);
        startButton = findViewById(R.id.start_button);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
    public void populateFields(String[] fields) {
        TextView[] textViews = {workoutName, numberOfSets, timePerExercise, coolDownSets, coolDownExercise, exercises};
        String[] captions = {"Workout Name: ", "Number Of Sets: ", "Time Per Exercise (minutes): ", "Cooldown Between Sets (minutes): ",
                "Cooldown Between Exercises (minutes): ", "List Of Exercises: "};
        for (int i = 0; i < fields.length; i ++) {
            textViews[i].setText(captions[i] + fields[i]);
        }
        return;
    }

    public void openTimer(String[] fields) {
        //Intent intent = new Intent(this, Timer.class);
        intent.putExtra("timer", fields);
        startActivity(intent);
    }
}