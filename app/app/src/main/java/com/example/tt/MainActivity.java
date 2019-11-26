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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    ImageButton StartButton;
    ImageButton ProfileButton;
    User user;
    private FusedLocationProviderClient mfusedLocationProviderClient;
    Switch moim_switch;
    static SharedPreferences save;
    static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        save = getSharedPreferences("mysave", MODE_PRIVATE);
        editor = save.edit();

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
        user = User.getInstance();

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view_drawer);
        navigationView.setNavigationItemSelectedListener(this);

        moim_switch = findViewById(R.id.switch_moim);

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
                startActivity(new Intent(getApplicationContext(), CardInfo.class));
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
