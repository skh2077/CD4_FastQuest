package com.example.tt;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;


public class createreview extends AppCompatActivity {
    private final int CAMERA_CODE = 0;
    private final int GALLERY_CODE = 1;

    DialogInterface.OnClickListener camerListener;
    DialogInterface.OnClickListener albumListener;
    DialogInterface.OnClickListener cancleListener;

    User user = User.getInstance();

    Button addImage;
    ImageView image;
    Bitmap photo;
    EditText review_title;
    EditText review_content;

    String act_id;

    private Uri photoUri;
    private String currentPhotoPath;//실제 사진 파일 경로
    String mImageCaptureName;//이미지 이름

    String imagePath;
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
        if(image_file == null) {
            new AlertDialog.Builder(this).setTitle("이미지 없습니다.").setPositiveButton("OK", null).show();
            return;
        }
        Intent idintent = getIntent();
        point = idintent.getFloatExtra("save_score",0);
        editor.putInt("save_score", (int)point);
        editor.apply();
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
                                    pointj.put("score", (int)point);
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
        //jsonObject.put("act", Integer.parseInt(actid));
        jsonObject.put("act", act_id);
        jsonObject.put("author", Integer.parseInt(user.getUser_id().toString()));
        //jsonObject.put("author", 3);
        String nickname = user.getNickname();
        jsonObject.put("nickname", nickname);
        //jsonObject.put("nickname", "test_nickname");
        String username = user.getUsername();
        jsonObject.put("username", username);
        //jsonObject.put("username", "test_username");
        //jsonObject.put("image", "dumy");


        //ReviewRequest reviewRequest = new ReviewRequest(review_title.getText().toString(),review_content.getText().toString(), photostring, responseListener);
        ReviewRequest reviewRequest = new ReviewRequest(Request.Method.POST, jsonObject, responseListener, null);
        RequestQueue queue = Volley.newRequestQueue(createreview.this);

        queue.add(reviewRequest);
    }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_review);
        fileService = APIUtils.getFileService();
        addImage = (Button)findViewById(R.id.add_image);
        image = (ImageView)findViewById(R.id.input_image);
        review_title = (EditText)findViewById(R.id.input_reviw_title);
        review_content = (EditText)findViewById(R.id.input_reviw_content);

        save = getSharedPreferences("mysave", MODE_PRIVATE);
        editor = save.edit();

        str_act = save.getString("activity","");
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
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });


    }

    public void takePhoto() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                    image_file = photoFile;
                } catch (IOException ex) {

                }
                if (photoFile != null) {
                    photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(intent, CAMERA_CODE);
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        File dir = new File(Environment.getExternalStorageDirectory() + "/path/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mImageCaptureName = timeStamp + ".jpg";
        File storageDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()
                + "/path/" + mImageCaptureName);
        currentPhotoPath = storageDir.getAbsolutePath();
        return storageDir;
    }

    private void getPictureForPhoto() {
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(currentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation;
        int exifDegree;
        if (exif != null) {
            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            exifDegree = exifOrientationToDegrees(exifOrientation);
        } else {
            exifDegree = 0;
        }
        image.setImageBitmap(rotate(bitmap, exifDegree));//이미지 뷰에 비트맵 넣기 }
        photo = bitmap;
    }



    private void selectGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_CODE);
    }


    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case GALLERY_CODE:
                sendPicture(data.getData());
                break;
            case CAMERA_CODE:
                getPictureForPhoto();
                break;

            default:
                break;

        }
    }

    private void sendPicture(Uri imgUri) {
        imagePath = getRealPathFromURI(imgUri); // path 경로
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegrees(exifOrientation);

        File tempfile = new File(imagePath);
        image_file = tempfile;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
        image.setImageBitmap(rotate(bitmap, exifDegree));//이미지 뷰에 비트맵 넣기
        photo = bitmap;
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap src, float degree) { // Matrix 객체 생성
        Matrix matrix = new Matrix(); // 회전 각도 셋팅
        matrix.postRotate(degree); // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }
    private String getRealPathFromURI(Uri contentUri) {
        int column_index=0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }
        return cursor.getString(column_index);}



    public void add_image(View view) {
        camerListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(ActivityCompat.checkSelfPermission(createreview.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(createreview.this, new String[] {Manifest.permission.CAMERA}, 1003);
                }
                else {
                    if (ActivityCompat.checkSelfPermission(createreview.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(createreview.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(createreview.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1002);
                    }
                }
                takePhoto();
            }
        };
        albumListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(ActivityCompat.checkSelfPermission(createreview.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(createreview.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(createreview.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1002);
                }
                selectGallery();
            }
        };
        cancleListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        new AlertDialog.Builder(this).setTitle("업로드할 이미지 선택").setPositiveButton("앨범 선택", albumListener).setNeutralButton("사진 촬영", camerListener).setNegativeButton("취소", cancleListener).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1002:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "저장소 권한 체크 거부", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case 1003:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "카메라 권한 체크 거부", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "저장소 권한 체크 거부", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                break;
        }
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
