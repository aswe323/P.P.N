package com.example.rems;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

/*
 *
 * editing and adding key words is done is the same activity,when editing the word and priority is taken from the SQLLite server
 *
 * */
public class EditKeyWords extends AppCompatActivity {

    private SeekBar seekbar;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_key_words);

        seekbar = findViewById(R.id.seekBar);
        textView = findViewById(R.id.textView19);
        seekbar.setProgress(0);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setText("priority: " + String.valueOf(progress) + "/10");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}
