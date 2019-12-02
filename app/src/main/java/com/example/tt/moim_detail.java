package com.example.tt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class moim_detail extends AppCompatActivity {

    Button attend_moim;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moim_detail);
        attend_moim = (Button)findViewById(R.id.attend_moim);
        getIncomingIntent();
    }

    private void getIncomingIntent() {

        if (getIntent().hasExtra("image") && getIntent().hasExtra("content")) {

            String imageUrl = getIntent().getStringExtra("image");
            title = getIntent().getStringExtra("title");
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

    public void Attend_moim(View view){
        Intent intent = new Intent(getApplicationContext(), chat.class);
        intent.putExtra("room_name", title + getIntent().getStringExtra("id"));
        startActivity(intent);
    }

}
