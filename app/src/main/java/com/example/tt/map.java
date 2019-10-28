package com.example.tt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class map extends AppCompatActivity implements OnMapReadyCallback {
    private FragmentManager fragmentManager;
    private MapFragment mapFragment;
    private FusedLocationProviderClient mfusedLocationProviderClient;
    public GoogleMap gmap;
    Marker startMarker;
    Marker goalMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        fragmentManager = getFragmentManager();
        mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);
        mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        //Location cur_location = mfusedLocationProviderClient.getLastLocation().getResult();
        //LatLng location = new LatLng(cur_location.getLatitude(), cur_location.getLongitude()); //현재위치
        double start_lat = 37.50640;
        double start_lng = 126.958563;
        LatLng location = new LatLng(start_lat, start_lng); //중앙대정문
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("출발지");
        markerOptions.snippet("여기서부터");
        markerOptions.position(location);
        startMarker = googleMap.addMarker((markerOptions));
        double goal_lat = 37.506840;
        double goal_lng = 126.953;
        LatLng goal = new LatLng(goal_lat, goal_lng); //목적지

        MarkerOptions goalmarkeroption = new MarkerOptions();
        goalmarkeroption.title("목적지");
        goalmarkeroption.snippet("여기까지");
        goalmarkeroption.position(goal);
        goalMarker = googleMap.addMarker((goalmarkeroption));

        LatLng camera = new LatLng((start_lat + goal_lat)/2, (start_lng + goal_lng) / 2);

        double zoom = 20 - (Math.log(Math.max(Math.abs(start_lat - goal_lat), Math.abs(start_lng - goal_lng)) / 0.00035) / Math.log(2));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(camera, (float)zoom));





    }


    public void currentLocation(View view) {
        Button curpo = (Button)findViewById(R.id.markerbutton);
        curpo.setBackgroundColor(Color.RED);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
            return;
        }
        mfusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null) {
                    LatLng mycurloc =  new LatLng(location.getLatitude(), location.getLongitude());
                    startMarker.setPosition(mycurloc);
                    startMarker.setTitle("현재 위치 : " + location.getLatitude() + ", "  + location.getLongitude());
                    LatLng camera = new LatLng((startMarker.getPosition().latitude + goalMarker.getPosition().latitude)/2, (startMarker.getPosition().longitude + goalMarker.getPosition().longitude) / 2);
                    double zoom = 20 - (Math.log(Math.max(Math.abs(startMarker.getPosition().latitude - goalMarker.getPosition().latitude), Math.abs(startMarker.getPosition().longitude - goalMarker.getPosition().longitude)) / 0.00035) / Math.log(2));
                    gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(camera, (float)zoom));
                }
            }
        });

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
                                LatLng mycurloc =  new LatLng(location.getLatitude(), location.getLongitude());
                                startMarker.setPosition(mycurloc);
                                startMarker.setTitle("현재 위치 : " + location.getLatitude() + ", "  + location.getLongitude());
                                LatLng camera = new LatLng((startMarker.getPosition().latitude + goalMarker.getPosition().latitude)/2, (startMarker.getPosition().longitude + goalMarker.getPosition().longitude) / 2);
                                double zoom = 20 - (Math.log(Math.max(Math.abs(startMarker.getPosition().latitude - goalMarker.getPosition().latitude), Math.abs(startMarker.getPosition().longitude - goalMarker.getPosition().longitude)) / 0.00035) / Math.log(2));
                                gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(camera, (float)zoom));
                            }
                        }
                    });
                }
        }
    }
}
