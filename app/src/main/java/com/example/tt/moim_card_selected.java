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
    Button reloadButton;
    Button start;
    Button attend_chatting;
    TextView act_title;
    TextView act_detail;
    Intent mintent;
    private FusedLocationProviderClient mfusedLocationProviderClient;
    LatLng correct_cur_loc;
    User user;
    TimePicker timePicker;

    static SharedPreferences save;
    static SharedPreferences.Editor editor;

    private Intent mBackgroundServiceIntent;
    private BackgroundService mBackgroundService;

    int reload_num;
    float add_score = 10;
    JSONObject moim_act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user  = User.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moim_card_selected);
        final Intent intent = getIntent();

        mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        save = getSharedPreferences("mysave", MODE_PRIVATE);
        editor = save.edit();
        editor.putInt("page", 3);
        editor.apply();

        reload_num = save.getInt("reload",5);

        final String str_act = save.getString("activity","");

        Activity play_activity = new Activity();
        try {
            moim_act = new JSONObject(save.getString("cat_all",""));
            play_activity = new Activity();
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

            //editor.putString("date");
            editor.apply();
            mBackgroundService = new BackgroundService();
            mBackgroundServiceIntent = new Intent(getApplicationContext(), mBackgroundService.getClass());
            startService(mBackgroundServiceIntent);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Intent actintent = new Intent(getApplicationContext(), createreview.class);
        actintent.putExtra("act_id", play_activity.act_id);

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
                if (reload_num > 0) {
                    reload_num --;
                    Intent reintent = new Intent(getApplicationContext(), CardInfo.class);
                    reintent.putExtra("reload", reload_num);
                    editor.remove("activity");
                    editor.apply();
                    startActivity(reintent);
                }else{
                    new AlertDialog.Builder(moim_card_selected.this).setTitle("Reload 횟수가 끝났습니다.\n 포기하겠습니까?") .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            editor.remove("reload");
                            editor.remove("page");
                            editor.remove("activity");
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
                }
            }
        });


        mintent = new Intent(getApplicationContext(), map.class);
        mintent.putExtra("title",play_activity.title);
        mintent.putExtra("latitude", play_activity.latitude);
        mintent.putExtra("longitude",play_activity.longitude);


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
                            actintent.putExtra("save_score", add_score);
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
    public Activity set_info(String url) throws JSONException, IOException {
        url_json Read = new url_json();
        Read.readJsonFromUrl(url);
        JSONObject activities_json = Read.readJsonFromUrl(url);
        JSONArray activities_arr = new JSONArray(activities_json.get("temp").toString());
        List<Activity> activities = new ArrayList<Activity>();
        for (int i = 0; i < activities_arr.length(); i++) {
            JSONObject temp_json_activity = (JSONObject) activities_arr.get(i);
            Activity temp_activity = new Activity(temp_json_activity);
            //temp_activity.score = 현재위치 위도 경도, 활동 위도 경도 차이
            double lat = Math.pow(temp_activity.latitude - user.getUser_location().latitude, 2);
            double lon = Math.pow(temp_activity.longitude - user.getUser_location().longitude, 2);
            float score = (float) Math.sqrt(lat + lon);
            temp_activity.score = score;
            activities.add(temp_activity);
        }
        if (activities.size() > 0) {
            float min = activities.get(0).score;
            int index = 0;
            for (int u = 0; u < activities.size(); u++) {
                if (activities.get(u).score < min) {
                    min = activities.get(u).score;
                    index = u;
                }
            }
            return activities.get(index);
        }
        else{
            Activity error = new Activity("0", "null","null","null",
                    'n','n', "null", 0, 0, 0);
            return error;
        }
    }

    public void openmap(View view) {
        startActivity(mintent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "권한 체크 거부 됌", Toast.LENGTH_SHORT).show();
                }
                else {
                    mfusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null) {
                                correct_cur_loc = new LatLng(location.getLatitude(),location.getLongitude());
                                user.setUser_location(correct_cur_loc);
                            }
                        }
                    });
                }
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
