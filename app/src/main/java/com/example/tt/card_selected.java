package com.example.tt;

import android.Manifest;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

    static SharedPreferences save;
    static SharedPreferences.Editor editor;

    private Intent mBackgroundServiceIntent;
    private BackgroundService mBackgroundService;

    int reload_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user  = User.getInstance();
        super.onCreate(savedInstanceState);
        final Intent intent = getIntent();
        reload_num = intent.getExtras().getInt("reload");
        mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        String cat_name = intent.getExtras().getString("cat_name");

        save = getSharedPreferences("mysave", MODE_PRIVATE);
        editor = save.edit();

        String url = "http://52.79.125.108/api/activity/" + cat_name;
        setContentView(R.layout.activity_card_selected);
        Activity play_activity = new Activity("0", "null","null","null",
                'n','n', "null", 0, 0, 0);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
            return;
        }
        mfusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null) {
                    correct_cur_loc = new LatLng(location.getLatitude(),location.getLongitude());
                    user.setUser_location(correct_cur_loc);
                }
                else{
                    Toast.makeText(getApplicationContext(), "권한 체크 거부 됌", Toast.LENGTH_SHORT).show();
                    LatLng loc_temp = new LatLng(0,0);
                    user.setUser_location(loc_temp);
                }
            }
        });
        try {
            play_activity = set_info(url);
            user.setUser_act(play_activity);
            editor.putString("latitude", String.valueOf(play_activity.latitude));
            editor.putString("longitude", String.valueOf(play_activity.longitude));
            editor.putString("date","");
            //editor.putString("date");
            editor.apply();
            mBackgroundService = new BackgroundService();
            mBackgroundServiceIntent = new Intent(getApplicationContext(), mBackgroundService.getClass());
            startService(mBackgroundServiceIntent);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Intent actintent = new Intent(getApplicationContext(), createreview.class);
        actintent.putExtra("act_id", play_activity.act_id);


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
                    startActivity(reintent);
                }
                else{
                    Toast.makeText(card_selected.this, "reload 횟수가 끝났습니다.", Toast.LENGTH_LONG);
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
                    startActivity(actintent);
                } else {
                    // 외부 활동이라면
                    mfusedLocationProviderClient.getLastLocation().addOnSuccessListener(card_selected.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                correct_cur_loc = new LatLng(location.getLatitude(), location.getLongitude());
                                user.setUser_location(correct_cur_loc);
                                if (user.getUser_location().latitude == finalPlay_activity.latitude &&
                                        user.getUser_location().longitude == finalPlay_activity.longitude) {
                                    //외부 활동 완료, 점수 추가!!
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
}
