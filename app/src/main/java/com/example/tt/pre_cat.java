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
import com.example.tt.data.UserData;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


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
                for (int i = 0; i < chipGroup.getChildCount(); i++) {
                    Chip chip = (Chip) chipGroup.getChildAt(i);
                    boolean foo = scat_list_save.contains(chip.getText().toString());
                    if (chip.isChecked() && !foo) {
                        scat_list_save.add(chip.getText().toString());

                    } else if (!chip.isChecked() && foo) {
                        scat_list_save.remove(chip.getText().toString());
                    }
                }

                UserData userdata = new UserData();
                for (int i = 0; i < scat_list_save.size(); i++) {

                    Toast.makeText(getApplicationContext(), scat_list_save.get(i), Toast.LENGTH_SHORT).show();
                }

                // startActivity(new Intent(getApplicationContext(),MainActivity.class));
                userdata.setBoolList(BoolList);

                Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
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

                chipGroup.removeAllViews();
                scat_list.clear();
                for (int i = 0; i < chipGroup.getChildCount(); i++) {
                    Chip chip = (Chip) chipGroup.getChildAt(i);
                    boolean foo = scat_list_save.contains(chip.getText().toString());
                    if (chip.isChecked() && !foo) {
                        scat_list_save.add(chip.getText().toString());

                    } else if (!chip.isChecked() && foo) {
                        scat_list_save.remove(chip.getText().toString());
                    }
                }

                url = "http://52.79.125.108/api/lcat/공예";
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

                Toast.makeText(getApplicationContext(), pre_art.getText(), Toast.LENGTH_SHORT).show();
                LayoutInflater inflater = LayoutInflater.from(pre_cat.this);

                for (String text : scat_list) {
                    Chip chip = (Chip) inflater.inflate(R.layout.chip_item, null, false);
                    chip.setText(text);

                    if (scat_list_save.contains(text)) {
                        chip.setChecked(true);
                    }
                       /* chip.setOnCloseIconClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                chipGroup.removeView(v);
                            }
                        });*/
                    chipGroup.addView(chip);
                }

                Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();

                break;
            case R.id.pre_Book:

                if (pre_book.isChecked()) {
                    scat_list.clear();
                    chipGroup.removeAllViews();
                    url = "http://52.79.125.108/api/lcat/인문";
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
                    Toast.makeText(getApplicationContext(), pre_book.getText(), Toast.LENGTH_SHORT).show();
                    inflater = LayoutInflater.from(pre_cat.this);

                    for (String text : scat_list) {
                        Chip chip = (Chip) inflater.inflate(R.layout.chip_item, null, false);
                        chip.setText(text);

                        if (scat_list_save.contains(text)) {
                            chip.setChecked(true);
                        }
                        chipGroup.addView(chip);
                    }
                } else {
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
                Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_SHORT).show();

                break;
            case R.id.pre_Camera:

                if (pre_camera.isChecked()) {
                    chipGroup.removeAllViews();
                    scat_list.clear();
                    url = "http://52.79.125.108/api/lcat/사진";
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
                    Toast.makeText(getApplicationContext(), pre_camera.getText(), Toast.LENGTH_SHORT).show();
                    inflater = LayoutInflater.from(pre_cat.this);

                    for (String text : scat_list) {
                        Chip chip = (Chip) inflater.inflate(R.layout.chip_item, null, false);
                        chip.setText(text);

                        if (scat_list_save.contains(text)) {
                            chip.setChecked(true);
                        }
                        chipGroup.addView(chip);
                    }
                } else {

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
                Toast.makeText(getApplicationContext(), "3", Toast.LENGTH_SHORT).show();

                break;
            case R.id.pre_Dance:

                if (pre_dance.isChecked()) {
                    scat_list.clear();
                    chipGroup.removeAllViews();
                    url = "http://52.79.125.108/api/lcat/댄스";
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
                    Toast.makeText(getApplicationContext(), pre_dance.getText(), Toast.LENGTH_SHORT).show();
                    inflater = LayoutInflater.from(pre_cat.this);

                    for (String text : scat_list) {
                        Chip chip = (Chip) inflater.inflate(R.layout.chip_item, null, false);
                        chip.setText(text);
                        if (scat_list_save.contains(text)) {
                            chip.setChecked(true);
                        }

                        chipGroup.addView(chip);
                    }
                } else {
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

                Toast.makeText(getApplicationContext(), "4", Toast.LENGTH_SHORT).show();

                break;
            case R.id.pre_Food:

                if (pre_food.isChecked()) {
                    scat_list.clear();
                    chipGroup.removeAllViews();
                    url = "http://52.79.125.108/api/lcat/요리";
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
                    Toast.makeText(getApplicationContext(), pre_food.getText(), Toast.LENGTH_SHORT).show();
                    inflater = LayoutInflater.from(pre_cat.this);
                    for (String text : scat_list) {
                        Chip chip = (Chip) inflater.inflate(R.layout.chip_item, null, false);
                        chip.setText(text);
                        if (scat_list_save.contains(text)) {
                            chip.setChecked(true);
                        }
                        chipGroup.addView(chip);
                    }
                } else {
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
                Toast.makeText(getApplicationContext(), "5", Toast.LENGTH_SHORT).show();

                break;
            case R.id.pre_Game:

                if (pre_game.isChecked()) {
                    scat_list.clear();
                    chipGroup.removeAllViews();
                    url = "http://52.79.125.108/api/lcat/게임";
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
                    Toast.makeText(getApplicationContext(), pre_game.getText(), Toast.LENGTH_SHORT).show();
                    inflater = LayoutInflater.from(pre_cat.this);
                    for (String text : scat_list) {
                        Chip chip = (Chip) inflater.inflate(R.layout.chip_item, null, false);
                        chip.setText(text);
                        if (scat_list_save.contains(text)) {
                            chip.setChecked(true);
                        }
                        chipGroup.addView(chip);
                    }
                } else {
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

                Toast.makeText(getApplicationContext(), "6", Toast.LENGTH_SHORT).show();

                break;
            case R.id.pre_Language:
                if (pre_language.isChecked()) {
                    scat_list.clear();
                    chipGroup.removeAllViews();
                    url = "http://52.79.125.108/api/lcat/외국";
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
                    Toast.makeText(getApplicationContext(), pre_language.getText(), Toast.LENGTH_SHORT).show();
                    inflater = LayoutInflater.from(pre_cat.this);
                    for (String text : scat_list) {
                        Chip chip = (Chip) inflater.inflate(R.layout.chip_item, null, false);
                        chip.setText(text);
                        if (scat_list_save.contains(text)) {
                            chip.setChecked(true);
                        }
                        chipGroup.addView(chip);
                    }
                } else {
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

                Toast.makeText(getApplicationContext(), "7", Toast.LENGTH_SHORT).show();

                break;
            case R.id.pre_Meet:

                if (pre_meet.isChecked()) {
                    scat_list.clear();
                    chipGroup.removeAllViews();
                    url = "http://52.79.125.108/api/lcat/사교";
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
                    Toast.makeText(getApplicationContext(), pre_meet.getText(), Toast.LENGTH_SHORT).show();
                    inflater = LayoutInflater.from(pre_cat.this);
                    for (String text : scat_list) {
                        Chip chip = (Chip) inflater.inflate(R.layout.chip_item, null, false);
                        chip.setText(text);
                        if (scat_list_save.contains(text)) {
                            chip.setChecked(true);
                        }
                        chipGroup.addView(chip);
                    }
                } else {
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


                break;
            case R.id.pre_Movie:
                if (pre_movie.isChecked()) {
                    scat_list.clear();
                    chipGroup.removeAllViews();
                    url = "http://52.79.125.108/api/lcat/문화";
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
                    Toast.makeText(getApplicationContext(), pre_movie.getText(), Toast.LENGTH_SHORT).show();
                    inflater = LayoutInflater.from(pre_cat.this);
                    for (String text : scat_list) {
                        Chip chip = (Chip) inflater.inflate(R.layout.chip_item, null, false);
                        chip.setText(text);
                        if (scat_list_save.contains(text)) {
                            chip.setChecked(true);
                        }
                        chipGroup.addView(chip);
                    }
                } else {
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


                break;
            case R.id.pre_Music:
                if (pre_music.isChecked()) {
                    scat_list.clear();
                    chipGroup.removeAllViews();
                    url = "http://52.79.125.108/api/lcat/음악";
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
                    Toast.makeText(getApplicationContext(), pre_music.getText(), Toast.LENGTH_SHORT).show();
                    inflater = LayoutInflater.from(pre_cat.this);
                    for (String text : scat_list) {
                        Chip chip = (Chip) inflater.inflate(R.layout.chip_item, null, false);
                        chip.setText(text);
                        if (scat_list_save.contains(text)) {
                            chip.setChecked(true);
                        }
                        chipGroup.addView(chip);
                    }
                } else {
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

                break;
            case R.id.pre_Sports:

                if (pre_sports.isChecked()) {
                    scat_list.clear();
                    chipGroup.removeAllViews();
                    url = "http://52.79.125.108/api/lcat/운동";
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
                    Toast.makeText(getApplicationContext(), pre_sports.getText(), Toast.LENGTH_SHORT).show();
                    inflater = LayoutInflater.from(pre_cat.this);
                    for (String text : scat_list) {
                        Chip chip = (Chip) inflater.inflate(R.layout.chip_item, null, false);
                        chip.setText(text);
                        if (scat_list_save.contains(text)) {
                            chip.setChecked(true);
                        }
                        chipGroup.addView(chip);
                    }
                } else {
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

                break;
            case R.id.pre_Travel:

                if (pre_travel.isChecked()) {
                    scat_list.clear();
                    chipGroup.removeAllViews();
                    url = "http://52.79.125.108/api/lcat/아웃";
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
                    Toast.makeText(getApplicationContext(), pre_travel.getText(), Toast.LENGTH_SHORT).show();
                    inflater = LayoutInflater.from(pre_cat.this);
                    for (String text : scat_list) {
                        Chip chip = (Chip) inflater.inflate(R.layout.chip_item, null, false);
                        chip.setText(text);
                        if (scat_list_save.contains(text)) {
                            chip.setChecked(true);
                        }
                        chipGroup.addView(chip);
                    }
                } else {
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

                break;
            case R.id.pre_Volunteer:

                if (pre_volunteer.isChecked()) {
                    scat_list.clear();
                    chipGroup.removeAllViews();
                    url = "http://52.79.125.108/api/lcat/봉사";
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
                    Toast.makeText(getApplicationContext(), pre_volunteer.getText(), Toast.LENGTH_SHORT).show();
                    inflater = LayoutInflater.from(pre_cat.this);
                    for (String text : scat_list) {
                        Chip chip = (Chip) inflater.inflate(R.layout.chip_item, null, false);
                        chip.setText(text);
                        if (scat_list_save.contains(text)) {
                            chip.setChecked(true);
                        }
                        chipGroup.addView(chip);
                    }
                } else {
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

                break;

        }
    }
}