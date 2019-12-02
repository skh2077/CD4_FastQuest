package com.example.tt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.tt.data.User;
import com.example.tt.data.UserData;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;


public class pre_cat extends AppCompatActivity implements View.OnClickListener {
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
    ChipGroup chipGroup;
    Button saveButton;
    ImageButton backButton;
    static ArrayList<Boolean> BoolList =  new ArrayList<Boolean>();
    String url;
    private JSONObject cat_json = null;
    private JSONArray cat_arr = null;
    final url_json read = new url_json();

    ArrayList<String> scat_list = new ArrayList<String>();
    ArrayList<String> scat_list_save = new ArrayList<String>();

    LayoutInflater inflater;

    User user;

    boolean[] first_check = new boolean[14];
    Vector<String> user_precat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pre_cat);

        pre_art = findViewById(R.id.pre_Art);
        pre_art.setOnClickListener(this);
        pre_book = findViewById(R.id.pre_Book);
        pre_book.setOnClickListener(this);
        pre_camera = findViewById(R.id.pre_Camera);
        pre_camera.setOnClickListener(this);
        pre_dance = findViewById(R.id.pre_Dance);
        pre_dance.setOnClickListener(this);
        pre_food = findViewById(R.id.pre_Food);
        pre_food.setOnClickListener(this);
        pre_game = findViewById(R.id.pre_Game);
        pre_game.setOnClickListener(this);
        pre_language =findViewById(R.id.pre_Language);
        pre_language.setOnClickListener(this);
        pre_book.setOnClickListener(this);
        pre_book.setOnClickListener(this);
        pre_meet = findViewById(R.id.pre_Meet);
        pre_meet.setOnClickListener(this);
        pre_movie = findViewById(R.id.pre_Movie);
        pre_movie.setOnClickListener(this);
        pre_music = findViewById(R.id.pre_Music);
        pre_music.setOnClickListener(this);
        pre_sports = findViewById(R.id.pre_Sports);
        pre_sports.setOnClickListener(this);
        pre_travel = findViewById(R.id.pre_Travel);
        pre_travel.setOnClickListener(this);
        pre_volunteer = findViewById(R.id.pre_Volunteer);
        pre_volunteer.setOnClickListener(this);

        saveButton = findViewById(R.id.saveButton);

        UserData userdata = new UserData();
        BoolList = userdata.getBooList();

        user = User.getInstance();

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        chipGroup = findViewById(R.id.chip_group);
        scat_list_save.add("tmp");

        for(int i =0; i < 14; i++) {
            first_check[i] = false;
        }

        url = "http://52.79.125.108/api/user/"+ user.getUser_id() +"/precat/";
        try {
            cat_json = read.readJsonFromUrl(url);
            cat_arr = new JSONArray(cat_json.get("temp").toString());
            for (int i = 0; i < cat_arr.length(); i++) {
                JSONObject temp = (JSONObject) cat_arr.get(i);
                if(!scat_list_save.contains(temp.get("cat_name")))
                {
                    scat_list_save.add(temp.get("cat_name").toString());
                }
            }
        }
        catch (Exception e) {

        }

    }
    public void OnClickHandler(View view)
    {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("수정사항").setMessage("저장하시겠습니까?");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {


                //DELETE and For-loop PUT
                save_chip();

                UserData userdata = new UserData(); // <= 먼 필요야?

                for (int i = 1; i < scat_list_save.size(); i++) {
                    Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>(){

                        @Override
                        public void onResponse(JSONObject response) {
                            try{
                            }
                            catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    };//Response.Listener 완료

                    JSONObject jsonObject = new JSONObject();

                    try {
                        jsonObject.put("user", user.getUser_id());
                        jsonObject.put("cat_name", scat_list_save.get(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    pre_cat_Request preCatRequest = new pre_cat_Request(Request.Method.POST, jsonObject, responseListener,null);
                    RequestQueue queue = Volley.newRequestQueue(pre_cat.this);

                    queue.add(preCatRequest);
                }

                // startActivity(new Intent(getApplicationContext(),MainActivity.class));
                userdata.setBoolList(BoolList);

                Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
               // startActivity(new Intent(getApplicationContext(),MainActivity.class));


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.pre_Art:
                save_chip();
                create_chip("공예");
                break;
            case R.id.pre_Book:
                save_chip();
                create_chip("인문");
                break;
            case R.id.pre_Camera:
                save_chip();
                create_chip("사진");
                break;
            case R.id.pre_Dance:
                save_chip();
                create_chip("댄스");
                break;
            case R.id.pre_Food:
                save_chip();
                create_chip("요리");
                break;
            case R.id.pre_Game:
                save_chip();
                create_chip("게임");
                break;
            case R.id.pre_Language:
                save_chip();
                create_chip("외국");
                break;
            case R.id.pre_Meet:
                save_chip();
                create_chip("사교");
                break;
            case R.id.pre_Movie:
                save_chip();
                create_chip("문화");
                break;
            case R.id.pre_Music:
                save_chip();
                create_chip("음악");
                break;
            case R.id.pre_Sports:
                save_chip();
                create_chip("운동");
                break;
            case R.id.pre_Travel:
                save_chip();
                create_chip("아웃");
                break;
            case R.id.pre_Volunteer:
                save_chip();
                create_chip("봉사");
                break;
        }
    }

    public void create_chip(String lcat) {
        scat_list.clear();
        chipGroup.removeAllViews();
        url = "http://52.79.125.108/api/lcat/" + lcat;
        try {
            cat_json = read.readJsonFromUrl(url);
            cat_arr = new JSONArray(cat_json.get("temp").toString());
            for (int i = 0; i < cat_arr.length(); i++) {
                JSONObject temp = (JSONObject) cat_arr.get(i);
                scat_list.add(temp.get("cat_name").toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), lcat, Toast.LENGTH_SHORT).show();
        inflater = LayoutInflater.from(pre_cat.this);
        for (String text : scat_list) {
            Chip chip = (Chip) inflater.inflate(R.layout.chip_item, null, false);
            chip.setText(text);
            if (scat_list_save.contains(text)) {
                chip.setChecked(true);
            }
            chipGroup.addView(chip);
        }
    }

    public void save_chip() {
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            boolean foo = scat_list_save.contains(chip.getText().toString());
            if (chip.isChecked() && !foo) {
                scat_list_save.add(chip.getText().toString());

            } else if (!chip.isChecked() && foo) {
                scat_list_save.remove(chip.getText().toString());
            }
        }
        chipGroup.removeAllViews();
        scat_list.clear();
    }
}