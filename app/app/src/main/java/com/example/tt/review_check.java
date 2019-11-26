package com.example.tt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.tt.data.User;
import com.example.tt.model.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class review_check extends AppCompatActivity {
    ImageButton backButton;
    RecyclerAdapter adapter;
    String url;
    private JSONObject cat_json = null;
    private  JSONArray cat_arr = null;
    final url_json read = new url_json();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_check);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        init();
        getData();
    }

    private void init() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void getData() {

        //그냥 이미지 샘플 입력용

        List<Integer> listResId = Arrays.asList(
                R.drawable.pre_art,
                R.drawable.pre_book,
                R.drawable.pre_camera,
                R.drawable.pre_dance,
                R.drawable.pre_food,
                R.drawable.pre_game,
                R.drawable.pre_language,
                R.drawable.pre_meet,
                R.drawable.pre_travel,
                R.drawable.pre_volunteer,
                R.drawable.pre_art,
                R.drawable.ic_person_black_24dp,
                R.drawable.pre_music,
                R.drawable.pre_movie,
                R.drawable.pre_music,
                R.drawable.pre_book
        );

        User user = User.getInstance();

        url = "http://52.79.125.108/api/feed/user" + "/rbqbwbrb";// user.getUser_id()
        try {
            cat_json = read.readJsonFromUrl(url);
            cat_arr = new JSONArray(cat_json.get("temp").toString());


            for (int i = 0; i < cat_arr.length(); i++) {
                JSONObject temp = (JSONObject) cat_arr.get(i);
                Data data = new Data();
                data.setTitle(temp.get("title").toString());
                data.setContent(temp.get("content").toString());
                data.setAuthor(temp.get("nickname").toString());
                // data.setResId(temp.get("image");
                // data.setResId(listResId.get(i));
                data.setUrlImage("http://52.79.125.108/" + temp.get("image").toString());
                adapter.addItem(data);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();

    }
}
