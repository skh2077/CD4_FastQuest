package com.example.tt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

public class User_survey extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    SeekBar seekBar1;
    SeekBar seekBar2;
    SeekBar seekBar3;
    ImageButton backButton;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersurvey);


        // initiate  views
        seekBar1 = (SeekBar) findViewById(R.id.seekBar1);
        seekBar2 = (SeekBar) findViewById(R.id.seekBar2);
        seekBar3 = (SeekBar) findViewById(R.id.seekBar3);

        seekBar1.setOnSeekBarChangeListener(this);
        seekBar2.setOnSeekBarChangeListener(this);
        seekBar3.setOnSeekBarChangeListener(this);



        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        nextButton = findViewById(R.id.next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), pre_cat.class));
            }
        });
    }


    int value;

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        value = progress;


        switch (seekBar.getId()) {
            case R.id.seekBar1:

                break;

            case R.id.seekBar2:

                break;
            case R.id.seekBar3:

                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        switch (seekBar.getId()) {
            case R.id.seekBar1:

                break;

            case R.id.seekBar2:

                break;
            case R.id.seekBar3:

                break;
        }
        Toast.makeText(User_survey.this, "Seek bar progress is :" + value,
                Toast.LENGTH_SHORT).show();
    }


}
