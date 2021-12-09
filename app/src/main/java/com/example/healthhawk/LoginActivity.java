/*
Name:   LoginActivity
Author: Sebastian Koller
Version: 3.0 (12/3/2021)
Status: COMPLETE
 */
package com.example.healthhawk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity
{
    /* ------------ Class Members ------------ */
    Button loginButton;
    Button registerButton;
    EditText emailEditText;
    EditText passwordEditText;
    AppDatabase database;
    SharedPreferences prefs;
    SharedPreferences.Editor spEditor;

    /* ------------ Lifecycle Methods ------------ */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("LoginActivty", "In OnCreate");
        setContentView(R.layout.login_layout);
        assignElements();
        setListeners();
        try { database.open(); }
        catch (SQLException throwables) { throwables.printStackTrace(); }
    }

    protected void onResume() {
        super.onResume();
        passwordEditText.setText("");
        // Reopen database (untested)
        try { database.open(); } catch (SQLException throwables) { throwables.printStackTrace(); }

        // Updates the emailTextEdit if the user just registered
        prefs = getSharedPreferences("SharedPrefs_Login", Context.MODE_PRIVATE);
        spEditor = prefs.edit();
        emailEditText.setText(prefs.getString("DefaultEmail", ""));
    }

    protected void onPause() {
        super.onPause();
        database.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage(
                        "Name:      LoginActivity\n" +
                        "Version:   3.0\n" +
                        "Author:    Sebastian Koller\n\n" +
                        "Description:\n" +
                        "Use the two TextEdits to enter your email and password. Once credentials are inputted, " +
                                "clicking the login button will validate your credentials with the database, if valid you will be sent to the HomeActivity.\n" +
                        "Use register button if you wish to open the RegisterActivity");
                builder.setTitle("Activity Information");
                builder.setNeutralButton("Done", null);
                builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    /* ------------ Setup ------------ */
    protected void assignElements()
    {

        loginButton = (Button) this.findViewById(R.id.cancelButton);
        registerButton = (Button) this.findViewById(R.id.registerButton);
        emailEditText = (EditText) this.findViewById(R.id.editTextTextEmailAddress);
        passwordEditText = (EditText) this.findViewById(R.id.editTextTextPassword);

        // Database
        database = new AppDatabase(this);

        // Shared Preferences
        prefs = getSharedPreferences("SharedPrefs_Login", Context.MODE_PRIVATE);
        spEditor = prefs.edit();
        emailEditText.setText(prefs.getString("DefaultEmail",""));
    }

    protected void setListeners()
    {
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { login(); }
        });
        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { register(); }
        });
    }

    /* ------------ Custom Functions ------------ */
    protected void login()
    {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (database.verifyCredentials(email,password))
        {
            spEditor.putString("DefaultEmail",emailEditText.getText().toString());
            spEditor.commit();
            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
            intent.putExtra("USER_ID",email);
            startActivity(intent);
//            Toast toast = Toast.makeText(getApplicationContext(),"Successful Login!",Toast.LENGTH_SHORT);
//            toast.show();
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(),"Invalid Username/Password",Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    protected void register()
    {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

}
