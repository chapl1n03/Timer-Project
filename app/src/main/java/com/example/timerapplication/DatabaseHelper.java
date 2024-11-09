package com.example.timerapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_HOURS + " INTEGER, " +
                    COLUMN_MINUTES + " INTEGER, " +
                    COLUMN_SECONDS + " INTEGER, " +
                    COLUMN_DURATION + " TEXT, " +
                    COLUMN_SOUND + " TEXT)";  // Add sound column

    private static final String CREATE_SOUND_TABLE =
            "CREATE TABLE sound_settings (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "sound TEXT)";  // Separate table for sound settings

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_SOUND_TABLE); // Create sound settings table
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS sound_settings"); // Drop sound settings table if exists
        onCreate(db);
    }

    // Insert timer data into the database, including sound setting
    public void insertTimer(String duration, int hours, int minutes, int seconds, String sound) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DURATION, duration);
        values.put(COLUMN_HOURS, hours);
        values.put(COLUMN_MINUTES, minutes);
        values.put(COLUMN_SECONDS, seconds);
        values.put(COLUMN_SOUND, sound);  // Add sound setting here

        db.insert(TABLE_NAME, null, values);
        db.close();
    }


    public String getSound() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT sound FROM sound_settings LIMIT 1", null);

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String sound = cursor.getString(cursor.getColumnIndex("sound"));
            cursor.close();
            return sound;
        } else {
            return null; // No sound setting found
        }
    }
}

