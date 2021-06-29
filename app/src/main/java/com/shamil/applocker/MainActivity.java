package com.shamil.applocker;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.shamil.applocker.LockScreen.PatternChange;
import com.shamil.applocker.LockScreen.PatternLockScreen;
import com.shamil.applocker.LockScreen.PinCodeChange;
import com.shamil.applocker.LockScreen.PinCodeLockScreen;
import com.shamil.applocker.Service.MyAccessibilityService;
import com.shamil.applocker.Service.ServiceChecker;
import com.shamil.applocker.Settings.SettingsActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<AppAdapter> appList = null;
    MyListAdapter listAdapter = null;
    ListView listview;
    DBHelper db;
    public static String pass;
    public static String passType;
    public static ArrayList<String> dbPackage;
    PackageManager packageManager = null;

    public static void updateData(Context context) {
        DBHelper db = new DBHelper(context);
        dbPackage = db.getAllPackage();
        SharedPreferences pref = context.getSharedPreferences("AppLocker", 0);
        pass = pref.getString("code", "");
        passType = pref.getString("type", "");
    }

    public static boolean isAccessibilityServiceEnabled(Context context, Class<?> accessibilityService) {
        ComponentName expectedComponentName = new ComponentName(context, accessibilityService);

        String enabledServicesSetting = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        if (enabledServicesSetting == null)
            return false;

        TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter(':');
        colonSplitter.setString(enabledServicesSetting);

        while (colonSplitter.hasNext()) {
            String componentNameString = colonSplitter.next();
            ComponentName enabledService = ComponentName.unflattenFromString(componentNameString);

            if (enabledService != null && enabledService.equals(expectedComponentName))
                return true;
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = getSharedPreferences("AppLocker", 0);
        permissionCheck();
        updateData(this);
        db = new DBHelper(this);

        if (!pref.contains("code")) {
            selectLockTypeDialog(MainActivity.this);
        } else {
            if (passType.equals("pin")) {
                PinCodeLockScreen.PinCodeLockScreen(this, getPackageName());
            } else {
                PatternLockScreen.PatternLockScreen(this, getPackageName());
            }
        }

        packageManager = getPackageManager();
        appList = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
        Collections.sort(appList, new Comparator<AppAdapter>() {
            public int compare(AppAdapter p1, AppAdapter p2) {
                return p1.appName.compareTo(p2.appName);
            }
        });
        listAdapter = new MyListAdapter(MainActivity.this, appList);

        listview = findViewById(R.id.app_list);
        listview.setAdapter(listAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppAdapter item = listAdapter.getItem(position);
                if (R.drawable.locked == item.getLocked()) {
                    db.deletePackage(item.packageName);
                    item.setLocked(R.drawable.not_locked);
                } else {
                    db.insertPackage(item.packageName);
                    item.setLocked(R.drawable.locked);
                }
                listAdapter.notifyDataSetChanged();
            }
        });
        int second = 2;

        Intent i = new Intent(this, ServiceChecker.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
                i, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + second * 1000, pendingIntent);
        }
    }

    private List<AppAdapter> checkForLaunchIntent(List<ApplicationInfo> list) {
        ArrayList<AppAdapter> application = new ArrayList<AppAdapter>();
        for (ApplicationInfo info : list) {
            try {
                int isLocked = R.drawable.not_locked;
                for (String dbPackage : db.getAllPackage()) {
                    if (dbPackage.equals(info.packageName)) {
                        isLocked = R.drawable.locked;
                    }
                }
                if (packageManager.getLaunchIntentForPackage(info.packageName) != null) {
                    application.add(new AppAdapter(info.loadLabel(packageManager).toString(), info.packageName, info.loadIcon(packageManager), isLocked));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return application;
    }

    @Override
    protected void onStop() {
        updateData(this);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);

        return super.onOptionsItemSelected(item);


    }

    void permissionDialog(String message, Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OPEN SETTINGS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(intent);
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        alert.setTitle("PERMISSION DENIED");
        alert.show();
    }
    public static void selectLockTypeDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.select_lock_type);

        Button patternButton = dialog.findViewById(R.id.selected_pattern);
        Button pinButton = dialog.findViewById(R.id.selected_pin);
        patternButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(context, PatternChange.class);
                context.startActivity(intent);
            }
        });
        pinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(context, PinCodeChange.class);
                context.startActivity(intent);
            }
        });

        dialog.show();
    }

    void permissionCheck() {

        // Accessibility Permission
        if (!isAccessibilityServiceEnabled(this, MyAccessibilityService.class)) {
            Intent accessibilityIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            permissionDialog("GRANT ACCESSIBILITY PERMISSION", accessibilityIntent);
        }

        // Display Overlay Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean drawOverlays = Settings.canDrawOverlays(this);
            if (!drawOverlays) {
                Intent displayIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                permissionDialog("GRANT DISPLAY OVERLAY PERMISSION", displayIntent);

            }
        }

    }
}