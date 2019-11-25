package com.example.tt;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


public class Register_Activity extends AppCompatActivity {

    private String userGender;
    private AlertDialog dialog;
    private boolean validate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText idText = (EditText)findViewById(R.id.idText);
        final EditText passwordText = (EditText)findViewById(R.id.passwordText);
        final EditText passwordConfirm = (EditText)findViewById(R.id.passwordText2);
        final EditText age = (EditText)findViewById(R.id.age);
        final EditText emailText = (EditText)findViewById(R.id.emailText);


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

        //회원가입시 아이디가 사용가능한지 검증하는 부분
        final Button validateButton = (Button)findViewById(R.id.validateButton);
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = idText.getText().toString();
                if(validate){
                    Log.d("tag", "true");
                    return;//검증 완료
                }
                //ID 값을 입력하지 않았다면
                if(userID.equals("")){
                    Log.d("tag", "blank");
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register_Activity.this);
                    dialog = builder.setMessage("ID is empty")
                            .setPositiveButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                }


                //검증시작
                Response.Listener<String> responseListener = new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        try{
                            if(!response.contains("\"" + idText.getText().toString() + "\"")){//사용할 수 있는 아이디라면
                                AlertDialog.Builder builder = new AlertDialog.Builder(Register_Activity.this);
                                dialog = builder.setMessage("you can use ID")
                                        .setPositiveButton("OK", null)
                                        .create();
                                dialog.show();
                                idText.setEnabled(false);//아이디값을 바꿀 수 없도록 함
                                validate = true;//검증완료
                                idText.setBackgroundColor(0x8C8C8C);
                                validateButton.setBackgroundColor(0x8C8C8C);
                            }else{//사용할 수 없는 아이디라면
                                AlertDialog.Builder builder = new AlertDialog.Builder(Register_Activity.this);
                                dialog = builder.setMessage("alreay used ID")
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
                ValidateRequest validateRequest = new ValidateRequest(userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Register_Activity.this);
                queue.add(validateRequest);
                Log.d("tag", "끝까지는 가나?");
            }
        });


        //회원 가입 버튼이 눌렸을때
        Button registerButton = (Button)findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = idText.getText().toString();
                String userPassword = passwordText.getText().toString();
                String userPasswordConfirm = passwordConfirm.getText().toString();
                String userage = age.getText().toString();
                String userEmail = emailText.getText().toString();

                if(!userPassword.equals(userPasswordConfirm)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register_Activity.this);
                    dialog = builder.setMessage("confirm Password")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                }

                //한칸이라도 빠뜨렸을 경우
                if(userID.equals("")||userPassword.equals("")||userage.equals("")||userEmail.equals("")||userGender.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register_Activity.this);
                    dialog = builder.setMessage("Empty text exist")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                }

                //회원가입 시작
                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            try {
                                String success = jsonResponse.get("token").toString();
                                AlertDialog.Builder builder = new AlertDialog.Builder(Register_Activity.this);
                                dialog = builder.setMessage("Register Your ID")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        })
                                        .create();
                                dialog.show();
                            } catch (Exception e) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Register_Activity.this);
                                dialog = builder.setMessage("Register fail")
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
                RegisterRequest registerRequest = new RegisterRequest(userID, userPassword, userage, userGender, userEmail, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Register_Activity.this);

                queue.add(registerRequest);


            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }

}
