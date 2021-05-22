package com.applocker;

import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<AppAdapter> appList = null;
    MyListAdapter listAdapter = null;
    ListView listview;
    DBHelper db;
    private PackageManager packageManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = this.getSharedPreferences("AppLocker", 0);

        if (pref.contains("code") == false) {
            Intent intent = new Intent(MainActivity.this, SelectChangeLock.class);
            startActivity(intent);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(MainActivity.this, MyService.class));
        } else {
            startService(new Intent(MainActivity.this, MyService.class));
        }

        permissionCheck();
        db = new DBHelper(this);
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

        int second = 5;

        Intent intent = new Intent(MainActivity.this, RestartReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,
                intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + second * 1000, pendingIntent);

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

    void permissionCheck() {

        // Usage State Permission
        boolean granted = false;
        AppOpsManager appOps = (AppOpsManager) MainActivity.this
                .getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), MainActivity.this.getPackageName());

        if (mode == AppOpsManager.MODE_DEFAULT) {
            granted = (MainActivity.this.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
        } else {
            granted = (mode == AppOpsManager.MODE_ALLOWED);
        }
        if (granted == false) {
            Intent usageStatePermission = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            permissionDialog("GRANT USAGE STATE PERMISSION", usageStatePermission);
        }

        // Display Overlay Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean drawOverlays = Settings.canDrawOverlays(this);
            if (drawOverlays == false) {
                Intent displayIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                permissionDialog("GRANT DISPLAY OVERLAY PERMISSION", displayIntent);

            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.change:
                Intent intent = new Intent(this, SelectChangeLock.class);
                startActivity(intent);
                return true;
            case R.id.clear:
                this.deleteDatabase("Packages.db");
                finish();
                return true;
            case R.id.exit:
                finish();

            default:
                return super.onOptionsItemSelected(item);

        }
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

}