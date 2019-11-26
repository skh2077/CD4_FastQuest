package com.example.tt;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.tt.data.User;

import org.json.JSONObject;

public class Login extends AppCompatActivity {

    Button login;
    EditText username;
    EditText password;
    private AlertDialog dialog;
    private String userID;
    private String userPassword;
    User user;
    static SharedPreferences save;
    static SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        login= (Button)findViewById(R.id.login);


        save = getSharedPreferences("mysave", MODE_PRIVATE);
        editor = save.edit();
        final String myid = save.getString("id","");
        final String mypassword = save.getString("password","");
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
                            user = User.getInstance();
                            user.setUser_id(jsonuser.get("id").toString());
                            user.setUsername(jsonuser.get("username").toString());
                            user.setEmail(jsonuser.get("email").toString());
                            user.setNickname(jsonuser.get("nickname").toString());
                            //user.setScore(Integer.parseInt(jsonuser.get("score").toString()));
                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                            dialog = builder.setMessage("success Login")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
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
                }
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        dialog = builder.setMessage("success Login")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        editor.putString("id", userID);
                                        editor.putString("password", userPassword);
                                        editor.apply();
                                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
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

}
