package com.example.tt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.Button;
import com.example.tt.data.UserData;
import java.util.ArrayList;


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
    static ArrayList<Boolean> BoolList =  new ArrayList<Boolean>();



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

        UserData userdata = new UserData();
        BoolList = userdata.getBooList();



        if(BoolList.get(0)==false) {
            pre_art.setChecked(false);
            pre_art.setAlpha(0.1f);
        }
        if(BoolList.get(1)==false) {
            pre_book.setChecked(false);
            pre_book.setAlpha(0.1f);
        }
        if(BoolList.get(2)==false) {
            pre_camera.setChecked(false);
            pre_camera.setAlpha(0.1f);
        }
        if(BoolList.get(3)==false) {
            pre_dance.setChecked(false);
            pre_dance.setAlpha(0.1f);
        }
        if(BoolList.get(4)==false) {
            pre_food.setChecked(false);
            pre_food.setAlpha(0.1f);
        }
        if(BoolList.get(5)==false) {
            pre_game.setChecked(false);
            pre_game.setAlpha(0.1f);
        }
        if(BoolList.get(6)==false) {
            pre_language.setChecked(false);
            pre_language.setAlpha(0.1f);
        }
        if(BoolList.get(7)==false) {
            pre_meet.setChecked(false);
            pre_meet.setAlpha(0.1f);
        }
        if(BoolList.get(8)==false) {
            pre_movie.setChecked(false);
            pre_movie.setAlpha(0.1f);
        }
        if(BoolList.get(9)==false) {
            pre_music.setChecked(false);
            pre_music.setAlpha(0.1f);
        }
        if(BoolList.get(10)==false) {
            pre_sports.setChecked(false);
            pre_sports.setAlpha(0.1f);
        }
        if(BoolList.get(11)==false) {
            pre_travel.setChecked(false);
            pre_travel.setAlpha(0.1f);
        }
        if(BoolList.get(12)==false) {
            pre_volunteer.setChecked(false);
            pre_volunteer.setAlpha(0.1f);
        }

        pre_art.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pre_art.isChecked()){
                    pre_art.setAlpha(1f);
                    BoolList.set(0,true);
                    ;
                }
                else{
                    pre_art.setAlpha(0.1f);
                    BoolList.set(0,false);
                }
            }
        });
        pre_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pre_book.isChecked()){
                    pre_book.setAlpha(1f);
                    BoolList.set(1,true);
                }
                else{
                    pre_book.setAlpha(0.1f);
                    BoolList.set(1,false);
                }
            }
        });
        pre_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pre_camera.isChecked()){
                    pre_camera.setAlpha(1f);
                    BoolList.set(2,true);
                }
                else{
                    pre_camera.setAlpha(0.1f);
                    BoolList.set(2,false);
                }
            }
        });
        pre_dance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pre_dance.isChecked()){
                    pre_dance.setAlpha(1f);
                    BoolList.set(3,true);
                }
                else{
                    pre_dance.setAlpha(0.1f);
                    BoolList.set(3,false);
                }
            }
        });
        pre_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pre_food.isChecked()){
                    pre_food.setAlpha(1f);
                    BoolList.set(4,true);
                }
                else{
                    pre_food.setAlpha(0.1f);
                    BoolList.set(4,false);
                }
            }
        });
        pre_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pre_game.isChecked()){
                    pre_game.setAlpha(1f);
                    BoolList.set(5,true);
                }
                else{
                    pre_game.setAlpha(0.1f);
                    BoolList.set(5,false);
                }
            }
        });
        pre_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pre_language.isChecked()){
                    pre_language.setAlpha(1f);
                    BoolList.set(6,true);
                }
                else{
                    pre_language.setAlpha(0.1f);
                    BoolList.set(6,false);
                }
            }
        });
        pre_meet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pre_meet.isChecked()){
                    pre_meet.setAlpha(1f);
                    BoolList.set(7,true);
                }
                else{
                    pre_meet.setAlpha(0.1f);
                    BoolList.set(7,false);
                }
            }
        });
        pre_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pre_movie.isChecked()){
                    pre_movie.setAlpha(1f);
                    BoolList.set(8,true);
                }
                else{
                    pre_movie.setAlpha(0.1f);
                    BoolList.set(8,false);
                }
            }
        });
        pre_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pre_music.isChecked()){
                    pre_music.setAlpha(1f);
                    BoolList.set(9,true);
                }
                else{
                    pre_music.setAlpha(0.1f);
                    BoolList.set(9,false);
                }
            }
        });

        pre_sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pre_sports.isChecked()){
                    pre_sports.setAlpha(1f);
                    BoolList.set(10,true);
                }
                else{
                    pre_sports.setAlpha(0.1f);
                    BoolList.set(10,false);
                }
            }
        });
        pre_travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pre_travel.isChecked()){
                    pre_travel.setAlpha(1f);
                    BoolList.set(11,true);

                    // String trueOrFalse = String.valueOf(BoolList.get(11));
                    // Toast.makeText(getApplicationContext(), trueOrFalse, Toast.LENGTH_SHORT).show();
                }
                else{
                    pre_travel.setAlpha(0.1f);
                    BoolList.set(11,false);
                    //String trueOrFalse = String.valueOf(BoolList.get(11));
                    //Toast.makeText(getApplicationContext(), trueOrFalse, Toast.LENGTH_SHORT).show();
                }
            }
        });
        pre_volunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pre_volunteer.isChecked()){
                    pre_volunteer.setAlpha(1f);
                    BoolList.set(12,true);
                }
                else{
                    pre_volunteer.setAlpha(0.1f);
                    BoolList.set(12,false);
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

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {

                UserData userdata = new UserData();
                userdata.setBoolList(BoolList);

                Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),profile.class));



            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(getApplicationContext(),profile.class));
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}