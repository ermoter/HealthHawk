package com.example.healthhawk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;



public class FoodAndCaloriesFunctionality extends AppCompatActivity {

    ArrayList<String> foodList = new ArrayList<String>();
    ArrayList<String> calorieList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listviews);
        TextView totalCalories  = findViewById(R.id.totalCaloriesTextView);
        TextView inputFood = findViewById(R.id.foodEditText);
        TextView inputCalories = findViewById(R.id.calorieEditText);

        ListView foodListView = findViewById(R.id.foodListView);
        //ListView calorieListView = findViewById(R.id.calorieListView);

        Button buttonAddItems = findViewById(R.id.addItemsButton);

        FoodAdapter foodAdapter = new FoodAdapter(this);
        //CalorieAdapter calorieAdapter = new CalorieAdapter(this);

        foodListView.setAdapter(foodAdapter);
        //calorieListView.setAdapter(calorieAdapter);

        buttonAddItems.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String text1 = inputFood.getText().toString();
                String text2 = inputCalories.getText().toString();
                foodList.add(text1);
                calorieList.add(text2);

                foodAdapter.notifyDataSetChanged();
                //calorieAdapter.notifyDataSetChanged();

                inputFood.setText("");
                inputCalories.setText("");
            }
        });
    }

    private class FoodAdapter extends ArrayAdapter<String> {
        public FoodAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            int s = foodList.size();
            return s;
        }

        public String getFoodItem(int position) {
            String item = foodList.get(position);
            return item;
        }

        public String getCalories(int position) {
            String item = calorieList.get(position);
            return item;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = FoodAndCaloriesFunctionality.this.getLayoutInflater();

            View result = null ;

            result = inflater.inflate(R.layout.food_row, null);
            TextView message = result.findViewById(R.id.foodText);
            message.setText(getFoodItem(position));

            TextView message1 = result.findViewById(R.id.calorieText);
            message1.setText(getCalories(position));
            // Log.i("CalorieText: ", message1.getText().toString());

            return result;
        }
    }

//    private class CalorieAdapter extends ArrayAdapter<String> {
//        public CalorieAdapter(Context ctx) {
//            super(ctx, 0);
//        }
//
//        public int getCount() {
//            int s = calorieList.size();
//            return s;
//        }
//
//        public String getItem(int position) {
//            String item = calorieList.get(position);
//            return item;
//        }
//
//        public View getView(int position, View convertView, ViewGroup parent) {
//            LayoutInflater inflater = MainActivity.this.getLayoutInflater();
//
//            View result = null ;
//
//            result = inflater.inflate(R.layout.food_row, null);
//            TextView message = result.findViewById(R.id.calorieText);
//            message.setText(getItem(position));
//
//            return result;
//        }
//    }
}