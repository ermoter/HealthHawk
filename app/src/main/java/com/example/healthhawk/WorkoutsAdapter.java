package com.example.healthhawk;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class WorkoutsAdapter extends RecyclerView.Adapter<WorkoutsAdapter.ViewHolder>{
    private ArrayList<String[]> workoutsList;
    private Context context;

    public WorkoutsAdapter(Context ctx, ArrayList<String[]> workoutsList) {
        this.context = ctx;
        this.workoutsList = workoutsList;
    }

    @NonNull
    @Override
    public WorkoutsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.workout_items, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutsAdapter.ViewHolder holder, int position) {

        String[] fields = workoutsList.get(position);
        holder.workoutName.setText(fields[0]);
        // Set on click listener for button, which opens up timer and starts workout
        holder.workoutName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleClick(fields);
                //openTimer(fields);
                return;
            }
        });
    }

    @Override
    public int getItemCount() {
        return workoutsList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView workoutName;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            workoutName = view.findViewById(R.id.button_workout_items);

        }

    }

    private void handleClick(String[] fields) {
        Intent intent = new Intent(context, DisplayWorkoutInfo.class);
        intent.putExtra("xd1", fields);
        context.startActivity(intent);
    }
}