package com.example.tt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.tt.data.Activity;
import com.example.tt.data.User;
import com.example.tt.model.FileINfo;
import com.example.tt.remote.APIUtils;
import com.example.tt.remote.FileService;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;


public class createreview extends AppCompatActivity {

    User user = User.getInstance();

    Button addImage;
    ImageView image;
    EditText review_title;
    EditText review_content;
    String act_id;
    File image_file = null;
    FileService fileService;
    String upload_id;
    float point;
    static SharedPreferences save;
    static SharedPreferences.Editor editor;
    String str_act;

    public void add_review(View view) throws JSONException {

        if (review_title.getText().toString().equals("")) {
            new AlertDialog.Builder(this).setTitle("제목을 입력해주세요").setPositiveButton("OK", null).show();
            return;
        }
        if (review_content.getText().toString().equals("")) {
            new AlertDialog.Builder(this).setTitle("내용을 입력해주세요").setPositiveButton("OK", null).show();
            return;
        }
        if (image_file == null) {
            new AlertDialog.Builder(this).setTitle("이미지 없습니다.").setPositiveButton("OK", null).show();
            return;
        }
        point = save.getInt("score", 0);
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    upload_id = response.get("id").toString();
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), image_file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("image", image_file.getName(), requestBody);

                    Call<FileINfo> call = fileService.upload(upload_id, body);

                    call.enqueue(new Callback<FileINfo>() {
                        @Override
                        public void onResponse(Call<FileINfo> call, retrofit2.Response<FileINfo> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(createreview.this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
                                Toast.makeText(createreview.this, image_file.getName(), Toast.LENGTH_SHORT).show();
                                editor.remove("page");
                                editor.apply();

                                Response.Listener<JSONObject> pjresponseListener = new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Toast.makeText(createreview.this, String.valueOf(point) + "점이 적립되었습니다.", Toast.LENGTH_LONG);

                                    }
                                };
                                JSONObject pointj = new JSONObject();
                                try {
                                    pointj.put("score", (int) point);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                String URL = "http://52.79.125.108/users/" + user.getUser_id();
                                addpointRequest preq = new addpointRequest(Request.Method.PUT, pointj, URL, pjresponseListener, null);
                                RequestQueue pjqueue = Volley.newRequestQueue(createreview.this);
                                pjqueue.add(preq);
                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                        }

                        @Override
                        public void onFailure(Call<FileINfo> call, Throwable t) {
                            Toast.makeText(createreview.this, "ERROR: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };//Response.Listener 완료

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", review_title.getText().toString());
        jsonObject.put("content", review_content.getText().toString());
        jsonObject.put("act", act_id);
        jsonObject.put("author", Integer.parseInt(user.getUser_id().toString()));
        String nickname = user.getNickname();
        jsonObject.put("nickname", nickname);
        String username = user.getUsername();
        jsonObject.put("username", username);
        ReviewRequest reviewRequest = new ReviewRequest(Request.Method.POST, jsonObject, responseListener, null);
        RequestQueue queue = Volley.newRequestQueue(createreview.this);
        queue.add(reviewRequest);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_review);
        fileService = APIUtils.getFileService();
        addImage = (Button) findViewById(R.id.add_image);
        image = (ImageView) findViewById(R.id.input_image);
        review_title = (EditText) findViewById(R.id.input_reviw_title);
        review_content = (EditText) findViewById(R.id.input_reviw_content);

        save = getSharedPreferences("mysave", MODE_PRIVATE);
        editor = save.edit();

        str_act = save.getString("activity", "");
        try {
            JSONObject tem_j = new JSONObject(str_act);
            Activity play_activity = new Activity(tem_j);
            act_id = play_activity.act_id;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        editor.putInt("page", 4);
        editor.apply();

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.remove("page");
                editor.apply();
                Toast.makeText(createreview.this, String.valueOf(point) + "점이 적립되었습니다.", Toast.LENGTH_LONG);
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }

    public void add_image(View view) {
        Add_picture add_picture = new Add_picture(this, image);
        add_picture.add_photo();
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
