package com.example.tt;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.tt.data.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class BackgroundService extends Service {

    private static final String TAG = "BOOMBOOMTESTGPS";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    NotificationManager notificationManager;
    User user;
    static SharedPreferences save;
    static SharedPreferences.Editor editor;
    double latitude;
    double longitude;

    @Override
    public void onCreate()
    {
        save = getSharedPreferences("mysave", MODE_PRIVATE);
        editor = save.edit();

        String lati = save.getString("latitude","");
        String longi = save.getString("longitude","");
        if(!lati.equals("") && !longi.equals("")) {
            latitude = Double.parseDouble(lati);
            longitude = Double.parseDouble(longi);
        }

        Log.e(TAG, "onCreate");

        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    private class LocationListener implements android.location.LocationListener
    {
        Location mLastLocation;

        public LocationListener(String provider)
        {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
            user = User.getInstance();
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            //토스트 띄우기
            Toast.makeText(BackgroundService.this, "백그라운드 실행", Toast.LENGTH_LONG).show();


            if(Math.abs(mLastLocation.getLatitude() - latitude) < 0.0005 &&//약50m
            Math.abs(mLastLocation.getLongitude() - longitude) < 0.0005) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


                NotificationCompat.Builder builder = new NotificationCompat.Builder(BackgroundService.this, "1001")
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground)) //BitMap 이미지 요구
                        .setContentTitle("알림")
                        //.setContentText(location.toString())
                        // 더 많은 내용이라서 일부만 보여줘야 하는 경우 아래 주석을 제거하면 setContentText에 있는 문자열 대신 아래 문자열을 보여줌
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("위치에 도착했습니다!!"))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);

                //OREO API 26 이상에서는 채널 필요
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    builder.setSmallIcon(R.drawable.ic_launcher_foreground); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
                    CharSequence channelName = "노티페케이션 채널";
                    String description = "오레오 이상을 위한 것임";
                    int importance = NotificationManager.IMPORTANCE_HIGH;

                    NotificationChannel channel = new NotificationChannel("1001", channelName, importance);
                    channel.setDescription(description);

                    // 노티피케이션 채널을 시스템에 등록
                    assert notificationManager != null;
                    notificationManager.createNotificationChannel(channel);

                } else
                    builder.setSmallIcon(R.mipmap.ic_launcher); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

                assert notificationManager != null;
                notificationManager.notify(1234, builder.build()); // 고유숫자로 노티피케이션 동작시킴
            }
        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)

    };

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    Timer timer = new Timer();
    TimerTask timerTask;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        String cur_time_str = format.format(new Date());
        Date cur_time = new Date();
        try {
            cur_time = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").parse(cur_time_str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        String time = save.getString("date","");
        if(!time.equals("")) {
            timer = new Timer();
            initializeTimerTask();
            long delay = 0;
            Date moim_time = user.getUser_act().date;
            if (moim_time.getTime() - cur_time.getTime() > 1000 * 60 * 30) {
                delay = moim_time.getTime() - cur_time.getTime() - 1000 * 60 * 30;
            }
            timer.schedule(timerTask, delay);
        }
        return START_STICKY;
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {

            public void run() {
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


                NotificationCompat.Builder builder = new NotificationCompat.Builder(BackgroundService.this, "1001")
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground)) //BitMap 이미지 요구
                        .setContentTitle("알림")
                        .setContentText("곧 시간이에요!!.")
                        // 더 많은 내용이라서 일부만 보여줘야 하는 경우 아래 주석을 제거하면 setContentText에 있는 문자열 대신 아래 문자열을 보여줌
                        //.setStyle(new NotificationCompat.BigTextStyle().bigText("곧 시간이에요!!."))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);

                //OREO API 26 이상에서는 채널 필요
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    builder.setSmallIcon(R.drawable.ic_launcher_foreground); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
                    CharSequence channelName = "노티페케이션 채널";
                    String description = "오레오 이상을 위한 것임";
                    int importance = NotificationManager.IMPORTANCE_HIGH;

                    NotificationChannel channel = new NotificationChannel("1001", channelName, importance);
                    channel.setDescription(description);

                    // 노티피케이션 채널을 시스템에 등록
                    assert notificationManager != null;
                    notificationManager.createNotificationChannel(channel);

                } else
                    builder.setSmallIcon(R.mipmap.ic_launcher); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

                assert notificationManager != null;
                notificationManager.notify(1234, builder.build()); // 고유숫자로 노티피케이션 동작시킴
            }
        };
    }

    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        Intent broadcastIntent = new Intent("com.bluexmas.common.RestartService");
        sendBroadcast(broadcastIntent);
        Toast.makeText(BackgroundService.this, "끝남", Toast.LENGTH_LONG).show();
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}
