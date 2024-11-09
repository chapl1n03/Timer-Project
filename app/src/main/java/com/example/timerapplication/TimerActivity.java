package com.example.timerapplication;

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
    private Button startButton, pauseButton, resetButton;
    private CountDownTimer countDownTimer;
    private long timeInMillis, timeLeftInMillis;
    private boolean isRunning;

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

        startButton.setOnClickListener(v -> startTimer());
        pauseButton.setOnClickListener(v -> pauseTimer());
        resetButton.setOnClickListener(v -> resetTimer());
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
                MediaPlayer mediaPlayer = MediaPlayer.create(TimerActivity.this, R.raw.notification_sound);
                mediaPlayer.start();
                Toast.makeText(TimerActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();
            }
        }.start();

        isRunning = true;
    }

    private void pauseTimer() {
        if (isRunning) {
            countDownTimer.cancel();
            isRunning = false;
        }
    }

    private void resetTimer() {
        pauseTimer();
        timerDisplay.setText("00:00:00");
    }

    private void updateTimerDisplay() {
        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        timerDisplay.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }
}

