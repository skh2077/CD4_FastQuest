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
    static int reload_value = 5;
    User user;
    static SharedPreferences save;
    static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = User.getInstance();
        save = getSharedPreferences("mysave", MODE_PRIVATE);
        editor = save.edit();
        editor.putInt("page", 1);
        reload_value = save.getInt("reload",5);
        final url_json read = new url_json();
        Intent get_intent = getIntent();
        try{
            if(reload_value > get_intent.getExtras().getInt("reload")){
                reload_value = get_intent.getExtras().getInt("reload");
            }
        }
        catch (Exception e) {
            Toast.makeText(this, "reload error", Toast.LENGTH_SHORT);
        }
        editor.putInt("reload", reload_value);
        editor.apply();

        JSONObject user_info;
        String user_info_url = "http://52.79.125.108/api/user/" + user.getUsername();
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
        if(user.getIsinside() == true) {
            url = "http://52.79.125.108/api/select/" + user.getUsername();
        }
        int moim_str_num = 0;
        if(user.getismoim() == true) {
            url = "http://52.79.125.108/api/selectassemble/" + user.getUsername();
            moim_str_num = 1;
        }
        value = new Vector<>();
        int cur_card_num = 0;
        String[] moim_str = {"","\n모임"};
        boolean lack = false;
        do {
            if(lack == true) {
                url = "http://52.79.125.108/api/users/" + user.getNickname();
                if(user.getIsinside() == true) {
                    url = "http://52.79.125.108/api/select/" + user.getUsername();
                }
                cur_card_num = value.size();
                moim_str_num = 0;
            }
            lack = true;
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

        card1 = (TextView) findViewById(R.id.card1);
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
                editor.putString("cat_all", value.get(0).getCat_all());
                editor.apply();
                startActivity(intent);
            }
        });
        card2 = (TextView) findViewById(R.id.card2);
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
                editor.putString("cat_all", value.get(0).getCat_all());
                editor.apply();
                startActivity(intent);
            }
        });
        card3 = (TextView) findViewById(R.id.card3);
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
                editor.putString("cat_all", value.get(0).getCat_all());
                editor.apply();
                startActivity(intent);
            }
        });
        card4 = (TextView) findViewById(R.id.card4);
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
                editor.putString("cat_all", value.get(0).getCat_all());
                editor.apply();
                startActivity(intent);
            }
        });
        card5 = (TextView) findViewById(R.id.card5);
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
                editor.putString("cat_all", value.get(0).getCat_all());
                editor.apply();
                startActivity(intent);
            }
        });

    }

    public void reload(View view) {

        if (reload_value > 0) {
            reload_value = reload_value - 1;
            editor.putInt("reload", reload_value);
            editor.apply();
            startActivity(new Intent(getApplicationContext(), CardInfo.class));
            finish();
        } else {
            new AlertDialog.Builder(this).setTitle("Reload 횟수가 끝났습니다.\n 포기하겠습니까?") .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    editor.remove("reload");
                    editor.remove("page");
                    editor.remove("activity");
                    editor.apply();
                    reload_value = 5;
                    //스코어 하락 시킬 것
                    edit_score(user.getUser_id(),  -5);

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.cancel();
                }
            }).create().show();
            return;
        }
    }


    public void card_set(String text, int value, TextView card) {
        value = value /20;
        card.setText(text);
        card.setTextColor(Color.BLACK);
        card.setTextSize(30);
        card.setTypeface(card.getTypeface(), Typeface.BOLD);
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
    public void edit_score(String user_id, int score) {
        // 수정하면 유저 id 받으면 통신하는게 완성 됨
        int temp_score = 0;
        com.android.volley.Response.Listener<JSONObject> pjresponseListener = new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    user.setScore(Integer.parseInt(response.get("score").toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        String URL = "http://52.79.125.108/api/detail/" + user_id;
        //String URL = "http://52.79.125.108/api/user/" +  user_name;
        url_json read = new url_json();
        JSONObject jtemp_score = null;
        try {
            jtemp_score = read.readJsonFromUrl(URL);
            JSONObject temp = new JSONObject(jtemp_score.get("temp").toString());
            temp_score = Integer.parseInt(temp.get("score").toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject pointj = new JSONObject();
        try {
            pointj.put("score", temp_score +score);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        addpointRequest preq = new addpointRequest(Request.Method.PUT, pointj, URL, pjresponseListener, null);
        RequestQueue pjqueue = Volley.newRequestQueue(this);
        pjqueue.add(preq);
    }
}
