package com.shamil.applocker;

import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

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
                            Toast.makeText(context, "Locked", Toast.LENGTH_SHORT).show();

                            if (pref.getString("lock", "").equals("pin")) {
                                PinCodeLockScreen.PinCodeLockScreen(context, topActivity);
                            } else {
                                PatternLockScreen.PatternLockScreen(context, topActivity);
                            }
                        }
                    }
                }
                SecondApp = topActivity;
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(new Intent(context, MyService.class));
                } else {
                    startService(new Intent(context, MyService.class));
                }
            }
        }, 1000);

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);
        super.onTaskRemoved(rootIntent);
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
    public int onStartCommand(Intent intent, int flags, int startId) {
        onTaskRemoved(intent);
        return START_STICKY;
    }
}