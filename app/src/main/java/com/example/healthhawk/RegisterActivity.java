/*
Name:    RegisterActivity
Author:  Sebastian Koller
Version: 3.0 (12/3/2021)
Status:  COMPLETE
*/
package com.example.healthhawk;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import androidx.appcompat.widget.Toolbar;

import java.sql.SQLException;

public class RegisterActivity extends AppCompatActivity
{
    /* ------------ Class Members ------------ */
    Button cancelButton;
    Button registerButton;
    EditText nameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText rePasswordEditText;
    Toolbar toolbar;
    AppDatabase database;
    SharedPreferences prefs;
    SharedPreferences.Editor spEditor;

    /* ------------ Lifecycle Methods ------------ */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        assignElements();
        setListeners();
    }

    /* ------------ Setup ------------ */
    protected void assignElements()
    {
        cancelButton        = (Button) this.findViewById(R.id.cancelButton);
        registerButton      = (Button) this.findViewById(R.id.registerButton);
        nameEditText        = (EditText) this.findViewById(R.id.editTextTextPersonName);
        emailEditText       = (EditText) this.findViewById(R.id.editTextTextEmailAddress);
        passwordEditText    = (EditText) this.findViewById(R.id.editTextTextPassword);
        rePasswordEditText  = (EditText) this.findViewById(R.id.editTextTextRePassword);
        toolbar             = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create and open database
        database = new AppDatabase(this);
        try { database.open(); } catch (SQLException throwables) { throwables.printStackTrace(); }
        // Shared Preferences
        prefs = getSharedPreferences("SharedPrefs_Login", Context.MODE_PRIVATE);
        spEditor = prefs.edit();
    }

    protected void setListeners()
    {
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { finish(); }
        });
        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { register(); }
        });
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
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setMessage(
                        "Name:      RegisterActivity\n" +
                        "Version:   3.0\n" +
                        "Author:    Sebastian Koller\n\n" +
                        "Description:\n" +
                        "...");
                builder.setTitle("Activity Information");
                builder.setNeutralButton("Done", null);
                builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    /* ------------ Custom Functions ------------ */
    protected void register()
    {
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String rePassword = rePasswordEditText.getText().toString();

        // Invalid password
        String passwordValidity = isValidPassword(password);
        if (passwordValidity.compareTo("Valid") != 0)
        {
            Toast toast = Toast.makeText(getApplicationContext(),passwordValidity,Toast.LENGTH_SHORT);
            toast.show();
        }
        // Passwords don't match
        else if (password.equals(rePassword) == false)
        {
            Toast toast = Toast.makeText(getApplicationContext(),"Passwords do not match!",Toast.LENGTH_SHORT);
            toast.show();
        }
        // Email already in use
        else if (database.userExists(email))
        {
            Toast toast = Toast.makeText(getApplicationContext(),"Email already in use!",Toast.LENGTH_SHORT);
            toast.show();
        }
        // Email already in use
        else if (name.equals(""))
        {
            Toast toast = Toast.makeText(getApplicationContext(),"Please enter your name!",Toast.LENGTH_SHORT);
            toast.show();
        }
        else
        {
            boolean inserted = database.insertUser(email, password, name);

            if (inserted)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setMessage("Registration Complete!");
                builder.setTitle("Success");
                builder.setNeutralButton("Return to login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        spEditor.putString("DefaultEmail",emailEditText.getText().toString());
                        spEditor.commit();
                        finish();
                    }
                });
                builder.show();
            }
            else
            {
                Toast toast = Toast.makeText(getApplicationContext(),"Error occurred when inserting user!",Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    private String isValidPassword(String password)
    {
        if (password == null)           return "You must enter a password!";
        else if (password.length() < 5) return "Password must be at least 5 characters long!";
        else                            return "Valid";
    }
}
