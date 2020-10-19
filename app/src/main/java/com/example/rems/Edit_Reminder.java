package com.example.rems;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
/*
 *
 * edit reminder used for adding a new reminder,when the edit is choosen the info will be taken from the SQLLite DB, otherwise it will be empty of info
 *
 *add reminder and sub reminder are made in the same activity,only differences is that in
 * sub reminder the "add sub reminder" button won't be visible.
 * every reminder that will have a sub remainder will be a collection,others will be and object.
 *
 */


public class Edit_Reminder extends AppCompatActivity {

    /*private Context thisconthext=this;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__reminder);
        //time picker dialog https://www.youtube.com/watch?v=H4gtHTkajAE
        final TextView settimetext=findViewById(R.id.SetTimeTextView);
        final Calendar calendar=Calendar.getInstance();
        final int hour=calendar.get(calendar.HOUR_OF_DAY);
        final int minute=calendar.get(Calendar.MINUTE);
        settimetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(thisconthext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        settimetext.setText(hourOfDay+":"+minute);
                    }
                },hour,minute,android.text.format.DateFormat.is24HourFormat(thisconthext));
                timePickerDialog.show();
            }
        });

        //date picker dialog https://www.youtube.com/watch?v=hwe1abDO2Aghttps://www.youtube.com/watch?v=hwe1abDO2Ag
        final TextView setdatetext=findViewById(R.id.SetDateTextView);

        setdatetext.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(thisconthext, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable((new ColorDrawable(Color.TRANSPARENT)));
                dialog.show();

            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month+=1;
                Log.d("","onDateSet: dd/mm/yyyy:"+dayOfMonth+"/"+month+"/"+year);
                String date = dayOfMonth+"/"+month+"/"+year;
                setdatetext.setText(date);
            }
        };

    }*/
}
