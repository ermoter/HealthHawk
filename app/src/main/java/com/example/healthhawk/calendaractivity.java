package com.example.healthhawk;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentValues;

import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

public class calendaractivity extends AppCompatActivity {

    private EditText AddEvent;
    private TextView UpcomingEvents;
    private CalendarView calendarView;
    private String selectedDate;
    private SQLiteDatabase sqLiteDatabase;
    private Button AddEventbtn;
    private  AppDatabase AppDB = new AppDatabase(this);

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
                //InsertDatabase(view);
                AppDB.InsertDatabase(view,selectedDate,AddEvent,UpcomingEvents,AddEventbtn);
            }
        });


        delebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Deletelment(view);
            }
        });

        delebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Deletelment(view);
                AppDB.Deletelment(view,selectedDate,AddEvent,UpcomingEvents,AddEventbtn);

            }

        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = Integer.toString(year) + Integer.toString(month) + Integer.toString(dayOfMonth);
                //  public void ReadDatabase(View view, String selectedDate, EditText AddEvent, TextView UpcomingEvents, Button AddEventbtn){
                AppDB.ReadDatabase(view,selectedDate,AddEvent,UpcomingEvents,AddEventbtn);

                //ReadDatabase(view);
            }
        });
        try{

            //dbHandler = new mySQLiteDBHandler(this, "CalendarDatabase", null,1);
            //sqLiteDatabase = dbHandler.getWritableDatabase();
            //sqLiteDatabase.execSQL("CREATE TABLE EventCalendar(Date TEXT, Event TEXT)");
        }
        catch (Exception e){
            e.printStackTrace();
        }







    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.help_menu, menu);
        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.help_menu_item:
                AlertDialog.Builder builder = new AlertDialog.Builder(calendaractivity.this);
                builder.setMessage(
                        "Name:      FoodAndCaloriesActivity\n" +
                                "Version:   3.0\n" +
                                "Author:    Maynard Amari\n\n" +
                                "Description:\n" +
                                "Add your meals by entering the name of the food and the calories it contained.");
                builder.setTitle("Activity Information");
                builder.setNeutralButton("Done", null);
                builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void InsertDatabase(View view){

        String query = "Select Event from EventCalendar where Date = " + selectedDate;
        try{
            sqLiteDatabase.execSQL("delete from "+ "EventCalendar" + " WHERE " + "Date" + " ="+selectedDate);

            ContentValues contentValues = new ContentValues();
            contentValues.put("Date",selectedDate);
            contentValues.put("Event", AddEvent.getText().toString());

            //sqLiteDatabase.insert("EventCalendar", null, contentValues);
            AppDB.ReadDatabase(view,selectedDate,AddEvent,UpcomingEvents,AddEventbtn);
            //ReadDatabase(view);
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