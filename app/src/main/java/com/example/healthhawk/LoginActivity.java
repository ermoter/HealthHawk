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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

        // Reopen database (untested)
        //try { database.open(); } catch (SQLException throwables) { throwables.printStackTrace(); }

        // Updates the emailTextEdit if the user just registered
        prefs = getSharedPreferences("SharedPrefs_Login", Context.MODE_PRIVATE);
        spEditor = prefs.edit();
        emailEditText.setText(prefs.getString("DefaultEmail", ""));
    }

    protected void onPause() {
        super.onPause();

        // Close database (untested)
        // database.close();

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
            Toast toast = Toast.makeText(getApplicationContext(),"Successful Login!",Toast.LENGTH_SHORT);
            toast.show();
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
