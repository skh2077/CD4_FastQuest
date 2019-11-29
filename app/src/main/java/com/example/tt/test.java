package com.example.tt;

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

import com.example.tt.model.FileINfo;
import com.example.tt.remote.APIUtils;
import com.example.tt.remote.FileService;

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

                Call<FileINfo> call = fileService.upload(body);
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
}
