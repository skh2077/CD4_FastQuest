package com.example.tt;

import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.List;

public class map extends AppCompatActivity implements OnMapReadyCallback {
    private FragmentManager fragmentManager;
    private MapFragment mapFragment;
    private FusedLocationProviderClient mfusedLocationProviderClient;
    public GoogleMap gmap;
    TextView actTextView;
    //Button playing;
    Marker startMarker;
    Marker goalMarker;
    double goal_lat;
    double goal_lng;
    LocationManager locationManager;
    String locationProvider;
    MarkerOptions startMarkeroption;
    boolean noti = false;
    Marker tempMarker;
    List<Address> list = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationProvider = locationManager.getBestProvider(new Criteria(), true);


        actTextView= (TextView)findViewById(R.id.activity_text);
        actTextView.setText(getIntent().getExtras().getString("title"));
        //playing = (Button)findViewById(R.id.playing);
        goal_lat = getIntent().getExtras().getDouble("latitude");
        goal_lng = getIntent().getExtras().getDouble("longitude");

        fragmentManager = getFragmentManager();
        mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);
        mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        /*playing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),createreview.class));
            }
        });*/


    }
    LocationListener listener = new LocationListener() {

        //상태가 바뀌었을 때

        public void onStatusChanged(String provider, int status, Bundle extras) {

            String msg = "";

            switch(status){

                case LocationProvider.OUT_OF_SERVICE :

                    Toast.makeText(map.this, "위치 정보를 이용할 수 없습니다.", Toast.LENGTH_SHORT).show();

                    break;

                case LocationProvider.TEMPORARILY_UNAVAILABLE :

                    Toast.makeText(map.this, "일시적으로 이용을 할 수 없습니다.", Toast.LENGTH_SHORT).show();

                    break;

                case LocationProvider.AVAILABLE :

                    Toast.makeText(map.this, "위치 정보가 이용 가능합니다.", Toast.LENGTH_SHORT).show();

                    break;

            }



        }

        //공급자가 공급 가능하게 되었을 때

        public void onProviderEnabled(String provider) {

            Toast.makeText(map.this, "위치 정보가 이용 가능합니다.", Toast.LENGTH_SHORT).show();

        }

        //공급자가 공급 못하게 되었을 때

        public void onProviderDisabled(String provider) {

            Toast.makeText(map.this, "위치 정보를 이용할 수 없습니다.", Toast.LENGTH_SHORT).show();

        }

        //위치 정보가 바뀌엇을때 호출되는 메소드

        public void onLocationChanged(Location location) {

            //기존의 현재위치 지우기
            if(startMarker != null) {
                startMarker.remove();
            }
            LatLng mycurloc = new LatLng(location.getLatitude(), location.getLongitude());
            startMarkeroption = new MarkerOptions();
            startMarkeroption.position(mycurloc);
            startMarkeroption.title("현재 위치");

            Geocoder geocoder = new Geocoder(map.this);
            try {
                list = geocoder.getFromLocation(mycurloc.latitude, mycurloc.longitude, 10);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (list != null) {
                if (list.size()==0) {
                    startMarkeroption.snippet("해당되는 주소 정보는 없습니다");
                } else {
                    startMarkeroption.snippet(list.get(0).getAddressLine(0));

                }
            }


            startMarker = gmap.addMarker(startMarkeroption);
            LatLng camera = new LatLng((startMarker.getPosition().latitude + goalMarker.getPosition().latitude) / 2, (startMarker.getPosition().longitude + goalMarker.getPosition().longitude) / 2);
            double zoom = 20 - (Math.log(Math.max(Math.abs(startMarker.getPosition().latitude - goalMarker.getPosition().latitude), Math.abs(startMarker.getPosition().longitude - goalMarker.getPosition().longitude)) / 0.00035) / Math.log(2));
            gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(camera, (float) zoom));

            LatLng currpos = new LatLng(location.getLatitude(), location.getLongitude());

            LatLng goalLocation = goalMarker.getPosition();

            double radius = 200; // 500m distance.

            double distance = SphericalUtil.computeDistanceBetween(currpos, goalLocation);

            if ((distance < radius) && noti == false) {
                noti = true;
                Toast.makeText(map.this, "목적지 근처 200m 이내 입니다.", Toast.LENGTH_LONG).show();
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            } else if ((distance > radius) && noti == true) {
                Toast.makeText(map.this, "목적지 근처에서 벗어났습니다.", Toast.LENGTH_LONG).show();
                noti = false;
            }

        }

    };

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        gmap = googleMap;
        LatLng goal = new LatLng(goal_lat, goal_lng); //목적지

        MarkerOptions goalmarkeroption = new MarkerOptions();
        goalmarkeroption.title("목적지");
        Geocoder geocoder = new Geocoder(map.this);
        try {
            list = geocoder.getFromLocation(goal_lat, goal_lng, 10);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (list != null) {
            if (list.size()==0) {
                goalmarkeroption.snippet("해당되는 주소 정보는 없습니다");
            } else {
                goalmarkeroption.snippet(list.get(0).getAddressLine(0));

            }
        }
        goalmarkeroption.position(goal);
        goalMarker = gmap.addMarker((goalmarkeroption));
        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(goal, (float)15));

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
            return;
        }
        Location currrrrlocation = locationManager.getLastKnownLocation(locationProvider);
        locationManager.requestLocationUpdates(locationProvider, 5000, 10, listener);

        mfusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null) {
                    LatLng mycurloc =  new LatLng(location.getLatitude(), location.getLongitude());
                    startMarkeroption = new MarkerOptions();
                    startMarkeroption.position(mycurloc);
                    startMarkeroption.title("현재 위치 : " + location.getLatitude() + ", "  + location.getLongitude());
                    startMarker = gmap.addMarker(startMarkeroption);
                    LatLng camera = new LatLng((startMarker.getPosition().latitude + goalMarker.getPosition().latitude)/2, (startMarker.getPosition().longitude + goalMarker.getPosition().longitude) / 2);
                    double zoom = 20 - (Math.log(Math.max(Math.abs(startMarker.getPosition().latitude - goalMarker.getPosition().latitude), Math.abs(startMarker.getPosition().longitude - goalMarker.getPosition().longitude)) / 0.00035) / Math.log(2));
                    gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(camera, (float)zoom));
                }
                else{
                    new AlertDialog.Builder(map.this).setMessage("현재 위치를 받을 수 없습니다.").setPositiveButton("OK",null).show();
                }
            }
        });

        gmap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(LatLng point) {
                if(tempMarker != null) {
                    tempMarker.remove();
                }
                MarkerOptions mOptions = new MarkerOptions();
                // 마커 타이틀
                mOptions.title("마커 좌표");
                Double latitude = point.latitude; // 위도
                Double longitude = point.longitude; // 경도
                Geocoder geocoder = new Geocoder(map.this);
                try {
                    list = geocoder.getFromLocation(latitude, longitude, 10);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (list != null) {
                    if (list.size()==0) {
                        mOptions.snippet("해당되는 주소 정보는 없습니다");
                    } else {
                        mOptions.snippet(list.get(0).getAddressLine(0));

                    }
                }

                // LatLng: 위도 경도 쌍을 나타냄
                mOptions.position(new LatLng(latitude, longitude));
                // 마커(핀) 추가
                tempMarker = googleMap.addMarker(mOptions);
            }
        });

    }

/*
    public void currentLocation(View view) {
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

    }*/

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
                                startMarkeroption = new MarkerOptions();
                                startMarker.setPosition(mycurloc);

                                Geocoder geocoder = new Geocoder(map.this);
                                try {
                                    list = geocoder.getFromLocation(mycurloc.latitude, mycurloc.longitude, 10);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (list != null) {
                                    if (list.size()==0) {
                                        startMarkeroption.snippet("해당되는 주소 정보는 없습니다");
                                    } else {
                                        startMarkeroption.snippet(list.get(0).getAddressLine(0));

                                    }
                                }



                                startMarker.setTitle("현재 위치" );
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
