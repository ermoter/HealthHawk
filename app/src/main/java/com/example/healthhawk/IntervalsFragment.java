package com.example.healthhawk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

public class IntervalsFragment extends Fragment {
    // Intent variable
    public static final String RESULT_VALUE = "Result Value";
    // Prepare variables for the fragment views
    Button okButton;
    NumberPicker minutesPicker;
    NumberPicker secondsPicker;

    public IntervalsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_intervals, container, false);

        // Find views by their ids
        okButton = RootView.findViewById(R.id.ok_intervals);
        minutesPicker = RootView.findViewById(R.id.minutes_picker);
        secondsPicker = RootView.findViewById(R.id.seconds_picker);

        // Set ranges for both number pickers, and set values to 0
        minutesPicker.setMinValue(0);
        minutesPicker.setMaxValue(59);
        minutesPicker.setValue(0);

        secondsPicker.setMinValue(0);
        secondsPicker.setMaxValue(59);
        minutesPicker.setValue(0);

        // Set on click listener for the ok button, which will
        // then set the textview field to the appropriate values
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get values from pickers
                int minuteValue = minutesPicker.getValue();
                String secondsValue = String.format("%02d", secondsPicker.getValue());
                String resultValue = minuteValue + ":" + secondsValue;

                // Send back results to AddWorkout Activity
                sendResult(resultValue);
            }
        });

        return RootView;
    }

    /** Private function which sends back value to AddWorkout **/
    private void sendResult(String resultValue) {
        //Send back result intent telling AddWorkout activity what value to put in the field
        Intent resultIntent = new Intent();
        resultIntent.putExtra(RESULT_VALUE, resultValue);
        getActivity().setResult(Activity.RESULT_OK, resultIntent);
        getActivity().finish();
    }
}