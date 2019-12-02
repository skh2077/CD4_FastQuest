package com.example.tt;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.tt.data.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;

public class Login extends AppCompatActivity {
    //android:value="AIzaSyB_RuFDEAjgz-R4NvYbA13tcvHSKbm999Q" /> debug key
    //android:value="AIzaSyAYMsaas3FYMMpkeo2YC2Zug6bsiwFHM1E" />release key
    Button login;
    EditText username;
    EditText password;
    private AlertDialog dialog;
    private String userID;
    private String userPassword;
    User user;
    LatLng correct_cur_loc;
    static SharedPreferences save;
    static SharedPreferences.Editor editor;
    boolean mycheck_pre;
    private FusedLocationProviderClient mfusedLocationProviderClient;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user = User.getInstance();
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        login= (Button)findViewById(R.id.login);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
            return;
        }

        mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mfusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    correct_cur_loc = new LatLng(location.getLatitude(), location.getLongitude());
                    user.setUser_location(correct_cur_loc);
                } else {
                    Toast.makeText(getApplicationContext(), "권한 체크 거부 됌", Toast.LENGTH_SHORT).show();
                    LatLng loc_temp = new LatLng(0, 0);
                    user.setUser_location(loc_temp);
                }
            }
        });

        save = getSharedPreferences("mysave", MODE_PRIVATE);
        editor = save.edit();
        //editor.remove("page");
        editor.apply();
        final String myid = save.getString("id","");
        final String mypassword = save.getString("password","");
         mycheck_pre= save.getBoolean("check_pre",false);
        if(!myid.equals("") && !mypassword.equals("")) {
            Response.Listener<String> responseListener = new Response.Listener<String>(){
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        try {
                            username.setText(myid);
                            password.setText(mypassword);
                            String success = jsonResponse.get("token").toString();
                            JSONObject jsonuser = new JSONObject(jsonResponse.get("user").toString());
                            user.setUser_id(jsonuser.get("id").toString());
                            user.setUsername(jsonuser.get("username").toString());
                            user.setEmail(jsonuser.get("email").toString());
                            user.setNickname(jsonuser.get("nickname").toString());
                            user.setScore(Integer.parseInt(jsonuser.get("score").toString()));
                            user.setActivity(Float.parseFloat(jsonuser.get("activity").toString()));
                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                            dialog = builder.setMessage("success Login")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                            finish();
                                        }
                                    })
                                    .create();
                            dialog.show();
                        } catch (Exception e) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                            dialog = builder.setMessage("이게 실패하면 안되는데")
                                    .setNegativeButton("OK", null)
                                    .create();
                            dialog.show();
                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
;                }
            };//Response.Listener 완료

            //Volley 라이브러리를 이용해서 실제 서버와 통신을 구현하는 부분
            loginRequest login_Request = new loginRequest(myid, mypassword, responseListener);
            RequestQueue queue = Volley.newRequestQueue(Login.this);

            queue.add(login_Request);
        }
        //버튼이 눌리면 RegisterActivity로 가게함
    }

    public void login_button(View view) {
        userID = username.getText().toString();
        userPassword = password.getText().toString();
        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    try {
                        String success = jsonResponse.get("token").toString();
                        JSONObject jsonuser = new JSONObject(jsonResponse.get("user").toString());
                        user = User.getInstance();
                        user.setUser_id(jsonuser.get("id").toString());
                        user.setUsername(jsonuser.get("username").toString());
                        user.setEmail(jsonuser.get("email").toString());
                        user.setNickname(jsonuser.get("nickname").toString());
                        //user.setScore(Integer.parseInt(jsonuser.get("score").toString()));
                        user.setScore(Integer.parseInt(jsonuser.get("score").toString()));
                        user.setActivity(Float.parseFloat(jsonuser.get("activity").toString()));
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        dialog = builder.setMessage("success Login")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        editor.putString("id", userID);
                                        editor.putString("password", userPassword);
                                        editor.apply();
                                        if(mycheck_pre == false){
                                            editor.putBoolean("check_pre", true);
                                            editor.apply();
                                            startActivity(new Intent(getApplicationContext(), User_survey.class));
                                        }
                                        else {
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        }
                                        finish();
                                    }
                                })
                                .create();
                        dialog.show();
                    } catch (Exception e) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        dialog = builder.setMessage("Login fail")
                                .setNegativeButton("OK", null)
                                .create();
                        dialog.show();
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        };//Response.Listener 완료

        //Volley 라이브러리를 이용해서 실제 서버와 통신을 구현하는 부분
        loginRequest login_Request = new loginRequest(userID, userPassword, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Login.this);

        queue.add(login_Request);

    }
    public void move_register(View view) {
        startActivity(new Intent(getApplicationContext(), Register_Activity.class));
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
