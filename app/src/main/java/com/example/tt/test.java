package com.example.tt;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
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
import retrofit2.Response;

public class test extends AppCompatActivity {
    FileService fileService;
    Button btnChooseFile;
    Button btnUpload;
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        btnChooseFile = (Button) findViewById(R.id.btnChooseFile);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        fileService = APIUtils.getFileService();

        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File image = new File(imagePath);
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), image);
                MultipartBody.Part body = MultipartBody.Part.createFormData("image", image.getName(), requestBody);

                Call<FileINfo> call = fileService.upload("2", body);
                call.enqueue(new Callback<FileINfo>() {
                    @Override
                    public void onResponse(Call<FileINfo> call, Response<FileINfo> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(test.this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<FileINfo> call, Throwable t) {
                        Toast.makeText(test.this, "ERROR: " + t.getMessage(), Toast.LENGTH_SHORT).show();;

                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(data == null){
                Toast.makeText(this, "Unable to choose image!", Toast.LENGTH_SHORT).show();;
                return;
            }

            Uri imageURI = data.getData();
            imagePath = getRealPathFromUri(imageURI);

        }
    }

    private String getRealPathFromUri(Uri uri){
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_idx);
        cursor.close();
        return result;
    }

    void edit_score(String user_id, int score, final Context context) {
        // 수정하면 유저 id 받으면 통신하는게 완성 됨
        com.android.volley.Response.Listener<JSONObject> pjresponseListener = new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(context, "10점이 적립되었습니다.", Toast.LENGTH_LONG);

            }
        };
        JSONObject pointj = new JSONObject();
        try {
            pointj.put("score", (int)score);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String URL = "http://52.79.125.108/users/" + user_id;
        addpointRequest preq = new addpointRequest(Request.Method.PUT, pointj, URL, pjresponseListener, null);
        RequestQueue pjqueue = Volley.newRequestQueue(context);
        pjqueue.add(preq);
    }



}