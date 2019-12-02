package com.example.tt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

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
    User user = User.getInstance();
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
        //recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void getData() {

        url = "http://52.79.125.108/api/feed/user/"+user.getUsername();
        Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();
        try {
            cat_json = read.readJsonFromUrl(url);
            cat_arr = new JSONArray(cat_json.get("temp").toString());
            for (int i = 0; i < cat_arr.length(); i++) {
                JSONObject temp = (JSONObject) cat_arr.get(i);
                Data data = new Data();
                data.setTitle(temp.get("title").toString());
                data.setContent(temp.get("content").toString());
                //data.setAuthor(temp.get("nickname").toString());
                // data.setResId(temp.get("image");
                //data.setResId(listResId.get(i));
                data.setUrlImage(temp.get("image").toString());
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
