package com.shamil.applocker.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.shamil.applocker.DBHelper;
import com.shamil.applocker.MainActivity;
import com.shamil.applocker.LockScreen.PatternLockScreen;
import com.shamil.applocker.LockScreen.PinCodeLockScreen;
import com.shamil.applocker.R;

import java.util.List;


public class MyService extends Service {

    public static Runnable runnable = null;
    static String SecondApp = " ";
    static SharedPreferences pref;
    public Context context = this;
    public Handler handler = null;

    public static void getTopApp(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager m = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            if (m != null) {
                long now = System.currentTimeMillis();
                List<UsageStats> stats = m.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, 0, now);
                String topActivity = "";
                if ((stats != null) && (!stats.isEmpty())) {
                    int j = 0;
                    for (int i = 0; i < stats.size(); i++) {
                        if (stats.get(i).getLastTimeUsed() > stats.get(j).getLastTimeUsed()) {
                            j = i;
                        }
                    }
                    topActivity = stats.get(j).getPackageName();
                }
                if (SecondApp.equals(context.getPackageName()) || SecondApp.equals(topActivity)) {
                } else {
                    DBHelper db = new DBHelper(context);
                    for (String dbPackage : db.getAllPackage()) {
                        if (topActivity.equals(dbPackage)) {
                            if (!pref.getString("code", "").equals("")) {
                                if (pref.getString("type", "").equals("pin")) {
                                    PinCodeLockScreen.PinCodeLockScreen(context, topActivity);
                                } else {
                                    PatternLockScreen.PatternLockScreen(context, topActivity);
                                }
                            }
                        }
                    }
                }
                SecondApp = topActivity;
            }
        }
    }

    @Override
    public void onCreate() {

        pref = getSharedPreferences("AppLocker", 0);
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                getTopApp(context);
                handler.postDelayed(runnable, 1000);
            }
        };

        handler.postDelayed(runnable, 1000);

    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent serviceIntent = new Intent(this, MyService.class);
        startService(serviceIntent);
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_ID)
                .setContentTitle("App Lock")
                .setContentText("App Lock service started")
                .setSmallIcon(R.drawable.icon)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Intent serviceIntent = new Intent(this, MyService.class);
        startService(serviceIntent);
        super.onDestroy();
    }
}