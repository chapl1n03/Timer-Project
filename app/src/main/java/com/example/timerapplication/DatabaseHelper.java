package com.example.timerapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "timer.db";
    private static final int DATABASE_VERSION = 1;

    // Table and column names
    public static final String TABLE_NAME = "timers";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_HOURS = "hours";
    public static final String COLUMN_MINUTES = "minutes";
    public static final String COLUMN_SECONDS = "seconds";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_SOUND = "sound";  // Sound setting column
    public static final String COLUMN_END_TIME = "end_time"; // End time column

    // SQL to create the table
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_HOURS + " INTEGER, " +
                    COLUMN_MINUTES + " INTEGER, " +
                    COLUMN_SECONDS + " INTEGER, " +
                    COLUMN_DURATION + " TEXT, " +
                    COLUMN_SOUND + " TEXT, " +
                    COLUMN_END_TIME + " TEXT);";  // Add sound and end time columns

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the old table and recreate it when upgrading
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Insert timer data into the database, including sound setting and end time
    public void insertTimer(String duration, int hours, int minutes, int seconds, String sound, String endTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DURATION, duration);
        values.put(COLUMN_HOURS, hours);
        values.put(COLUMN_MINUTES, minutes);
        values.put(COLUMN_SECONDS, seconds);
        values.put(COLUMN_SOUND, sound);  // Add sound setting here
        values.put(COLUMN_END_TIME, endTime);  // Save the end time

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Retrieve the sound setting for the last inserted timer (if needed)
    public String getSound() {
        SQLiteDatabase db = this.getReadableDatabase();
        // Get the sound setting of the latest timer inserted
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_SOUND + " FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC LIMIT 1", null);

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String sound = cursor.getString(cursor.getColumnIndex(COLUMN_SOUND));
            cursor.close();
            return sound;
        } else {
            return null; // No sound setting found
        }
    }

    public String getEndTime() {
        SQLiteDatabase db = this.getReadableDatabase();
        // Get the end time of the latest timer inserted
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_END_TIME + " FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC LIMIT 1", null);

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String endTime = cursor.getString(cursor.getColumnIndex(COLUMN_END_TIME));
            cursor.close();
            return endTime;
        } else {
            return null; // No end time found
        }
    }

    public List<timer> getTimerHistory() {
        List<timer> timerHistory = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, COLUMN_ID + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Check if each column exists before accessing
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                int durationIndex = cursor.getColumnIndex(COLUMN_DURATION);
                int hoursIndex = cursor.getColumnIndex(COLUMN_HOURS);
                int minutesIndex = cursor.getColumnIndex(COLUMN_MINUTES);
                int secondsIndex = cursor.getColumnIndex(COLUMN_SECONDS);
                int soundIndex = cursor.getColumnIndex(COLUMN_SOUND);
                int endTimeIndex = cursor.getColumnIndex(COLUMN_END_TIME);

                // Safely access the columns, check if the index is valid (>= 0)
                if (idIndex >= 0 && durationIndex >= 0 && hoursIndex >= 0 && minutesIndex >= 0 &&
                        secondsIndex >= 0 && soundIndex >= 0 && endTimeIndex >= 0) {

                    int id = cursor.getInt(idIndex);
                    String duration = cursor.getString(durationIndex);
                    int hours = cursor.getInt(hoursIndex);
                    int minutes = cursor.getInt(minutesIndex);
                    int seconds = cursor.getInt(secondsIndex);
                    String sound = cursor.getString(soundIndex);
                    String endTime = cursor.getString(endTimeIndex);

                    timer timer = new timer(id, duration, hours, minutes, seconds, sound, endTime);
                    timerHistory.add(timer);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        return timerHistory;
    }


    // Additional helper methods, if required (e.g., to get all timers, delete timers, etc.)
}
