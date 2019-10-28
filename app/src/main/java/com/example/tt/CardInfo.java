package com.example.tt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.Marker;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Random;
import java.util.Vector;

public class CardInfo extends AppCompatActivity {
    Button card1,card2,card3,card4,card5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User user = new User(20150101, "nh7881@naver.com", "South", 777, 20, 15, 0, 23, 19970219, 0);
        Vector<String> vprecat = new Vector<String>();
        simple_pre_cat pre_cat1 = new simple_pre_cat(20150101, "헬스");
        simple_pre_cat pre_cat2 = new simple_pre_cat(20150101, "볼링");
        vprecat.add(pre_cat1.getCat_name());
        vprecat.add(pre_cat2.getCat_name());
        //추천 알고리즘 옹작할 것!
        Vector<category> value = new Vector<category>();
        try {
            value = recom(user, vprecat);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_card_info);
        card1 = (Button)findViewById(R.id.card1);
        card_set(value.get(0).cat_name, (int)(100-value.get(0).score), card1);
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),card_selected.class));
            }
        });
        card2 = (Button)findViewById(R.id.card2);
        card_set(value.get(1).cat_name, (int)(100-value.get(1).score), card2);
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),card_selected.class));
            }
        });
        card3 = (Button)findViewById(R.id.card3);
        card_set(value.get(2).cat_name, (int)(100-value.get(2).score), card3);
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),card_selected.class));
            }
        });
        card4 = (Button)findViewById(R.id.card4);
        card_set(value.get(3).cat_name, (int)(100-value.get(3).score), card4);
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),card_selected.class));
            }
        });
        card5 = (Button)findViewById(R.id.card5);
        card_set(value.get(4).cat_name, (int)(100-value.get(4).score), card5);
        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),card_selected.class));
            }
        });

    }


    public Vector<category> recom(User user, Vector<String> vprecat) throws IOException, JSONException {
        Vector<String> catlist = new Vector<String>();
        Vector<category> precat = new Vector<category>();
        float act = 0;
        float max_act =0;
        float soc = 0;
        float max_soc = 0;
        JSONObject jsonCat = null;
        String handle;
        jsonCat = readJsonFromUrl("http://52.79.125.108/api/category/");
        // jsoncat값을 category안에 ㄱㄱ
        JSONArray carr = new JSONArray(jsonCat.get("temp").toString());
        Vector<category> vcat = new Vector<category>();
        for(int i= 0; i<carr.length(); i++) {
            JSONObject temp = (JSONObject) carr.get(i);
            category tcat = new category();
            tcat.cat_name = temp.get("cat_name").toString();
            tcat.Lcat_name = temp.get("lcat_name").toString();
            handle = temp.get("activity_rate").toString();
            if (handle == "null") {
                tcat.activity_rate = 0;
            } else {
                tcat.activity_rate = Integer.parseInt(handle);
            }
            handle = temp.get("sociality_rate").toString();
            if (handle == "null") {
                tcat.sociality_rate = 0;
            } else {
                tcat.sociality_rate = Integer.parseInt(handle);
            }

            if(vprecat.get(0).equals(tcat.cat_name) || vprecat.get(1).equals(tcat.cat_name)) {

                if(max_act < Math.abs(user.getActivity() - tcat.activity_rate)) {
                    max_act = Math.abs(user.getActivity() - tcat.activity_rate);
                    act = tcat.activity_rate;
                }

                if(max_soc < Math.abs(user.getSociality() - tcat.sociality_rate)) {
                    max_soc = Math.abs(user.getSociality() - tcat.sociality_rate);
                    soc = tcat.sociality_rate;
                }
                precat.add(tcat);
            }
            vcat.add(tcat);
        }

        for(int k = 0; k<vcat.size(); k ++) {
            float sco = 100 - (Math.abs(soc - vcat.get(k).sociality_rate) + Math.abs(act - vcat.get(k).activity_rate));
            vcat.get(k).score = sco;

        }

        JSONObject jsonObject = null;
        String url = "http://52.79.125.108/api/activity/" +
                (int)Math.min(user.getSociality(), soc) +"/"+ (int)Math.max(user.getSociality(), soc) +"/"+
                (int)Math.min(user.getActivity(), act) +"/"+ (int)Math.max(user.getActivity(), act) +"/outside/solo";
        jsonObject = readJsonFromUrl(url);
        JSONArray jarr = new JSONArray(jsonObject.get("temp").toString());
        Vector<Activity> activities = new Vector<Activity>();

        for(int i= 0; i<jarr.length(); i++) {
            JSONObject temp = (JSONObject)jarr.get(i);
            Activity tact = new Activity();
            tact.act_id = Integer.parseInt(temp.get("act_id").toString());
            tact.title = temp.get("title").toString();
            tact.category = temp.get("category").toString();
            tact.content = temp.get("content").toString();
            tact.longterm = temp.get("longterm").toString().charAt(0);
            tact.outside = temp.get("outside").toString().charAt(0);
            tact.address = temp.get("address").toString();
            tact.latitude = Double.parseDouble(temp.get("latitude").toString());
            tact.longitude = Double.parseDouble(temp.get("longitude").toString());
            if(catlist.contains(tact.category) != true) {
                catlist.add(tact.category);
            }
            activities.add(tact);
        }
        Collections.sort(vcat, new MyComparator());
        Vector<category> recommend = new Vector<category>();
        int size = vcat.size();
        for(int j =0; j<size;j++) {
            if(catlist.contains(vcat.get(j).cat_name) == true) {
                recommend.add(vcat.get(j));
                vcat.remove(j);
                j = j-1;
                size = size -1;
            }
        }
        Vector<category> value = new Vector<category>();
        int num =  (int)(5 - user.score * 0.001);
        for(int n = 0; n<num; n++) {
            if(recommend.size() == 0) {break;}
            Random random = new Random();
            int seed = random.nextInt(recommend.size());
            value.add(recommend.get(seed));
            recommend.remove(seed);
        }
        for(int k = 0; k<5-num; k++) {
            if(vcat.size() == 0) {break;}
            value.add(vcat.get(Math.min(k, vcat.size()-1)));
            vcat.remove(Math.min(k, vcat.size()-1));
        }
        if(value.size() < 5) {
            while(value.size() < 5) {
                value.add(vcat.get(0));
                vcat.remove(0);
            }
        }

        return value;



        //유저의 카테고리 받고 <-- 임의 값
        // 유저의 카테고리와 같은 대분류를 가진 카테고리들 갖고 오기
        // query user category = category
        // 유저의 안밖 받고
        // query user in_out = in_out
        // 유저의 안밖과 같은 안밖을 가진 활동을 첫번 째 대분류에서 다 뽑는다.
        // 유저의 사교성과 활동성을 받고
        // 유저의 사교성과 활동성과 카테고리의 사교성 활동성 사이의 범위를 가진 활동들을 뽑는다.
        //  query max(유저 사교성,  카테고리 사교성) > activity && min(유저 사교성, 카테고리 사교성)
        //  랜덤으로 5 - 사용자의 점수 * 0.0001개만 받는다. 이 때 받은 값을 int로 받음으로써 소수 자리 값은 버린다.
        // 받은 결과와 활동성과 사교성을 비교하여 점수를 뽑고 색깔에 반영한다.
        // 나머지 수 만큼 (자신의 사교성과 활동성 바깥) <> (사용자의 점수 * 0.0003 + 자신의 사교성과 활동성 바깥)의 법위를 가진 활동을 뽑는다.
        // 원하는 수만큼 뽑으면 끝내고 색깔에 반영한다.
        // 해당하는 결과가 없다면 범위를 1늘려서 반복한다.
    }
    private String jsonReadAll(Reader reader) throws IOException {

        StringBuilder sb = new StringBuilder();

        int cp;

        while ((cp = reader.read()) != -1) {

            sb.append((char) cp);

        }
        String temp = sb.toString();

        return temp;

    }

    private JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        URLConnection t_connection = new URL(url).openConnection();
        InputStream is = t_connection.getInputStream();
        try {

            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

            String jsonText = jsonReadAll(rd);

            JSONObject json = new JSONObject();
            json.put("temp",jsonText);

            return json;

        } finally {

            is.close();

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
    class MyComparator implements Comparator<category> {
        @Override
        public int compare(category c1, category c2) {
            if (c1.score < c2.score) {
                return 1;
            }
            return -1;
        }
    }
}
