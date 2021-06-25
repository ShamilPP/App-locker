package com.shamil.applocker.Receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import com.shamil.applocker.Service.MyService;

public class BootCompleted extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, MyService.class);
        ContextCompat.startForegroundService(context, serviceIntent);
        int second = 5;

        Intent i = new Intent(context, ServiceChecker.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                i, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + second * 1000, pendingIntent);

    }
}