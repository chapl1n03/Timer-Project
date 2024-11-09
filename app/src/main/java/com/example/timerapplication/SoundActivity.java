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

        soundRadioGroup = findViewById(R.id.soundRadioGroup);
        playPreviewButton = findViewById(R.id.playPreviewButton);
        saveSettingsButton = findViewById(R.id.saveSettingsButton);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        mediaPlayer = new MediaPlayer();

        loadSettings();

        playPreviewButton.setOnClickListener(v -> playSelectedSound());

        saveSettingsButton.setOnClickListener(v -> saveSettings());
    }

    private void loadSettings() {
        String savedSound = sharedPreferences.getString(SELECTED_SOUND_KEY, "sound_1");

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
            default:
                ((RadioButton) findViewById(R.id.sound1RadioButton)).setChecked(true); // Default
                break;
        }
    }

    private void playSelectedSound() {
        String selectedSound = getSelectedSound();

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer(); // Recreate the MediaPlayer instance
        }

        try {
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
                default:
                    mediaPlayer = MediaPlayer.create(this, R.raw.sound01); // Default sound
                    break;
            }

            mediaPlayer.start(); // Play the selected sound
        } catch (Exception e) {
            Toast.makeText(this, "Error playing sound", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    private String getSelectedSound() {
        int selectedId = soundRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedId);
        return selectedRadioButton != null ? (String) selectedRadioButton.getTag() : "sound_1"; // Default to "sound_1"
    }

    private void saveSettings() {
        String selectedSound = getSelectedSound();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SELECTED_SOUND_KEY, selectedSound);
        editor.apply();

        Toast.makeText(this, "Settings Saved", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release(); // Ensure the media player is released when the activity is destroyed
        }
    }
}

