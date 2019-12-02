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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
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
import java.util.ArrayList;
import java.util.List;

public class card_selected extends AppCompatActivity {
    Button reloadButton;
    Button start;
    TextView act_title;
    TextView act_detail;
    Intent mintent;
    private FusedLocationProviderClient mfusedLocationProviderClient;
    LatLng correct_cur_loc;
    User user;
    Activity play_activity;

    static SharedPreferences save;
    static SharedPreferences.Editor editor;

    private Intent mBackgroundServiceIntent;
    private BackgroundService mBackgroundService;

    int reload_num;
    float add_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user = User.getInstance();
        super.onCreate(savedInstanceState);
        mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        setContentView(R.layout.activity_card_selected);

        String cat_score;
        save = getSharedPreferences("mysave", MODE_PRIVATE);
        editor = save.edit();

        editor.putInt("page", 2);
        editor.apply();

        add_score = 0;
        final String str_act = save.getString("activity","");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
            return;
        }
        mfusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    correct_cur_loc = new LatLng(location.getLatitude(), location.getLongitude());
                    user.setUser_location(correct_cur_loc);
                } else {
                    Toast.makeText(getApplicationContext(), "권한 체크 거부 됌", Toast.LENGTH_SHORT).show();
                    LatLng loc_temp = new LatLng(0, 0);
                    user.setUser_location(loc_temp);
                }
            }
        });


        if (str_act.equals("")) {
            final Intent intent = getIntent();
            reload_num = intent.getExtras().getInt("reload");
            editor.putInt("reload",reload_num);
            editor.apply();
            String cat_name = intent.getExtras().getString("cat_name");
            String cat_all = intent.getExtras().getString("cat_all");

            try {
                JSONObject tempj = new JSONObject(cat_all);
                cat_score = tempj.get("activity_rate").toString();
                add_score = Math.abs(user.getActivity() - Float.parseFloat(cat_score));
                editor.putFloat("cat_score", add_score);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String url = "http://52.79.125.108/api/activity/" + cat_name;
            play_activity = new Activity("0", "null", "null", "null",
                    'n', 'n', "null", 0, 0, 0);
            try {
                play_activity = set_info(url);
                editor.putString("latitude", String.valueOf(play_activity.latitude));
                editor.putString("longitude", String.valueOf(play_activity.longitude));
                editor.putString("date", "");
                //editor.putString("date");
                editor.apply();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            reload_num = save.getInt("reload",5);
            add_score = save.getFloat("cat_score",0);
            try {
                JSONObject tem_j = new JSONObject(str_act);
                play_activity = new Activity(tem_j);
                play_activity.score = save.getFloat("act_score",0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        user.setUser_act(play_activity);

        add_score = add_score + play_activity.score * 10;
        mBackgroundService = new BackgroundService();
        mBackgroundServiceIntent = new Intent(getApplicationContext(), mBackgroundService.getClass());
        startService(mBackgroundServiceIntent);

        final Intent actintent = new Intent(getApplicationContext(), createreview.class);
        actintent.putExtra("act_id", play_activity.act_id);
        actintent.putExtra("save_score", add_score);


        act_title = (TextView)findViewById(R.id.Title);
        act_title.setText(play_activity.title);
        act_detail = (TextView)findViewById(R.id.Detail);
        act_detail.setText(play_activity.content);



        reloadButton = (Button)findViewById(R.id.Reload);
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
                    new AlertDialog.Builder(card_selected.this).setTitle("Reload 횟수가 끝났습니다.\n 포기하겠습니까?") .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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


        start = (Button)findViewById(R.id.Start);
        final Activity finalPlay_activity = play_activity;
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalPlay_activity.outside == 'n') {
                    // 실내 활동이라면
                    actintent.putExtra("save_score", 0);
                    Toast.makeText(card_selected.this, "실내활동 입니다.", Toast.LENGTH_SHORT).show();
                    editor.putInt("reload",5);
                    editor.apply();
                    startActivity(actintent);
                } else {
                    // 외부 활동이라면
                    mfusedLocationProviderClient.getLastLocation().addOnSuccessListener(card_selected.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                correct_cur_loc = new LatLng(location.getLatitude(), location.getLongitude());
                                user.setUser_location(correct_cur_loc);
                                if(Math.abs(user.getUser_location().latitude - finalPlay_activity.latitude) < 0.0005 &&//약50m
                                        Math.abs(user.getUser_location().longitude - finalPlay_activity.longitude) < 0.0005) {
                                    //외부 활동 완료, 점수 추가!!
                                    actintent.putExtra("save_score", add_score);
                                    editor.putInt("reload",5);
                                    editor.apply();
                                    startActivity(actintent);
                                } else {
                                    Toast.makeText(card_selected.this, "활동 장소에 도착한 후 눌러주세요", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "권한 체크 거부 됌", Toast.LENGTH_SHORT).show();
                                LatLng loc_temp = new LatLng(0, 0);
                                user.setUser_location(loc_temp);
                            }
                        }
                    });
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
        List<JSONObject> jactivities = new ArrayList<>();
        for (int i = 0; i < activities_arr.length(); i++) {
            JSONObject temp_json_activity = (JSONObject) activities_arr.get(i);
            Activity temp_activity = new Activity(temp_json_activity);
            //temp_activity.score = 현재위치 위도 경도, 활동 위도 경도 차이
            double lat = Math.pow(temp_activity.latitude - user.getUser_location().latitude, 2);
            double lon = Math.pow(temp_activity.longitude - user.getUser_location().longitude, 2);
            float score = (float) Math.sqrt(lat + lon);
            temp_activity.score = score;
            activities.add(temp_activity);
            jactivities.add(temp_json_activity);
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
            editor.putString("activity", jactivities.get(index).toString());
            editor.putFloat("act_score", activities.get(index).score);
            editor.apply();
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
