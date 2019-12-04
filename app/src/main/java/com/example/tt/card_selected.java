package com.example.tt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tt.data.Activity;
import com.example.tt.data.User;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class card_selected extends AppCompatActivity {
    Button reloadButton, start;
    TextView act_title, act_detail;
    Intent map_intent;
    User user;
    Activity play_activity;
    Current_Location current_location;
    static SharedPreferences save;
    static SharedPreferences.Editor editor;
    private Intent mBackgroundServiceIntent;
    private BackgroundService mBackgroundService;
    int reload_num, score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user = User.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_selected);

        save = getSharedPreferences("mysave", MODE_PRIVATE);
        editor = save.edit();

        reload_num = save.getInt("reload", 5);
        score = save.getInt("score", 0);
        final String selected_activity = save.getString("activity","");

        //현재 위치 받기
        current_location = new Current_Location(this);
        user.setUser_location(current_location.get_current_location());

        if (selected_activity.equals("")) { //처음 실행 될 때
            String cat_all = save.getString("cat_all","");
            String cat_name ="";
            try {
                JSONObject temp_category = new JSONObject(cat_all);
                cat_name = temp_category.get("cat_name").toString();
                score = (int)Math.abs(user.getActivity() - Float.parseFloat(temp_category.get("activity_rate").toString()));
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
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            score = (int)(score + play_activity.score * 10);
            editor.putInt("page", 2);
            editor.putInt("score",(int)score);
            editor.putString("act_id", play_activity.act_id);
            editor.apply();
        }
        else{//종료 후 다시 시작될 때
            try {
                JSONObject temp_activity = new JSONObject(selected_activity);
                play_activity = new Activity(temp_activity);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            score = save.getInt("score", 0);
        }
        user.setUser_act(play_activity);

        mBackgroundService = new BackgroundService();
        mBackgroundServiceIntent = new Intent(getApplicationContext(), mBackgroundService.getClass());
        startService(mBackgroundServiceIntent);

        act_title = (TextView)findViewById(R.id.Title);
        act_title.setText(play_activity.title);
        act_detail = (TextView)findViewById(R.id.Detail);
        act_detail.setText(play_activity.content);

        reloadButton = (Button)findViewById(R.id.Reload);
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Reload reload = new Reload(card_selected.this);
               Intent reload_intent = new Intent(getApplicationContext(), CardInfo.class);
               reload.clicked_reload(reload_num);
            }
        });


        map_intent = new Intent(getApplicationContext(), map.class);
        map_intent.putExtra("title",play_activity.title);

        start = (Button)findViewById(R.id.Start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.remove("reload");
                if (play_activity.outside == 'n') {
                    // 실내 활동이라면
                    editor.putInt("score",0);
                    Toast.makeText(card_selected.this, "실내활동 입니다.\n리뷰로만 점수를 얻을 수 있습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    // 외부 활동이라면
                    user.setUser_location(current_location.get_current_location());
                    if (Math.abs(user.getUser_location().latitude - play_activity.latitude) < 0.0005 &&//약50m
                            Math.abs(user.getUser_location().longitude - play_activity.longitude) < 0.0005) {
                        //외부 활동 완료, 리뷰시 활동 점수 추가!!
                    }
                    else {
                        Toast.makeText(card_selected.this, "활동 장소에 도착한 후 눌러주세요", Toast.LENGTH_SHORT).show();
                    }
                }
                editor.apply();
                startActivity(new Intent(getApplicationContext(), createreview.class));
                finish();
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
            double lat = Math.pow(temp_activity.latitude - user.getUser_location().latitude, 2);
            double lon = Math.pow(temp_activity.longitude - user.getUser_location().longitude, 2);
            temp_activity.score = (float) Math.sqrt(lat + lon);;
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
        startActivity(map_intent);
    }

    private BackPressHandler backPressHandler = new BackPressHandler(this);

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressHandler.onBackPressed();
    }
}


