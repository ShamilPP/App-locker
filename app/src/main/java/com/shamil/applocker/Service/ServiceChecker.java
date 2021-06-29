package com.shamil.applocker.Service;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.content.ContextCompat;

public class ServiceChecker extends BroadcastReceiver {
    private static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!isMyServiceRunning(context, MyAccessibilityService.class)) {
            ContextCompat.startForegroundService(context, new Intent(context, MyAccessibilityService.class));
        }
        int second = 600;

        Intent i = new Intent(context, ServiceChecker.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                i, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + second * 1000, pendingIntent);
        }

    }
}
