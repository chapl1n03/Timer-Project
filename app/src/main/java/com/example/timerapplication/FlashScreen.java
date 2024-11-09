package com.example.timerapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class FlashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_screen);

        // Wait for 2 seconds and then move to Timer screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(FlashScreen.this, TimerActivity.class);
                startActivity(intent);
                finish(); // Finish FlashScreenActivity so the user can't go back to it
            }
        }, 2000); // 2000 milliseconds = 2 seconds
    }
}
