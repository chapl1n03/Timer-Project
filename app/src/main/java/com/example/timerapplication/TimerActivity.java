package com.example.timerapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import androidx.appcompat.app.AppCompatActivity;

public class TimerActivity extends AppCompatActivity {

    private EditText inputHours, inputMinutes, inputSeconds;
    private TextView timerDisplay;
    private Button startButton, pauseButton, resetButton, soundSettingsButton;
    private CountDownTimer countDownTimer;
    private long timeInMillis, timeLeftInMillis;
    private boolean isRunning;
    private MediaPlayer mediaPlayer;
    private DatabaseHelper databaseHelper;
    private String selectedSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        inputHours = findViewById(R.id.inputHours);
        inputMinutes = findViewById(R.id.inputMinutes);
        inputSeconds = findViewById(R.id.inputSeconds);
        timerDisplay = findViewById(R.id.timerDisplay);
        startButton = findViewById(R.id.startButton);
        pauseButton = findViewById(R.id.pauseButton);
        resetButton = findViewById(R.id.resetButton);
        soundSettingsButton = findViewById(R.id.soundSettingsButton);
        databaseHelper = new DatabaseHelper(this);

        loadSelectedSound();

        startButton.setOnClickListener(v -> startTimer());
        pauseButton.setOnClickListener(v -> pauseTimer());
        resetButton.setOnClickListener(v -> resetTimer());
        soundSettingsButton.setOnClickListener(v -> startActivity(new Intent(TimerActivity.this, SoundActivity.class)));
    }



    private void startTimer() {
        try {
            int hours = Integer.parseInt(inputHours.getText().toString());
            int minutes = Integer.parseInt(inputMinutes.getText().toString());
            int seconds = Integer.parseInt(inputSeconds.getText().toString());
            timeInMillis = (hours * 3600 + minutes * 60 + seconds) * 1000L;

            countDownTimer = new CountDownTimer(timeInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftInMillis = millisUntilFinished;
                    updateTimerDisplay();
                }

                @Override
                public void onFinish() {
                    timerDisplay.setText("00:00:00");
                    playNotificationSound();
                    Toast.makeText(TimerActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();
                    saveTimerHistory(hours, minutes, seconds);
                }
            }.start();

            isRunning = true;
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid time", Toast.LENGTH_SHORT).show();
        }
    }

    private void playNotificationSound() {
        if (selectedSound != null && !selectedSound.isEmpty()) {
            int soundResourceId = getResources().getIdentifier(selectedSound, "raw", getPackageName());
            if (soundResourceId != 0) {
                mediaPlayer = MediaPlayer.create(this, soundResourceId);
                mediaPlayer.start();
            } else {
                Toast.makeText(this, "Error loading sound", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveTimerHistory(int hours, int minutes, int seconds) {
        String duration = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        String endTime = getCurrentTime();  // Get the current time when the timer ends
        databaseHelper.insertTimer(duration, hours, minutes, seconds, selectedSound, endTime);
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());  // Get the current date and time
    }

    @SuppressLint("Range")
    private void loadSelectedSound() {
        // Get the sound name from the database
        selectedSound = databaseHelper.getSound();  // Now it directly gets a String

        if (selectedSound == null || selectedSound.isEmpty()) {
            selectedSound = "default_sound";  // Use the default sound if no setting is found
        }
    }



    private void updateTimerDisplay() {
        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        timerDisplay.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }

    // Pause Timer
    private void pauseTimer() {
        if (isRunning) {
            countDownTimer.cancel();
            isRunning = false;
        }
    }

    // Reset Timer
    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            timerDisplay.setText("00:00:00");
            isRunning = false;
        }
    }
}



