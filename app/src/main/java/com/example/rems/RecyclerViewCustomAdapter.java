package com.example.rems;

import android.service.autofill.TextValueSanitizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import module.ActivityTask;
import module.ActivityTasksUsed;

public class RecyclerViewCustomAdapter extends RecyclerView.Adapter<RecyclerViewCustomAdapter.ViewHolder> {


    private ArrayList<ActivityTask> myActivityTasks;


    public RecyclerViewCustomAdapter(ArrayList<ActivityTask> activityTasks) {
        myActivityTasks = activityTasks;
    }//constructed with the data we want the RecyclerView to use.


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;//a pointer to the UI element in the xml file.
        private ActivityTask activityTaskPointer;// a pointer to the data point this viewHolder is referencing. required for the event listeners to know what to do with each respected button.


        public ViewHolder(View v) { // the constructor for the class.
            super(v);
            //region eventListeners
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
            //endregion
            textView = v.findViewById(R.id.contentDisplay);//setting the textView member to the textView UI element in the xml.
        }

        public void setActivityTaskPointer(ActivityTask activityTaskPointer) {
            this.activityTaskPointer = activityTaskPointer;
        } //NOTICE this is called in @onBindViewHolder.

        public TextView getTextView() {
            return this.textView;
        } //NOTICE this is called in @onBindViewHolder
    }//inner static class that defines the ViewHolder that will contain the data.


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_display_layout, parent, false);

        return new ViewHolder(v);
    }// this is called when a ViewHolder is created. if you want to understand inflaters/inflating better go to the documentation. ctrl+q in android studio opens the documentation for the current item under the courser.

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTextView().setText(myActivityTasks.get(position).getContent());// setting the text to the content of the ActivityTask
        holder.setActivityTaskPointer(myActivityTasks.get(position));// setting the pointer to the ActivityTask from the ArrayList.
    } // whenever a view holder is scrolled off screen, it is reused with another data point (ActivityTask in this case), so we need to BIND the ViewHolder to the data


    @Override
    public int getItemCount() {
        return myActivityTasks.size();
    }


}
