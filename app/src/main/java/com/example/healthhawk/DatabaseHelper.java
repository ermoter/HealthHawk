package com.example.healthhawk;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Workouts.db";
    public static final int VERSION_NUM = 2;
    public static final String TABLE_NAME = "Workouts";
    public static final String KEY_WORKOUT_ID = "ID";
    public static final String KEY_WORKOUT_NAME = "WorkoutName";
    public static final String KEY_SETS = "Sets";
    public static final String KEY_EXERCISE_TIME = "Time_Per_Exercise";
    public static final String KEY_REST_TIME = "Rest_Between_Exercises";
    public static final String KEY_RECOVERY_TIME = "Recovery_Between_Sets";
    public static final String KEY_EXERCISES = "Exercises";
    public static final String CLASS_NAME = "DatabaseHelper";

    public DatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(CLASS_NAME, "Calling onCreate");
        // Note that column names/table names must not have any spaces
        String new_table =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        KEY_WORKOUT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_WORKOUT_NAME + " TEXT, " +
                        KEY_SETS + " INTEGER," + KEY_EXERCISES + " TEXT, " + KEY_EXERCISE_TIME + " DECIMAL, " +
                        KEY_REST_TIME + " DECIMAL, " + KEY_RECOVERY_TIME + " DECIMAL)";


        sqLiteDatabase.execSQL(new_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i(CLASS_NAME, "Calling onUpgrade, oldVersion=" + (VERSION_NUM-1) + " newVersion=" + (VERSION_NUM));
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
