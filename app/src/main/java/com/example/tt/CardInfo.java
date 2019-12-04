package com.example.tt;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.tt.data.User;
import com.example.tt.data.category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Vector;

public class CardInfo extends AppCompatActivity {
    TextView card1, card2, card3, card4, card5;
    Vector<category> value;
    static int reload_value;
    User user;
    static SharedPreferences save;
    static SharedPreferences.Editor editor;
    int moim_str_num = 0, cur_card_num = 0;
    String[] moim_str = {"","\n모임"};
    boolean lack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = User.getInstance();
        setContentView(R.layout.activity_card_info);

        save = getSharedPreferences("mysave", MODE_PRIVATE);
        editor = save.edit();
        editor.putInt("page", 1);
        reload_value = save.getInt("reload",5);
        editor.putInt("reload", reload_value);
        editor.apply();

        // 우선순위 모임 > 내부 > 그냥
        String url = "http://52.79.125.108/api/users/" + user.getNickname(); // 토글 없이 추천
        url = check_inside(url);
        url = check_moim(url);

        value = new Vector<>();
        lack = false;
        do {
            check_lack(url);
            recommend_category(url);
        }while(value.size() != 5);

        card1 = (TextView) findViewById(R.id.card1);
        card_set(value.get(0).cat_name, (int) Math.abs(value.get(0).activity_rate - user.getActivity()), card1);
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected(0);
                finish();
            }
        });
        card2 = (TextView) findViewById(R.id.card2);
        card_set(value.get(1).cat_name, (int) Math.abs(value.get(1).activity_rate - user.getActivity()), card2);
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected(1);
                finish();
            }
        });
        card3 = (TextView) findViewById(R.id.card3);
        card_set(value.get(2).cat_name, (int) Math.abs(value.get(2).activity_rate - user.getActivity()), card3);
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected(2);
                finish();
            }
        });
        card4 = (TextView) findViewById(R.id.card4);
        card_set(value.get(3).cat_name, (int) Math.abs(value.get(3).activity_rate - user.getActivity()), card4);
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected(3);
                finish();
            }
        });
        card5 = (TextView) findViewById(R.id.card5);
        card_set(value.get(4).cat_name, (int) Math.abs(value.get(4).activity_rate - user.getActivity()), card5);
        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected(4);
                finish();
            }
        });

    }

    public void reload(View view) {
        Reload card_reload = new Reload(this);
        card_reload.clicked_reload(reload_value);
    }


    public void card_set(String text, int value, TextView card) {
        int color_range = 20;
        int Text_size = 30;
        value = value /color_range;
        card.setText(text);
        card.setTextColor(Color.BLACK);
        card.setTextSize(Text_size);
        switch (value) {
            case 0:
                card.setBackground(getDrawable(R.drawable.graycard));
                break;
            case 1:
                card.setBackground(getDrawable(R.drawable.greencard));
                break;
            case 2:
                card.setBackground(getDrawable(R.drawable.bluecard));
                break;
            case 3:
                card.setBackground(getDrawable(R.drawable.pinkcard));
                break;
            case 4 :
                card.setBackground(getDrawable(R.drawable.yellowcard));
                break;
        }
    }

    private BackPressHandler backPressHandler = new BackPressHandler(this);
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressHandler.onBackPressed();
    }

    public String check_lack(String url){
        if(lack == true) { // 모임수가 부족하다면
            url = "http://52.79.125.108/api/users/" + user.getNickname(); // 토글 없이
            url = check_inside(url);
            cur_card_num = value.size();
            moim_str_num = 0;
        }
        lack = true;
        return url;
    }

    public void recommend_category(String url) {
        url_json read = new url_json();
        try {
            JSONObject cat_json = read.readJsonFromUrl(url);
            JSONArray cat_arr = new JSONArray(cat_json.get("temp").toString());
            for (int i = 0; i < cat_arr.length() - cur_card_num; i++) {
                JSONObject temp = (JSONObject) cat_arr.get(i);
                category temp_cat = new category();
                temp_cat.cat_name = temp.get("cat_name").toString() + moim_str[moim_str_num];
                temp_cat.activity_rate = Float.parseFloat(temp.get("activity_rate").toString());
                temp_cat.setCat_all(temp.toString());
                value.add(temp_cat);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void selected(int card){
        Intent intent;
        if (value.get(card).cat_name.contains("모임")) {
            intent = new Intent(getApplicationContext(), moim_card_selected.class);
        } else {
            intent = new Intent(getApplicationContext(), card_selected.class);
        }
        editor.putString("cat_all", value.get(card).getCat_all());
        editor.apply();
        startActivity(intent);
    }

    String check_inside(String url) {
        if(user.getIsinside() == true) { // 안쪽 활동이라면
            url = "http://52.79.125.108/api/select/" + user.getUsername();
        }
        return url;
    }
    String check_moim(String url) {
        if(user.getismoim() == true) { // 모임 활동이라면
            url = "http://52.79.125.108/api/selectassemble/" + user.getUsername();
            moim_str_num = 0;
        }
        return url;
    }
}
