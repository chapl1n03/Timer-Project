package com.example.timerapplication;

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

    // SQL query to create the table
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_HOURS + " INTEGER, " +
                    COLUMN_MINUTES + " INTEGER, " +
                    COLUMN_SECONDS + " INTEGER, " +
                    COLUMN_DURATION + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Insert timer data into the database
    public void insertTimer(String duration, int hours, int minutes, int seconds) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DURATION, duration);
        values.put(COLUMN_HOURS, hours);
        values.put(COLUMN_MINUTES, minutes);
        values.put(COLUMN_SECONDS, seconds);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Retrieve saved sound setting from the database
    public Cursor getSound() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM sound_settings LIMIT 1", null);
    }
}
