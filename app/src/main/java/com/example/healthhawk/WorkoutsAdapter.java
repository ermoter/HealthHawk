package com.example.healthhawk;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class WorkoutsAdapter extends RecyclerView.Adapter<WorkoutsAdapter.ViewHolder>{
    private ArrayList<String[]> workoutsList;
    private Context context;
    private Workouts workouts;

    public WorkoutsAdapter(Context ctx, ArrayList<String[]> workoutsList, Workouts workouts) {
        this.context = ctx;
        this.workoutsList = workoutsList;
        this.workouts = workouts;
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
                return;
            }
        });

        holder.workoutName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you would like to delete this workout?")
                        .setTitle("Delete Workout");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(context, Workouts.class);
                        workouts.deleteDatabaseEntry(fields[0], holder.getAdapterPosition());

                    }
                });
                builder.setNegativeButton("No, Take Me Back!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
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
        //intent.putExtra("position", position);
        context.startActivity(intent);
    }
}