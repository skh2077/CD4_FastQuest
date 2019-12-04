package com.example.tt;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.tt.data.Activity;
import com.example.tt.data.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class moim_card_selected extends AppCompatActivity {
    Button reloadButton, start, attend_chatting;
    TextView act_title, act_detail;
    private FusedLocationProviderClient mfusedLocationProviderClient;
    User user;
    TimePicker timePicker;

    static SharedPreferences save;
    static SharedPreferences.Editor editor;

    private Intent mBackgroundServiceIntent;
    private BackgroundService mBackgroundService;

    int reload_num;
    int add_score = 10; // 모임 점수는 일괄적으로 10
    JSONObject moim_act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moim_card_selected);

        user  = User.getInstance();
        mBackgroundService = new BackgroundService();
        mBackgroundServiceIntent = new Intent(getApplicationContext(), mBackgroundService.getClass());
        startService(mBackgroundServiceIntent);

        save = getSharedPreferences("mysave", MODE_PRIVATE);
        editor = save.edit();

        reload_num = save.getInt("reload",5);
        Activity play_activity = new Activity();


        try {
            moim_act = new JSONObject(save.getString("cat_all",""));
            play_activity = new Activity();
            play_activity.act_id = moim_act.get("act_id").toString();
            play_activity.title = moim_act.get("title").toString();
            play_activity.content = moim_act.get("content").toString();
            String[] temp_array = moim_act.get("time").toString().split("T");
            String moim_act_time_str = temp_array[0] + "-" + temp_array[1];
            Date moim_act_time = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").parse(moim_act_time_str);
            play_activity.setDate(moim_act_time);
            user.setUser_act(play_activity);
            editor.putString("latitude", "");
            editor.putString("longitude", "");
            editor.putString("date", moim_act_time_str);
            editor.putInt("page", 3);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Intent actintent = new Intent(getApplicationContext(), createreview.class);
        editor.putString("act_id", play_activity.act_id);
        editor.apply();

        act_title = (TextView)findViewById(R.id.Title2);
        act_title.setText(play_activity.title);
        act_detail = (TextView)findViewById(R.id.Detail2);
        act_detail.setText(play_activity.content);
        timePicker = (TimePicker)findViewById(R.id.Timepick);
        timePicker.setHour(play_activity.getDate().getHours());
        timePicker.setMinute(play_activity.getDate().getMinutes());

        attend_chatting = (Button)findViewById(R.id.chatting);
        attend_chatting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), chat.class);
                try {
                    intent.putExtra("room_name", moim_act.get("title").toString() + moim_act.get("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });


        reloadButton = (Button)findViewById(R.id.Reload2);
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reload card_reload = new Reload(moim_card_selected.this);
                card_reload.clicked_reload(reload_num);
            }
        });

        start = (Button)findViewById(R.id.Start2);
        final Activity finalPlay_activity = play_activity;
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gtime = save.getString("date", "");
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
                format.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
                String cur_str = format.format(new Date());
                Date current = new Date();

                if (!gtime.equals("")) {
                    try {
                        Date goal_time = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").parse(gtime);
                        current = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").parse(cur_str);
                        if (Math.abs(goal_time.getTime() - current.getTime()) < 1000 * 60 * 30) {
                            editor.putInt("score", add_score);
                            editor.apply();
                            startActivity(actintent);
                        } else {
                            Toast.makeText(moim_card_selected.this, "약속시간 30분 내외로 가능합니다.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private BackPressHandler backPressHandler = new BackPressHandler(this);

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressHandler.onBackPressed();
    }
}
