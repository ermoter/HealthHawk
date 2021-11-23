package com.example.healthhawk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.view.View;
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
    boolean recoveryFlag;
    boolean restFlag;

    int progressBarCounter = 0;
    ProgressBar progressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        progressBar = findViewById(R.id.timer_progress_bar);
        title = findViewById(R.id.timer_workout_title);
        header = findViewById(R.id.timer_currentWorkout_tv);

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

        title.setText(workoutName);
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

        timerTextView.setText(minutes + " : " + String.format("%02d", seconds));
    }

    private void initRest() {
        progressBarCounter = 0;
        setActivityBackgroundColorBlue();
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
            setActivityBackgroundColorGreen();
            t1.speak("Recovery", TextToSpeech.QUEUE_FLUSH, null, null);
            header.setText("Recovery");

            intervalsCompleted = -1;
            startTimer(recoveryBetweenSets);
        }
    }

    private void initWorkout() {
        progressBarCounter = 0;
        setActivityBackgroundColorOrange();
        t1.speak(exercises[exerciseIndex], TextToSpeech.QUEUE_FLUSH, null, null);
        header.setText(exercises[exerciseIndex]);
        exerciseIndex ++;
        startTimer(timePerExercise);
    }

    private void startTimer(long seconds) {
        //final MediaPlayer mp = MediaPlayer.create(this, );
        timer = new CountDownTimer(seconds, 1000) {
            int counter = (int)(seconds / 1000);
            @Override
            public void onTick(long millisUntilFinished) {
                counter--;
                //tv.playSoundEffect(SoundEffectConstants.CLICK);
                decrementTime(counter);
                progressBarCounter++;
                progressBar.setProgress((int)progressBarCounter*100/(5000/1000));
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
                        setActivityBackgroundColorOrange();
                        initRecovery();

                    }else {
                        timerTextView.setText("Done");
                    }

                }
            }
        }.start();
    }

    public void setActivityBackgroundColorBlue() {
        View view = this.getWindow().getDecorView();
        view.setBackgroundResource(R.drawable.blue);
    }

    public void setActivityBackgroundColorGreen() {
        View view = this.getWindow().getDecorView();
        view.setBackgroundResource(R.drawable.green);
    }

    public void setActivityBackgroundColorOrange() {
        View view = this.getWindow().getDecorView();
        view.setBackgroundResource(R.drawable.orange_);

    }


    public void onDestroy() {
        if(timer!=null)
            timer.cancel();
        super.onDestroy();

    }
}