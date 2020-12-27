package com.example.rems;



/*
TODO:
    1. separte the click events from the viewholder
    2. attach events with dedicated event attachments methods from the RecyclerView class
    3. implament deletion of an item from the adapter
        3.1 make sure to notifyItemRemoved and notifyDataSetChanged
 */


import android.app.FragmentManager;
import android.app.TabActivity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.service.autofill.TextValueSanitizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import module.ActivityTask;
import module.ActivityTasksUsed;

public class RecyclerViewCustomAdapter extends RecyclerView.Adapter<RecyclerViewCustomAdapter.ViewHolder> implements View.OnClickListener {//used for the recyclerView to get and show the data


    public static int removedAt;
    private ArrayList<ActivityTask> myActivityTasks;


    /**
     * attempts to remove an item from the data ArrayList and then update the recycler
     *
     * @param activityTask
     * @return true on success false on failure.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean removeTaskFromList(ActivityTask activityTask) {
        int positionRemoved = myActivityTasks.indexOf(activityTask);
        if (ActivityTasksUsed.removeActivityTask(activityTask) && myActivityTasks.remove(activityTask)) {
            this.notifyItemRemoved(positionRemoved);
            this.notifyDataSetChanged();
            return true;
        }
        return false;
    }

    ;


    public RecyclerViewCustomAdapter(ArrayList<ActivityTask> activityTasks) {
        myActivityTasks = activityTasks;
    }//constructed with the data we want the RecyclerView to use.

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;//a pointer to the UI element in the xml file.
        private final ImageView buttonDelete;
        private ActivityTask activityTaskPointer;// a pointer to the data point this viewHolder is referencing. required for the event listeners to know what to do with each respected button.

        @RequiresApi(api = Build.VERSION_CODES.O)
        public ViewHolder(View v) { // the constructor for the class.
            super(v);
            //region eventListeners
            v.findViewById(R.id.buttonEdit).setOnClickListener(v12 -> {
                if (!edit_reminder_fragment.isActive()) {
                    FragmentTransaction ft = ((FragmentActivity) v12.getContext()).getSupportFragmentManager().beginTransaction();
                    edit_reminder_fragment erf = new edit_reminder_fragment();//creating the fragment to put instead
                    edit_reminder_fragment.setReturnToID(R.id.fragment_reminders_colletion);
                    ft.replace(R.id.fragment_reminders_colletion, erf).commit();
                    ((FragmentActivity) v12.getContext()).getSupportFragmentManager().executePendingTransactions();
                    edit_reminder_fragment.editingReminder(activityTaskPointer);
                }

            });
            textView = v.findViewById(R.id.contentDisplay);//setting the textView member to the textView UI element in the xml.
            buttonDelete = v.findViewById(R.id.buttonDelete);
            //endregion
        }

        public void setActivityTaskPointer(ActivityTask activityTaskPointer) {
            this.activityTaskPointer = activityTaskPointer;
        } //NOTICE this is called in @onBindViewHolder.

        public ActivityTask getActivityTaskPointer() {
            return this.activityTaskPointer;
        }//NOTICE this is called in @onBindViewHolder.

        public ImageView getButtonDelete() {
            return buttonDelete;
        }

        public TextView getTextView() {
            return this.textView;
        } //NOTICE this is called in @onBindViewHolder
    }//inner static class that defines the ViewHolder that will contain the data.


    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_display_layout, parent, false);

        return new ViewHolder(v);
    }// this is called when a ViewHolder is created. if you want to understand inflaters/inflating better go to the documentation. ctrl+q in android studio opens the documentation for the current item under the courser.

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTextView().setText(myActivityTasks.get(position).getContent());// setting the text to the content of the ActivityTask
        holder.setActivityTaskPointer(myActivityTasks.get(position));// setting the pointer to the ActivityTask from the ArrayList.
        holder.getButtonDelete().setOnClickListener(v -> {
            removeTaskFromList(holder.activityTaskPointer);
        });


    } // whenever a view holder is scrolled off screen, it is reused with another data point (ActivityTask in this case), so we need to BIND the ViewHolder to the data


    @Override
    public int getItemCount() {
        return myActivityTasks.size();
    }


}
