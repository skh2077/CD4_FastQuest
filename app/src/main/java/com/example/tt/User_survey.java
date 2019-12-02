package com.example.tt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.tt.data.User;

import org.json.JSONException;
import org.json.JSONObject;

public class User_survey extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    SeekBar seekBar1;
    SeekBar seekBar2;
    SeekBar seekBar3;
    ImageButton backButton;
    Button nextButton;
    int seekBar1_score, seekBar2_score, seekBar3_score;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersurvey);

        user = User.getInstance();

        // initiate  views
        seekBar1 = (SeekBar) findViewById(R.id.seekBar1);
        seekBar2 = (SeekBar) findViewById(R.id.seekBar2);
        seekBar3 = (SeekBar) findViewById(R.id.seekBar3);

        seekBar1.setOnSeekBarChangeListener(this);
        seekBar2.setOnSeekBarChangeListener(this);
        seekBar3.setOnSeekBarChangeListener(this);



        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        nextButton = findViewById(R.id.next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp =(seekBar1_score + seekBar2_score) / 2;
                user.setActivity(temp);
                //user.setSociality(seekBar3_score);

                Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        finish();
                        startActivity(new Intent(getApplicationContext(),pre_cat.class));
                    }
                };//Response.Listener 완료

                //Volley 라이브러리를 이용해서 실제 서버와 통신을 구현하는 부분
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("activity", (int)user.getActivity());
                    //jsonObject.put("activity", 31);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String URL = "http://52.79.125.108/api/detail/" + user.getUser_id();

                profileRequest act_score_Request = new profileRequest(Request.Method.PUT, URL ,jsonObject, responseListener, null);
                RequestQueue queue = Volley.newRequestQueue(User_survey.this);

                queue.add(act_score_Request);
            }
        });
    }


    int value;

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        value = progress;


        switch (seekBar.getId()) {
            case R.id.seekBar1:

                break;

            case R.id.seekBar2:

                break;
            case R.id.seekBar3:

                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        switch (seekBar.getId()) {
            case R.id.seekBar1:
                seekBar1_score=value;
                break;

            case R.id.seekBar2:
                seekBar2_score=value;
                break;
            case R.id.seekBar3:
                seekBar3_score=value;
                break;
        }
        Toast.makeText(User_survey.this, "Seek bar progress is :" + value,
                Toast.LENGTH_SHORT).show();
    }


}
