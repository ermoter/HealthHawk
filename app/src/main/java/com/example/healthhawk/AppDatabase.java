/*
Name:   DatabaseHelper
Author: Sebastian Koller
Contributors:
    Amari Maynard
    - dates table methods
    Eric Tran
    - stats table methods
    Mohammad Baig
    - foods table methods
    Ridwan Mursal
    - workout table methods
    Sebastian Koller
    - class members
    - class basic functions
    - user table methods

Version: 3.0 (12/3/2021)
Status: IN PROGRESS
 */
package com.example.healthhawk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;

public class AppDatabase
{
    /* ------------ Class Members ------------ */
    SQLiteDatabase db;
    SQLiteDatabase dbRead;
    DatabaseHelper dbh;

    /* ------------ Class Basic Functions ------------ */
    public AppDatabase(Context context) { dbh = new DatabaseHelper(context); }

    public void open() throws SQLException {
        dbRead = dbh.getReadableDatabase();
        db = dbh.getWritableDatabase();
    }

    public void close() { dbh.close(); }

    /* ------------ ALL CUSTOM DATABASE METHODS ------------ */

    /* ------------ Users Table Methods ------------ */
    public boolean insertUser(String email, String password, String name)
    {
        ContentValues cv = new ContentValues();
        cv.put(dbh.USERS_COLUMN_EMAIL,    email);
        cv.put(dbh.USERS_COLUMN_PASSWORD, password);
        cv.put(dbh.USERS_COLUMN_NAME,     name);

        long result = db.insert(dbh.USERS_TABLE_NAME,null,cv);

        if(result==-1) {return false;} else {return true;}
    }

    public boolean deleteUser(String email)
    {
        String selection = dbh.USERS_COLUMN_EMAIL + " = ?";
        String[] selectionArgs = { "" + email};

        int result = db.delete(dbh.USERS_TABLE_NAME, selection, selectionArgs );

        if(result==-1) {return false;} else {return true;}
    }

    public boolean userExists(String email)
    {
        Cursor c = db.rawQuery(
                "SELECT * FROM "+ dbh.USERS_TABLE_NAME+
                        " WHERE "+ dbh.USERS_COLUMN_EMAIL+" = ?",new String[] {email});

        if (c.getCount()>0) {return true;} else {return false;}
    }

    public String getName(String email)
    {
        Cursor c = db.rawQuery(
                "SELECT * FROM "+ dbh.USERS_TABLE_NAME+
                        " WHERE "+ dbh.USERS_COLUMN_EMAIL+" = ?",new String[] {email});

        if (c.moveToFirst())
        {
            return c.getString(c.getColumnIndex(dbh.USERS_COLUMN_NAME));
        }
        else {return "Unamed Human!";}
    }

    public boolean verifyCredentials(String email, String password)
    {
        Cursor c = db.rawQuery("SELECT * FROM "+
                dbh.USERS_TABLE_NAME+" WHERE "+
                dbh.USERS_COLUMN_EMAIL+" = ? and "+
                dbh.USERS_COLUMN_PASSWORD+" = ?",new String[] {email,password});

        if (c.getCount()>0) {return true;} else {return false;}
    }

    /* ------------ Foods Table Methods ------------ */
    //insertFood
    public void insertFood(String[] fields) {
        ContentValues values = new ContentValues();

        // String userName = fields[1];
        String foodName = fields[0];
        String calories = fields[1];

        values = new ContentValues();
        //values.put(dbh.FOODS_COLUMN_USER, userName);
        values.put(dbh.FOODS_COLUMN_FOOD_NAME, foodName);
        values.put(dbh.FOODS_COLUMN_CALORIES, calories);
        db.insert(dbh.FOODS_TABLE_NAME, null, values);

    }
    //deleteFood
    public void deleteFood(String foodName) {//, String calories) {

        String selection = dbh.FOODS_COLUMN_FOOD_NAME + " = ?";
        String[] selectionArgs = { "" + foodName};

        db.delete(dbh.FOODS_TABLE_NAME, selection, selectionArgs);

    }
    //getAllFood
    public void getAllFood(ArrayList<String> foodList, ArrayList<String> calorieList) {

//        String[] foodTable;
//        int i = 0;
          Cursor c = dbRead.query(dbh.FOODS_TABLE_NAME, null, null, null, null, null, null);

          c.moveToFirst();
          while (c.moveToNext()) {
              foodList.add(c.getString(c.getColumnIndexOrThrow(dbh.FOODS_COLUMN_FOOD_NAME)));
              calorieList.add(c.getString(c.getColumnIndexOrThrow(dbh.FOODS_COLUMN_CALORIES)));
          }
          c.close();
    }

    public void logFood() {
        Cursor c = dbRead.query(dbh.FOODS_TABLE_NAME, null, null, null, null, null, null);

        c.moveToFirst();
        Log.i("Food Name: ", c.getString(c.getColumnIndexOrThrow(dbh.FOODS_COLUMN_FOOD_NAME)));
        Log.i("Food Cals: ", c.getString(c.getColumnIndexOrThrow(dbh.FOODS_COLUMN_CALORIES)));
        while (c.moveToNext()) {
            Log.i("Food Name: ", c.getString(c.getColumnIndexOrThrow(dbh.FOODS_COLUMN_FOOD_NAME)));
            Log.i("Food Cals: ", c.getString(c.getColumnIndexOrThrow(dbh.FOODS_COLUMN_CALORIES)));
        }
        c.close();
    }

    /* ------------ Stats Table Methods ------------ */

    public String getStats(){



        return "0";
    }

    /* ------------ Goals Table Methods ------------ */


    /* ------------ Dates Table Methods ------------ */

    /* ------------ Workouts Table Methods ------------ */
    public void addWorkout(String[] fields) {
        ContentValues values = new ContentValues();
        // Get appropriate fields from fields array
        String workoutName = fields[0];
        String sets = fields[1];
        String exercises = fields[2];
        String exerciseTime = fields[3];
        String restTime = fields[4];
        String recoveryTime = fields[5];

        // Add these values to the database
        values = new ContentValues();
        values.put(dbh.WORKOUTS_COLUMN_WORKOUT_NAME, workoutName);
        values.put(dbh.WORKOUTS_COLUMN_SETS, sets);
        values.put(dbh.WORKOUTS_COLUMN_EXERCISE_LIST, exercises);
        values.put(dbh.WORKOUTS_COLUMN_EXERCISE_TIME, exerciseTime);
        values.put(dbh.WORKOUTS_COLUMN_REST_TIME, restTime);
        values.put(dbh.WORKOUTS_COLUMN_RECOVERY_TIME, recoveryTime);
        long newRowId = db.insert(dbh.WORKOUTS_TABLE_NAME, null, values);
    }

    // helper function to delete item from recyclerview and database
    public void deleteWorkoutsDatabaseEntry(String name, int position) {
        String selection = dbh.WORKOUTS_COLUMN_WORKOUT_NAME + " = ?";
        String[] selectionArgs = { "" + name };

        // delete record in database containing selected workout name
        int deletedRows = dbRead.delete(dbh.WORKOUTS_COLUMN_WORKOUT_NAME, selection, selectionArgs );
    }

    public void addWorkoutsFromDatabase(ArrayList<String[]> adapterList) {
        Cursor cursor  = dbRead.query(
                dbh.WORKOUTS_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        String nameCol = dbh.WORKOUTS_COLUMN_WORKOUT_NAME;
        String setsCol = dbh.WORKOUTS_COLUMN_SETS;
        String exercisesCol = dbh.WORKOUTS_COLUMN_EXERCISE_LIST;
        String exerciseTimeCol = dbh.WORKOUTS_COLUMN_EXERCISE_TIME;
        String restCol = dbh.WORKOUTS_COLUMN_REST_TIME;
        String recoveryCol = dbh.WORKOUTS_COLUMN_RECOVERY_TIME;
        // Loop through each element in cursor, and add to list
        while(cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(nameCol));
            String sets = cursor.getString(cursor.getColumnIndexOrThrow(setsCol));
            String exercises = cursor.getString(cursor.getColumnIndexOrThrow(exercisesCol));
            String exerciseTime = cursor.getString(cursor.getColumnIndexOrThrow(exerciseTimeCol));
            String rest = cursor.getString(cursor.getColumnIndexOrThrow(restCol));
            String recovery = cursor.getString(cursor.getColumnIndexOrThrow(recoveryCol));

            String[] record =  {id, sets, exercises, exerciseTime, rest, recovery};
            adapterList.add(record);
        }
        cursor.close();
    }
}
