package com.example.tt;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

public class profile_detail extends AppCompatActivity {

    Button saveButton;
    ImageButton backButton;
    TextInputEditText name_tt;
    TextInputEditText nickname_tt;
    TextInputEditText birth_tt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);
        name_tt = findViewById(R.id.nameinput);
        nickname_tt = findViewById(R.id.nicknameinput);
        birth_tt = findViewById(R.id.ageinput);

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(), "저장됨", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),profile_detail.class));
                    }
                };//Response.Listener 완료

                //Volley 라이브러리를 이용해서 실제 서버와 통신을 구현하는 부분

                profileRequest profile_Request = new profileRequest(name_tt.getText().toString(), nickname_tt.getText().toString(), birth_tt.getText().toString(), responseListener);
                RequestQueue queue = Volley.newRequestQueue(profile_detail.this);

                queue.add(profile_Request);
            }
        });
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }
}
