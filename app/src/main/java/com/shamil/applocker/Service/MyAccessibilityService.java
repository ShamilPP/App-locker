package com.shamil.applocker.Service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.shamil.applocker.LockScreen.PatternLockScreen;
import com.shamil.applocker.LockScreen.PinCodeLockScreen;
import com.shamil.applocker.MainActivity;
import com.shamil.applocker.R;

import static com.shamil.applocker.MainActivity.dbPackage;
import static com.shamil.applocker.MainActivity.pass;
import static com.shamil.applocker.MainActivity.passType;
import static com.shamil.applocker.MainActivity.updateData;

public class MyAccessibilityService extends AccessibilityService {

    static String secondApp = "";

    public static void getAppAndLock(Context context, AccessibilityEvent event) {
        String topActivity = event.getPackageName() + "";
        if (secondApp.equals(context.getPackageName()) || secondApp.equals(topActivity) || topActivity.equals(context.getPackageName())) {
        } else {
            for (String currentPackage : dbPackage) {
                if (topActivity.equals(currentPackage)) {
                    if (!pass.equals("")) {
                        if (passType.equals("pin")) {
                            PinCodeLockScreen.PinCodeLockScreen(context, topActivity);
                        } else {
                            PatternLockScreen.PatternLockScreen(context, topActivity);
                        }
                    }
                }
            }
        }
        secondApp = topActivity;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        getAppAndLock(this, event);
    }

    @Override
    public void onServiceConnected() {
        updateData(this);
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        this.setServiceInfo(info);
    }

    @Override
    public void onInterrupt() {
        Toast.makeText(this, "Service Interrupt", Toast.LENGTH_SHORT).show();
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
}
