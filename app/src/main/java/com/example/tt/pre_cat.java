package com.example.tt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.Button;

public class pre_cat extends AppCompatActivity {
    ToggleButton pre_art;
    ToggleButton pre_book;
    ToggleButton pre_camera;
    ToggleButton pre_dance;
    ToggleButton pre_food;
    ToggleButton pre_game;
    ToggleButton pre_language;
    ToggleButton pre_meet;
    ToggleButton pre_movie;
    ToggleButton pre_music;
    ToggleButton pre_sports;
    ToggleButton pre_travel;
    ToggleButton pre_volunteer;
    Button saveButton;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_cat);

        pre_art = findViewById(R.id.pre_Art);
        pre_book = findViewById(R.id.pre_Book);
        pre_camera = findViewById(R.id.pre_Camera);
        pre_dance = findViewById(R.id.pre_Dance);
        pre_food = findViewById(R.id.pre_Food);
        pre_game = findViewById(R.id.pre_Game);
        pre_language =findViewById(R.id.pre_Language);
        pre_meet = findViewById(R.id.pre_Meet);
        pre_movie = findViewById(R.id.pre_Movie);
        pre_music = findViewById(R.id.pre_Music);
        pre_sports = findViewById(R.id.pre_Sports);
        pre_travel = findViewById(R.id.pre_Travel);
        pre_volunteer = findViewById(R.id.pre_Volunteer);
        saveButton = findViewById(R.id.saveButton);

        pre_art.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pre_art.isChecked()){
                    pre_art.setAlpha(1f);
                }
                else{
                    pre_art.setAlpha(0.1f);
                }
            }
        });
        pre_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pre_book.isChecked()){
                    pre_book.setAlpha(1f);
                }
                else{
                    pre_book.setAlpha(0.1f);
                }
            }
        });
        pre_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pre_camera.isChecked()){
                    pre_camera.setAlpha(1f);
                }
                else{
                    pre_camera.setAlpha(0.1f);
                }
            }
        });
        pre_dance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pre_dance.isChecked()){
                    pre_dance.setAlpha(1f);
                }
                else{
                    pre_dance.setAlpha(0.1f);
                }
            }
        });
        pre_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pre_food.isChecked()){
                    pre_food.setAlpha(1f);
                }
                else{
                    pre_food.setAlpha(0.1f);
                }
            }
        });
        pre_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pre_game.isChecked()){
                    pre_game.setAlpha(1f);
                }
                else{
                    pre_game.setAlpha(0.1f);
                }
            }
        });
        pre_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pre_language.isChecked()){
                    pre_language.setAlpha(1f);
                }
                else{
                    pre_language.setAlpha(0.1f);
                }
            }
        });
        pre_meet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pre_meet.isChecked()){
                    pre_meet.setAlpha(1f);
                }
                else{
                    pre_meet.setAlpha(0.1f);
                }
            }
        });
        pre_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pre_movie.isChecked()){
                    pre_movie.setAlpha(1f);
                }
                else{
                    pre_movie.setAlpha(0.1f);
                }
            }
        });
        pre_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pre_music.isChecked()){
                    pre_music.setAlpha(1f);
                }
                else{
                    pre_music.setAlpha(0.1f);
                }
            }
        });

        pre_sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pre_sports.isChecked()){
                    pre_sports.setAlpha(1f);
                }
                else{
                    pre_sports.setAlpha(0.1f);
                }
            }
        });
        pre_travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pre_travel.isChecked()){
                    pre_travel.setAlpha(1f);
                }
                else{
                    pre_travel.setAlpha(0.1f);
                }
            }
        });
        pre_volunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pre_volunteer.isChecked()){
                    pre_volunteer.setAlpha(1f);
                }
                else{
                    pre_volunteer.setAlpha(0.1f);
                }
            }
        });
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),profile.class));
            }
        });


    }
    public void OnClickHandler(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("수정사항").setMessage("저장하시겠습니까?");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),profile.class));
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),profile.class));
            }
        });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
