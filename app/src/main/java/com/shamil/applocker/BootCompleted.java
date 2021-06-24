package com.shamil.applocker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.shamil.applocker.Service.MyService;

public class BootCompleted extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, MyService.class);
        context.startService(serviceIntent);
    }
}