/*
Name:   DatabaseHelper
Author: Sebastian Koller
Contributors:
    Amari Maynard
    - dates table methods
    Eric Tran
    - goals table methods
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

import java.sql.SQLException;

public class AppDatabase
{
    /* ------------ Class Members ------------ */
    SQLiteDatabase db;
    DatabaseHelper dbh;

    /* ------------ Class Basic Functions ------------ */
    public AppDatabase(Context context) { dbh = new DatabaseHelper(context); }

    public void open() throws SQLException { db = dbh.getWritableDatabase(); }

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
        else {return "Unamed Person!";}
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
    //deleteFood
    //getAllFood
    /* ------------ Goals Table Methods ------------ */

    /* ------------ Dates Table Methods ------------ */

    /* ------------ Workouts Table Methods ------------ */
}
