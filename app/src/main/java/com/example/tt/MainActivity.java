package com.example.tt;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.tt.data.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    ImageButton StartButton;
    ImageButton ProfileButton;
    User user;
    private FusedLocationProviderClient mfusedLocationProviderClient;
    Switch moim_switch;
    Switch inside_switch;
    static SharedPreferences save;
    static SharedPreferences.Editor editor;
    TextView nick_and_username;
    TextView small_view_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        save = getSharedPreferences("mysave", MODE_PRIVATE);
        editor = save.edit();

        editor.remove("activity");
        editor.remove("page");
        editor.remove("reload");
        editor.apply();

        user = User.getInstance();

        JSONObject user_info;
        url_json read = new url_json();
        String user_info_url = "http://52.79.125.108/api/user/" + user.getUsername();
        try {
            user_info = read.readJsonFromUrl(user_info_url);
            JSONObject temp = new JSONObject(user_info.get("temp").toString());
            user.setNickname(temp.get("nickname").toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mfusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null) {
                    user.setUser_location(new LatLng(location.getLatitude(), location.getLongitude()));
                }
                else{
                    new AlertDialog.Builder(MainActivity.this).setMessage("현재 위치를 받을 수 없습니다.").setPositiveButton("OK",null).show();
                }
            }
        });


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view_drawer);
        navigationView.setNavigationItemSelectedListener(this);

        View nav_header_view = navigationView.getHeaderView(0);

        nick_and_username = nav_header_view.findViewById(R.id.userId);
        nick_and_username.setText(user.getNickname() + "(" + user.getUsername() + ")");
        small_view_score = nav_header_view.findViewById(R.id.small_score);
        small_view_score.setText(String.valueOf(user.getScore()));
        //small_view_score.setText("10");

        moim_switch = findViewById(R.id.switch_moim);
        inside_switch = findViewById(R.id.switch_inside);
        BottomNavigationView nav_view = findViewById(R.id.nav_view);
        Menu menu = nav_view.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        nav_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:

                        break;

                    case R.id.navigation_moim:
                        startActivity(new Intent(getApplicationContext(), moim.class));
                        break;

                    case R.id.navigation_review:
                        startActivity(new Intent(getApplicationContext(), review.class));
                        break;
                    case R.id.nav_logout:
                        editor.remove("id");
                        editor.remove("password");
                        editor.remove("check_pre");
                        editor.remove("page");
                        editor.apply();
                        Toast.makeText(MainActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_LONG);
                        startActivity(new Intent(getApplicationContext(), start.class));
                        break;
                }
                return false;
            }
        });
        StartButton = findViewById(R.id.StartButton);
        StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "GO!", Toast.LENGTH_SHORT).show();
                user.setIsmoim(moim_switch.isChecked());
                user.setIsinside(inside_switch.isChecked());
                int page_num = save.getInt("page", 0);
                switch (page_num) {
                    case 0:
                        startActivity(new Intent(getApplicationContext(), CardInfo.class));
                        break;
                    case 1:
                        startActivity(new Intent(getApplicationContext(), CardInfo.class));
                        break;
                    case 2:
                        startActivity(new Intent(getApplicationContext(), card_selected.class));
                        break;
                    case 3:
                        startActivity(new Intent(getApplicationContext(), moim_card_selected.class));
                        break;
                    case 4:
                        startActivity(new Intent(getApplicationContext(), createreview.class));
                        break;

                }
            }
        });


        ProfileButton = findViewById(R.id.profileButton);
        ProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawer.isDrawerOpen(Gravity.RIGHT)) drawer.openDrawer(Gravity.RIGHT);
                else drawer.closeDrawer(GravityCompat.END);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_pre_cat:
                startActivity(new Intent(getApplicationContext(), pre_cat.class));
                break;
            case R.id.nav_review:
                startActivity(new Intent(getApplicationContext(), review_check.class));
                break;
            case R.id.nav_profile:
                startActivity(new Intent(getApplicationContext(), profile_detail.class));
                break;
            case R.id.nav_tip:
                startActivity(new Intent(getApplicationContext(), info.class));
                break;
            case R.id.nav_logout:
                editor.remove("id");
                editor.remove("password");
                editor.apply();
                Toast.makeText(MainActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), start.class));
                break;
        }
        return true;
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
                                user.setUser_location(new LatLng(location.getLatitude(), location.getLongitude()));
                            }
                        }
                    });
                }
        }
    }
}
