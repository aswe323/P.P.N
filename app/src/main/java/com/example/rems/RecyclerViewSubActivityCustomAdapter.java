package com.example.rems;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import module.ActivityTask;
import module.SubActivity;

public class RecyclerViewSubActivityCustomAdapter extends RecyclerView.Adapter<RecyclerViewSubActivityCustomAdapter.ViewHolder> {
//TODO: add to book

    ArrayList<SubActivity> mySubActivities;

    public RecyclerViewSubActivityCustomAdapter(ArrayList<SubActivity> SubActivity) {
        mySubActivities = SubActivity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textSubActivitContent);
        }

        public TextView getTextView() {
            return textView;
        }

    }

    @NonNull
    @Override
    public RecyclerViewSubActivityCustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subactivity_display_layout, parent, false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewSubActivityCustomAdapter.ViewHolder holder, int position) {
        //assign data
        holder.getTextView().setText(mySubActivities.get(position).getContent());
    }


    @Override
    public int getItemCount() {
        return mySubActivities.size();
    }
}
