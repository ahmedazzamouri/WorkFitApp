package com.example.workfitapplication;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class Timer extends AppCompatActivity {

    TextView timer;
    static final long START_TIME = 3600000;
    CountDownTimer countDownTimer;
    boolean timerRun;
    long timeLeftInMillis = START_TIME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        timer = (TextView) findViewById(R.id.timer);

        startTimer();
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long timeUntilFinish) {
                timeLeftInMillis = timeUntilFinish;
                updateCountdownTimer();
            }

            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(), "Workout complete!", Toast.LENGTH_SHORT).show();
            }
        }.start();
        timerRun = true;
    }

    private void updateCountdownTimer() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeft = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timer.setText(timeLeft);
    }
}
