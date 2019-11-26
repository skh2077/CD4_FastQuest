package com.example.tt;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tt.data.User;
import com.example.tt.data.category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Vector;

public class CardInfo extends AppCompatActivity {
    Button card1, card2, card3, card4, card5;
    Vector<category> value;
    static int reload_value = 5;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = User.getInstance();
        final url_json read = new url_json();
        Intent get_intent = getIntent();
        try{
            if(reload_value < get_intent.getExtras().getInt("reload")){
                reload_value = get_intent.getExtras().getInt("reload");
            }
        }
        catch (Exception e) {
            Toast.makeText(this, "reload error", Toast.LENGTH_SHORT);
        }

        JSONObject user_info;
        String user_info_url = "http://52.79.125.108/api/user/%EA%B8%B0%EB%8B%88%ED%94%BC%EA%B7%B8";
        try {
            user_info = read.readJsonFromUrl(user_info_url);
            JSONObject temp = new JSONObject(user_info.get("temp").toString());
            user.setNickname(temp.get("nickname").toString());
            user.setActivity(Float.parseFloat(temp.get("activity").toString()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = "http://52.79.125.108/api/users/" + user.getNickname();
        int moim_str_num = 0;
        if(user.getismoim() == true) {
            url = "http://52.79.125.108/api/selectassemble/" + "skh20";
            moim_str_num = 1;
        }
        value = new Vector<>();
        int cur_card_num = 0;
        String[] moim_str = {"","\n모임"};
        do {
            if(value.size() != 0) {
                url = "http://52.79.125.108/api/users/" + user.getNickname();
                cur_card_num = value.size();
                moim_str_num = 0;
            }
            JSONObject cat_json = null;
            JSONArray cat_arr = null;
            try {
                cat_json = read.readJsonFromUrl(url);
                cat_arr = new JSONArray(cat_json.get("temp").toString());
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
        }while(value.size() != 5);

        setContentView(R.layout.activity_card_info);

        card1 = (Button) findViewById(R.id.card1);
        card_set(value.get(0).cat_name, (int) Math.abs(value.get(0).activity_rate - user.getActivity()), card1);
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (value.get(0).cat_name.contains("모임")) {
                    intent = new Intent(getApplicationContext(), moim_card_selected.class);
                } else {
                    intent = new Intent(getApplicationContext(), card_selected.class);
                }
                intent.putExtra("reload", reload_value);
                intent.putExtra("cat_name", value.get(0).cat_name);
                intent.putExtra("cat_all", value.get(0).getCat_all());
                startActivity(intent);
            }
        });
        card2 = (Button) findViewById(R.id.card2);
        card_set(value.get(1).cat_name, (int) Math.abs(value.get(1).activity_rate - user.getActivity()), card2);
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (value.get(1).cat_name.contains("모임")) {
                    intent = new Intent(getApplicationContext(), moim_card_selected.class);
                } else {
                    intent = new Intent(getApplicationContext(), card_selected.class);
                }
                intent.putExtra("reload", reload_value);
                intent.putExtra("cat_name", value.get(1).cat_name);
                intent.putExtra("cat_all", value.get(1).getCat_all());
                startActivity(intent);
            }
        });
        card3 = (Button) findViewById(R.id.card3);
        card_set(value.get(2).cat_name, (int) Math.abs(value.get(2).activity_rate - user.getActivity()), card3);
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (value.get(2).cat_name.contains("모임")) {
                    intent = new Intent(getApplicationContext(), moim_card_selected.class);
                } else {
                    intent = new Intent(getApplicationContext(), card_selected.class);
                }
                intent.putExtra("reload", reload_value);
                intent.putExtra("cat_name", value.get(2).cat_name);
                intent.putExtra("cat_all", value.get(2).getCat_all());
                startActivity(intent);
            }
        });
        card4 = (Button) findViewById(R.id.card4);
        card_set(value.get(3).cat_name, (int) Math.abs(value.get(3).activity_rate - user.getActivity()), card4);
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (value.get(3).cat_name.contains("모임")) {
                    intent = new Intent(getApplicationContext(), moim_card_selected.class);
                } else {
                    intent = new Intent(getApplicationContext(), card_selected.class);
                }
                intent.putExtra("reload", reload_value);
                intent.putExtra("cat_name", value.get(3).cat_name);
                intent.putExtra("cat_all", value.get(3).getCat_all());
                startActivity(intent);
            }
        });
        card5 = (Button) findViewById(R.id.card5);
        card_set(value.get(4).cat_name, (int) Math.abs(value.get(4).activity_rate - user.getActivity()), card5);
        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (value.get(4).cat_name.contains("모임")) {
                    intent = new Intent(getApplicationContext(), moim_card_selected.class);
                } else {
                    intent = new Intent(getApplicationContext(), card_selected.class);
                }
                intent.putExtra("reload", reload_value);
                intent.putExtra("cat_name", value.get(4).cat_name);
                intent.putExtra("cat_all", value.get(4).getCat_all());
                startActivity(intent);
            }
        });

    }

    public void reload(View view) {

        if (reload_value > 0) {
            finish();
            reload_value = reload_value - 1;
            startActivity(new Intent(getApplicationContext(), CardInfo.class));
        } else {
            new AlertDialog.Builder(this).setTitle("Reload 횟수가 끝났습니다.").setPositiveButton("OK", null).show();
            return;
        }
    }


    public void card_set(String text, int value, Button card) {
        value = value /20;
        card.setText(text);
        card.setTextColor(Color.BLACK);
        card.setTextSize(20);
        card.setTypeface(card.getTypeface(), Typeface.BOLD);
        switch (value) {
            case 0:
                card.setBackgroundColor(Color.GRAY);
                break;
            case 1:
                card.setBackgroundColor(Color.GREEN);
                break;
            case 2:
                card.setBackgroundColor(Color.BLUE);
                break;
            case 3:
                card.setBackgroundColor(Color.parseColor("#FF00DD"));
                break;
            case 4 :
                card.setBackgroundColor(Color.YELLOW);
                break;
        }
    }


}
