package com.example.healthhawk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Intervals extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intervals);

        androidx.fragment.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.intervals_frame, new IntervalsFragment());
        ft.commit();

    }
}