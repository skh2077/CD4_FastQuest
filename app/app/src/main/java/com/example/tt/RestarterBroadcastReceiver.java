package com.example.tt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class RestarterBroadcastReceiver extends BroadcastReceiver {

    private final static String TAG = RestarterBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("yooooooooooooooooooo", "ahhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
        context.startService(new Intent(context, BackgroundService.class));
    }
}
