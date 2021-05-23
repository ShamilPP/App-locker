package com.applocker.Settings;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.applocker.MainActivity;
import com.applocker.R;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        final ListView list = findViewById(R.id.settings_listview);
        ArrayList<SettingsData> arrayList = new ArrayList<SettingsData>();
        arrayList.add(new SettingsData("Change Lock", "Select your own lock pattern or pin", "null"));
        arrayList.add(new SettingsData("Delete all locked apps", "Delete your selected locked apps and app close", "null"));
        arrayList.add(new SettingsData("Hide App", "Hide or unhide app", "true"));
        arrayList.add(new SettingsData("Exit", "Exit this app", "null"));

        SettingsAdapter customAdapter = new SettingsAdapter(this, arrayList);
        list.setAdapter(customAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    //change lock
                    MainActivity.selectLockTypeDialog(SettingsActivity.this);
                }else if (position == 1) {
                    // delete database
                    deleteDatabase("Packages.db");
                    finishAffinity();
                } else if (position == 3) {
                    // exit
                    finishAffinity();
                }
            }
        });
    }
}