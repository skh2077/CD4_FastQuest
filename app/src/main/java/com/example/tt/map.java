package com.example.tt;

import androidx.appcompat.app.AppCompatActivity;


import android.app.FragmentManager;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

public class map extends AppCompatActivity implements OnMapReadyCallback {
    private FragmentManager fragmentManager;
    private MapFragment mapFragment;
    private FusedLocationProviderClient mfusedLocationProviderClient;

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

        //Location cur_location = mfusedLocationProviderClient.getLastLocation().getResult();
        //LatLng location = new LatLng(cur_location.getLatitude(), cur_location.getLongitude()); //현재위치
        double start_lat = 37.506695;
        double start_lng = 126.958356;
        LatLng location = new LatLng(start_lat, start_lng); //중앙대정문
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("출발지");
        markerOptions.snippet("여기서부터");
        markerOptions.position(location);
        googleMap.addMarker((markerOptions));

        double goal_lat = 37.50865;
        double goal_lng = 126.958350;
        LatLng goal = new LatLng(goal_lat, goal_lng); //목적지
        MarkerOptions goalmarker = new MarkerOptions();
        goalmarker.title("목적지");
        goalmarker.snippet("여기까지");
        goalmarker.position(goal);
        googleMap.addMarker((goalmarker));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17));

        // 지도 더블클릭하면 큰 지도 화면 어때?
    }


}
