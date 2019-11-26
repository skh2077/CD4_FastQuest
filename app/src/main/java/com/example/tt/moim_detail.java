package com.example.tt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class moim_detail extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moim_detail);
        getIncomingIntent();
    }

    private void getIncomingIntent() {

        if (getIntent().hasExtra("image") && getIntent().hasExtra("content")) {

            String imageUrl = getIntent().getStringExtra("image");
            String title = getIntent().getStringExtra("title");
            String author = getIntent().getStringExtra("author");
            String content = getIntent().getStringExtra("content");

            TextView Author = findViewById(R.id.author);
            Author.setText(author);
            TextView Content = findViewById(R.id.content);
            Content.setText(content);
            TextView Title = findViewById(R.id.title);
            Title.setText(title);
            ImageView image = findViewById(R.id.image);
            Picasso.get().load(imageUrl).into(image);


        }

    }

}
