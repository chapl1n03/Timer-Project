package com.example.timerapplication;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class TimerHistory extends AppCompatActivity {

    private ListView timerHistoryList;
    private DatabaseHelper databaseHelper;
    private ArrayList<String> timerHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        timerHistoryList = findViewById(R.id.timerHistoryList);
        databaseHelper = new DatabaseHelper(this);
        timerHistory = new ArrayList<>();

        loadTimerHistory();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, timerHistory);
        timerHistoryList.setAdapter(adapter);

        // Set up an item click listener if you want to show more details
        timerHistoryList.setOnItemClickListener((parent, view, position, id) -> {
            // Handle item click (e.g., show detailed info or delete)
        });
    }

    private void loadTimerHistory() {
        // Get the list of timers from the database
        List<timer> timers = databaseHelper.getTimerHistory();

        // Check if the list is not empty
        if (timers != null && !timers.isEmpty()) {
            for (timer timer : timers) {
                // Access the duration and end time from the Timer object
                String duration = timer.getDuration();
                String endTime = timer.getEndTime();

                // Add the information to your timerHistory (assuming it's a list or an array)
                timerHistory.add("Duration: " + duration + "\nEnded: " + endTime);
            }
        }
    }

}
