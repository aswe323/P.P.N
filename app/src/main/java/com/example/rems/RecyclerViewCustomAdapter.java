package com.example.rems;

import android.service.autofill.TextValueSanitizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import module.ActivityTask;
import module.ActivityTasksUsed;

public class RecyclerViewCustomAdapter extends RecyclerView.Adapter<RecyclerViewCustomAdapter.ViewHolder> {


    private ArrayList<ActivityTask> myActivityTasks;

    public RecyclerViewCustomAdapter(ArrayList<ActivityTask> activityTasks) {
        myActivityTasks = activityTasks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_display_layout, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTextView().setText(myActivityTasks.get(position).getContent());
        holder.setActivityTaskPointer(myActivityTasks.get(position));
    }

    @Override
    public int getItemCount() {
        return myActivityTasks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        private ActivityTask activityTaskPointer;

        public void setActivityTaskPointer(ActivityTask activityTaskPointer) {
            this.activityTaskPointer = activityTaskPointer;
        }

        public ViewHolder(View v) {
            super(v);


            // set event listeners here?
            v.findViewById(R.id.buttonDelete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityTasksUsed.removeActivityTask(activityTaskPointer);
                }
            });

            v.findViewById(R.id.buttonEdit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: @Lior implament edit function.
                }
            });


            textView = v.findViewById(R.id.contentDisplay);
        }


        public TextView getTextView() {
            return this.textView;
        }
    }


}
