package com.example.workfitapplication;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class Timer extends AppCompatActivity {

    TextView timer;
    Button buttonSet;
    Button buttonStartPause;
    Button buttonReset;
    private EditText editText;
    private CountDownTimer countDownTimer;
    private boolean timerRun;
    private long startTimeInMillis;
    private long timeLeftInMillis;
    private long endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);


        editText = findViewById(R.id.edit_text_timer);
        timer = findViewById(R.id.timer);

        buttonStartPause = findViewById(R.id.button_start);
        buttonReset = findViewById(R.id.button_reset);
        buttonSet = findViewById(R.id.set_timer);

        buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = editText.getText().toString();
                if (input.length() == 0){
                    Toast.makeText(Timer.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                long millisInput = Long.parseLong(input) * 60000;
                if (millisInput == 0){
                    Toast.makeText(Timer.this, "Please enter a correct number", Toast.LENGTH_SHORT).show();
                    return;
                }
                setTime(millisInput);
                editText.setText("");
            }
        });

        buttonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timerRun){
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });
        updateCountdownTimer();
    }

    private void setTime(long milliseconds){
        startTimeInMillis = milliseconds;
        resetTimer();
        closeKeyboard();
    }

    private void updateButtons(){
        if (timerRun){
            editText.setVisibility(View.INVISIBLE);
            buttonSet.setVisibility(View.INVISIBLE);
            buttonReset.setVisibility(View.INVISIBLE);
            buttonStartPause.setText(R.string.pause_timer);
        } else {
            editText.setVisibility(View.VISIBLE);
            buttonSet.setVisibility(View.VISIBLE);
            buttonStartPause.setText(R.string.start_timer);

            if (timeLeftInMillis < 1000){
                buttonStartPause.setVisibility(View.INVISIBLE);
            } else {
                buttonStartPause.setVisibility(View.VISIBLE);
            }
            if (timeLeftInMillis < startTimeInMillis){
                buttonReset.setVisibility(View.VISIBLE);
            } else {
                buttonReset.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void closeKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
                timerRun = false;
                updateButtons();
                Toast.makeText(getApplicationContext(), "Time complete!", Toast.LENGTH_SHORT).show();
            }
        }.start();

        timerRun = true;
        updateButtons();
    }

    private void pauseTimer(){
        countDownTimer.cancel();
        timerRun = false;
        updateButtons();
    }

    private void resetTimer(){
        timeLeftInMillis = startTimeInMillis;
        updateCountdownTimer();
        updateButtons();

    }
    private void updateCountdownTimer() {
        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeft;
        if (hours > 0){
            timeLeft = String.format(Locale.getDefault(),"%d:%02d:%02d", hours,minutes,seconds);
        } else {
            timeLeft = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        }

        timer.setText(timeLeft);
    }

    @Override
    protected void onStop(){
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor ed = prefs.edit();

        ed.putLong("startTimeInMillis", startTimeInMillis);
        ed.putLong("millisLeft", timeLeftInMillis);
        ed.putBoolean("timerRunning", timerRun);
        ed.putLong("endTime", endTime);

        ed.apply();

        if (countDownTimer != null){
            countDownTimer.cancel();
        }
    }


    @Override
    protected void onStart(){
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        startTimeInMillis = prefs.getLong("startTimeInMillis", 60000);
        timeLeftInMillis = prefs.getLong("millisLeft", startTimeInMillis);
        timerRun = prefs.getBoolean("timerRunning", false);

        updateCountdownTimer();
        updateButtons();

        if(timerRun){
            endTime = prefs.getLong("endTime", 0);
            timeLeftInMillis = endTime - System.currentTimeMillis();

            if (timeLeftInMillis < 0){
                timeLeftInMillis = 0;
                timerRun = false;
                updateCountdownTimer();
                updateButtons();
            } else {
                startTimer();
            }
        }

    }
}
