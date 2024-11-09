package com.example.timerapplication;

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
        soundSettingsButton.setOnClickListener(v -> startActivity(new Intent(TimerActivity.this, SoundSettingsActivity.class)));
    }

    private void startTimer() {
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
    }

    private void playNotificationSound() {
        mediaPlayer = MediaPlayer.create(this, getResources().getIdentifier(selectedSound, "raw", getPackageName()));
        mediaPlayer.start();
    }

    private void saveTimerHistory(int hours, int minutes, int seconds) {
        String duration = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        databaseHelper.insertTimer(duration);
    }

    private void loadSelectedSound() {
        Cursor cursor = databaseHelper.getSound();
        if (cursor.moveToFirst()) {
            selectedSound = cursor.getString(0);
        } else {
            selectedSound = "default_sound";
        }
    }

    private void updateTimerDisplay() {
        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        timerDisplay.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }
}
