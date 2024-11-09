package com.example.timerapplication;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SoundActivity extends AppCompatActivity {

    private RadioGroup soundRadioGroup;
    private Button playPreviewButton;
    private Button saveSettingsButton;
    private MediaPlayer mediaPlayer;
    private SharedPreferences sharedPreferences;

    private static final String PREFS_NAME = "SoundSettingsPrefs";
    private static final String SOUND_ENABLED_KEY = "sound_enabled";
    private static final String SELECTED_SOUND_KEY = "selected_sound";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        // Initialize UI elements
        soundRadioGroup = findViewById(R.id.soundRadioGroup);
        playPreviewButton = findViewById(R.id.playPreviewButton);
        saveSettingsButton = findViewById(R.id.saveSettingsButton);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Initialize MediaPlayer for sound preview
        mediaPlayer = new MediaPlayer();

        // Load saved settings
        loadSettings();

        // Set listeners
        playPreviewButton.setOnClickListener(v -> playSelectedSound());

        saveSettingsButton.setOnClickListener(v -> saveSettings());
    }

    // Load saved settings from SharedPreferences
    private void loadSettings() {
        String savedSound = sharedPreferences.getString(SELECTED_SOUND_KEY, "sound_1");

        // Set the selected sound in the RadioGroup
        switch (savedSound) {
            case "sound_1":
                ((RadioButton) findViewById(R.id.sound1RadioButton)).setChecked(true);
                break;
            case "sound_2":
                ((RadioButton) findViewById(R.id.sound2RadioButton)).setChecked(true);
                break;
            case "sound_3":
                ((RadioButton) findViewById(R.id.sound3RadioButton)).setChecked(true);
                break;
        }
    }

    // Play the selected sound
    private void playSelectedSound() {
        String selectedSound = getSelectedSound();

        // Release any previous media player instance
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
        }

        switch (selectedSound) {
            case "sound_1":
                mediaPlayer = MediaPlayer.create(this, R.raw.sound01);
                break;
            case "sound_2":
                mediaPlayer = MediaPlayer.create(this, R.raw.sound02);
                break;
            case "sound_3":
                mediaPlayer = MediaPlayer.create(this, R.raw.sound03);
                break;
        }

        mediaPlayer.start(); // Play the selected sound
    }

    // Get the selected sound based on the RadioButton checked
    private String getSelectedSound() {
        int selectedId = soundRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedId);
        return selectedRadioButton != null ? (String) selectedRadioButton.getTag() : "sound_1";
    }

    // Save the sound settings to SharedPreferences
    private void saveSettings() {
        String selectedSound = getSelectedSound();

        // Save to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SELECTED_SOUND_KEY, selectedSound);
        editor.apply();

        Toast.makeText(this, "Settings Saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release(); // Release the media player when the activity is destroyed
        }
    }
}
