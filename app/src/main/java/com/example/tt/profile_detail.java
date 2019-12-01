package com.example.tt;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tt.data.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;

public class profile_detail extends AppCompatActivity {

    Button saveButton;
    ImageButton backButton;
    TextInputLayout nickname_tt;
    TextInputLayout birth_tt;
    User user;
    String userGender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);
        nickname_tt = findViewById(R.id.nicknameinput);
        birth_tt = findViewById(R.id.ageinput);
        user = User.getInstance();

        RadioGroup genderGroup = (RadioGroup)findViewById(R.id.genderGroup);
        int genderGroupID = genderGroup.getCheckedRadioButtonId();
        userGender = ((RadioButton)findViewById(genderGroupID)).getText().toString();//초기화 값을 지정해줌

        //라디오버튼이 눌리면 값을 바꿔주는 부분
        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                RadioButton genderButton = (RadioButton)findViewById(i);
                userGender = genderButton.getText().toString();
            }
        });

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                            Toast.makeText(getApplicationContext(), "저장됨", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),profile_detail.class));
                    }
                };//Response.Listener 완료

                //Volley 라이브러리를 이용해서 실제 서버와 통신을 구현하는 부분
                String url = "http://52.79.125.108/api/detail/" + user.getUser_id();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("nickname", nickname_tt.getEditText().getText().toString());
                    jsonObject.put("gender", userGender);
                    jsonObject.put("age", Integer.parseInt(birth_tt.getEditText().getText().toString()));
                    jsonObject.put("activity", 31);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String URL = "http://52.79.125.108/api/detail/" + user.getUser_id();

                profileRequest profile_Request = new profileRequest(Request.Method.PUT, URL ,jsonObject, responseListener, null);
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
