package com.example.healthhawk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;

public class Timer extends AppCompatActivity {
    Intent intent;
    String[] fields;
    CountDownTimer timer;
    TextView timerTextView;
    TextView title;
    TextView header;
    TextToSpeech t1;
    ImageButton playPause;
    ImageButton ff;
    ImageButton rewind;
    TextView exerciseNumberTV;
    TextView setNumberTV;

    String workoutName;
    int numberOfSets;
    long timePerExercise;
    long restBetweenExercises;
    long recoveryBetweenSets;
    String[] exercises;
    int exerciseIndex;

    int intervalsCompleted = 0;
    int intervals;
    int setsRemaining;
    long timeRemaining;
    boolean recoveryFlag;
    boolean restFlag;
    boolean pauseFlag = false;

    int progressBarCounter = 0;
    ProgressBar progressBar;

    MediaPlayer mp;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

         mp = MediaPlayer.create(this, R.raw.timer_tick);

        progressBar = findViewById(R.id.timer_progress_bar);
        title = findViewById(R.id.timer_workout_title);
        header = findViewById(R.id.timer_currentWorkout_tv);
        playPause = findViewById(R.id.imageButton);
        ff = findViewById(R.id.imageButton2);
        rewind = findViewById(R.id.imageButton3);
        exerciseNumberTV = findViewById(R.id.exercise_number_tv);
        setNumberTV = findViewById(R.id.set_number_tv);

        intent = getIntent();
        fields = intent.getStringArrayExtra("timer");

        workoutName = fields[0];
        numberOfSets = Integer.parseInt(fields[1]) ;;
        exercises = fields[2].split(",");
        timePerExercise = getExerciseTime();
        restBetweenExercises = getRestTime();
        recoveryBetweenSets = getRecoveryTime();

        exerciseIndex = 0;

        restFlag = (restBetweenExercises > 0) ? true:false;
        recoveryFlag = (recoveryBetweenSets > 0) ? true:false;
        intervals = (restFlag) ? exercises.length + (exercises.length-1) : exercises.length;
        setsRemaining = numberOfSets-1;

        timerTextView = findViewById(R.id.tv_timer);

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

        // Set on click listeners for the image buttons
        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseTimer();
            }
        });

        ff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fastForward();
            }
        });

        rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rewind();
            }
        });

        title.setText(workoutName);
        //intervalsCompleted = -1;
        //startTimer(1000);
        initWorkout();

    }

    private long getRecoveryTime() {
        long recovery;
        String[] recoveryTime = fields[5].split(":");
        if (recoveryTime[0] == "0") {
            recovery = Long.parseLong(recoveryTime[1]) * 1000;
        }else {
            long minutesConversion = Long.parseLong(recoveryTime[0]) * 60 * 1000;
            recovery = Long.parseLong(recoveryTime[1]) * 1000  + minutesConversion;
        }
        return recovery;
    }

    private long getRestTime() {
        long rest;
        String[] restTime = fields[4].split(":");
        if (restTime[0] == "0") {
            rest = Long.parseLong(restTime[1]) * 1000;
        }else {
            long minutesConversion = Long.parseLong(restTime[0]) * 60 * 1000;
            rest = Long.parseLong(restTime[1]) * 1000  + minutesConversion;
        }
        return rest;
    }

    private long getExerciseTime() {
        long time;
        String[] exerciseTime = fields[3].split(":");
        if (exerciseTime[0] == "0") {
            time = Long.parseLong(exerciseTime[1]) * 1000;
        }else {
            long minutesConversion = Long.parseLong(exerciseTime[0]) * 60 * 1000;
            time = Long.parseLong(exerciseTime[1]) * 1000  + minutesConversion;
        }
        return time;
    }

    private void decrementTime(int seconds) {
        int minutes;
        if (seconds > 60) {
            minutes =  Math.floorDiv(seconds, 60);
            seconds = (seconds - minutes*60);

        }else {
            minutes = 0;
        }

        timerTextView.setText(minutes + ":" + String.format("%02d", seconds));
    }

    private void initRest() {
        progressBarCounter = 0;
        setTextColorBlue();
        t1.speak("Rest", TextToSpeech.QUEUE_FLUSH, null, null);
        header.setText("Rest");
        startTimer(restBetweenExercises);

    }

    private void initRecovery() {
        setsRemaining --;
        exerciseIndex = 0;
        // If there is no recovery interval, reset everything and go strait to the first exercise
        if (!recoveryFlag) {
            intervalsCompleted = 0;
            initWorkout();
        }// else, start a timer for the recovery length specified by the user
        else {
            progressBarCounter = 0;
            setTextColourGreen();
            t1.speak("Recovery", TextToSpeech.QUEUE_FLUSH, null, null);
            header.setText("Recovery");

            intervalsCompleted = -1;
            startTimer(recoveryBetweenSets);
        }
    }

    private void initWorkout() {
        exerciseNumberTV.setText("Exercise " + (exerciseIndex+1) + "/" +  exercises.length);
        setNumberTV.setText("Set " + (numberOfSets-setsRemaining) + "/" +  numberOfSets);
        progressBarCounter = 0;
        setTextColorRed();
        t1.speak(exercises[exerciseIndex], TextToSpeech.QUEUE_FLUSH, null, null);
        header.setText(exercises[exerciseIndex]);
        exerciseIndex ++;
        startTimer(timePerExercise);
    }

    private void startTimer(long seconds) {

        timer = new CountDownTimer(seconds, 1000) {
            int counter = (int)(seconds / 1000);
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished <= 5000) mp.start(); // play tick
                timeRemaining = millisUntilFinished;
                counter--;
                decrementTime(counter);
                progressBarCounter++;
                progressBar.setProgress((int)progressBarCounter*100/((int)seconds/1000));
            }

            @Override
            public void onFinish() {
                intervalsCompleted ++;
                progressBar.setProgress(100);
                if (intervalsCompleted < intervals){
                    if (restFlag && intervalsCompleted % 2 == 0) {
                        initWorkout();
                    }else {
                        initRest();
                    }
                }else {
                    if (setsRemaining > 0) {
                        setTextColorRed();
                        initRecovery();

                    }else {
                        timerTextView.setText("Done");
                    }

                }
            }
        }.start();
    }

    private int timerViewToInt(TextView timerView) {
        int minutes = Integer.parseInt(timerView.getText().toString().split(":")[0]);
        int seconds = Integer.parseInt(timerView.getText().toString().split(":")[1]);
        return minutes*60 + seconds;
    }

    private void pauseTimer() {
        if (pauseFlag) {
            progressBarCounter = 0;
            playPause.setImageResource(android.R.drawable.ic_media_play);
            int time = timerViewToInt(timerTextView);

            startTimer((long) time * 1000);
            pauseFlag = false;
        }else { // Timer is live, pause the timer
            timer.cancel();
            playPause.setImageResource(android.R.drawable.ic_media_pause);
            pauseFlag = true;
        }

    }

    private void fastForward() {
        if (pauseFlag == true) {
            playPause.setImageResource(android.R.drawable.ic_media_play);
            pauseFlag = false;
        }
        timer.cancel();
        intervalsCompleted ++;
        progressBar.setProgress(100);
        if (intervalsCompleted < intervals){
            if (restFlag && intervalsCompleted % 2 == 0) {
                initWorkout();
            }else {
                initRest();
            }
        }else {
            if (setsRemaining > 0) {
                setTextColorRed();
                initRecovery();

            }else {
                timerTextView.setText("Done");
            }
        }
    }

    private void rewind() {
        if (pauseFlag == true) { // if button is clicked when workout is paused, make sure to set the on pause flag to false
            playPause.setImageResource(android.R.drawable.ic_media_play);
            pauseFlag = false;
        }
        if (setsRemaining != numberOfSets - 1 || intervalsCompleted != 0) {
            timer.cancel();
            intervalsCompleted--;
            exerciseIndex --;
            progressBar.setProgress(100);

            if (intervalsCompleted < intervals) { // Regular case where you're in the middle of a set
                if (restFlag && intervalsCompleted % 2 == 0) {
                    if (intervalsCompleted == -2) { // Rewind was called while timer was in recovery interval
                        intervalsCompleted = intervals - 1; // adjust intervals completed and index to last exercise in the cycle
                        exerciseIndex = exercises.length-1;
                        setsRemaining ++;
                    }
                    initWorkout();
                } else {
                    if (intervalsCompleted == -1) {  // Case where you've just started a new set
                        setsRemaining++;
                        if (recoveryFlag) initRecovery();
                        else { // if there is no recovery, then go to the last workout of the previous set
                            intervalsCompleted = intervals - 1;
                            exerciseIndex = exercises.length-1;
                            initWorkout();
                        }
                    }else initRest();
                }
            } else {
                if (setsRemaining > 0) {
                    setTextColorRed();
                    initRecovery();

                } else {
                    timerTextView.setText("Done");
                }
            }
        }

    }

    public void setTextColorBlue() {
        timerTextView.setTextColor(getColor(R.color.blue));
        title.setTextColor(getColor(R.color.blue));
        header.setTextColor(getColor(R.color.blue));
        setNumberTV.setTextColor(getColor(R.color.blue));
        exerciseNumberTV.setTextColor(getColor(R.color.blue));

    }

    public void setTextColourGreen() {
        timerTextView.setTextColor(getColor(R.color.green));
        title.setTextColor(getColor(R.color.green));
        header.setTextColor(getColor(R.color.green));
        setNumberTV.setTextColor(getColor(R.color.green));
        exerciseNumberTV.setTextColor(getColor(R.color.green));
    }

    public void setTextColorRed() {
        timerTextView.setTextColor(getColor(R.color.red));
        title.setTextColor(getColor(R.color.red));
        header.setTextColor(getColor(R.color.red));
        setNumberTV.setTextColor(getColor(R.color.red));
        exerciseNumberTV.setTextColor(getColor(R.color.red));

    }

    @Override
    protected void onDestroy() {
        if(timer!=null)
            timer.cancel();
        super.onDestroy();

    }
}