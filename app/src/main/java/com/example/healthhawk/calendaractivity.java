package com.example.healthhawk;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentValues;

import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

public class calendaractivity extends AppCompatActivity {
    private mySQLiteDBHandler dbHandler;
    private EditText AddEvent;
    private TextView UpcomingEvents;
    private CalendarView calendarView;
    private String selectedDate;
    private SQLiteDatabase sqLiteDatabase;
    private Button AddEventbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.calanderactivity);

        AddEvent = findViewById(R.id.Addevent);
        calendarView = findViewById(R.id.calendarView);
        AddEventbtn = findViewById(R.id.NewEntry);
        Button delebutton = findViewById(R.id.DeleteEntry);
        UpcomingEvents = findViewById(R.id.UpcomingEvents);

        calendarView.setDate(System.currentTimeMillis(),false,true);

        AddEventbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertDatabase(view);
            }
        });


        delebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Deletelment(view);
            }
        });


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = Integer.toString(year) + Integer.toString(month) + Integer.toString(dayOfMonth);
                ReadDatabase(view);
            }
        });
        try{

            dbHandler = new mySQLiteDBHandler(this, "CalendarDatabase", null,1);
            sqLiteDatabase = dbHandler.getWritableDatabase();
            sqLiteDatabase.execSQL("CREATE TABLE EventCalendar(Date TEXT, Event TEXT)");
        }
        catch (Exception e){
            e.printStackTrace();
        }







    }
    public void InsertDatabase(View view){

        String query = "Select Event from EventCalendar where Date = " + selectedDate;
        try{
            sqLiteDatabase.execSQL("delete from "+ "EventCalendar" + " WHERE " + "Date" + " ="+selectedDate);

            ContentValues contentValues = new ContentValues();
            contentValues.put("Date",selectedDate);
            contentValues.put("Event", AddEvent.getText().toString());

            sqLiteDatabase.insert("EventCalendar", null, contentValues);
            ReadDatabase(view);
        }
        catch (Exception e){
            ContentValues contentValues = new ContentValues();
            contentValues.put("Date",selectedDate);
            contentValues.put("Event", AddEvent.getText().toString());

            sqLiteDatabase.insert("EventCalendar", null, contentValues);
            ReadDatabase(view);
        }





    }


    @Override
    public  void onSaveInstanceState(Bundle outstate){
        outstate.putString("AddEvent",AddEvent.getText().toString());
        outstate.putString("UpcomingEvents",UpcomingEvents.getText().toString());

        super.onSaveInstanceState(outstate);

    }

    public  void onRestoreInstanceState(Bundle outstate){
        super.onRestoreInstanceState(outstate);

        AddEvent.setText(outstate.get("AddEvent").toString());
        UpcomingEvents.setText(outstate.get("UpcomingEvents").toString());


        super.onSaveInstanceState(outstate);

    }
    public void ReadDatabase(View view){
        String query = "Select Event from EventCalendar where Date = " + selectedDate;
        try{
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            cursor.moveToFirst();
            String Text = selectedDate.substring(4,5)+"/"+selectedDate.substring(6,7)+"/"+selectedDate.substring(0,4);
            UpcomingEvents.setText("Upcoming Event: "+Text +"-"+cursor.getString(0));
            AddEvent.setText("");
            AddEventbtn.setText(R.string.addEvent);
        }
        catch (Exception e){
            e.printStackTrace();
            UpcomingEvents.setText(R.string.UPevents);
            AddEvent.setText("");
            AddEventbtn.setText(R.string.addEvent);
        }
    }

    public void Deletelment(View view){

        try{

            sqLiteDatabase.execSQL("delete from "+ "EventCalendar" + " WHERE " + "Date" + " ="+selectedDate);
            // sqLiteDatabase.execSQL("CREATE TABLE EventCalendar(Date TEXT, Event TEXT)");


            UpcomingEvents.setText(R.string.UPevents);
            AddEvent.setText("");
            ReadDatabase(view);
        }
        catch (Exception e){
            e.printStackTrace();
            UpcomingEvents.setText(R.string.UPevents);
            AddEvent.setText("");
        }
    }


}