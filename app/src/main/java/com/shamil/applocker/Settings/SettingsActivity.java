package com.shamil.applocker.Settings;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.shamil.applocker.MainActivity;
import com.shamil.applocker.R;

import java.util.ArrayList;

import static com.shamil.applocker.MainActivity.updateData;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        final ListView list = findViewById(R.id.settings_listview);
        ArrayList<SettingsData> arrayList = new ArrayList<SettingsData>();
        arrayList.add(new SettingsData("Change Lock", "Select your own lock pattern or pin"));
        arrayList.add(new SettingsData("Delete all locked apps", "Delete your selected locked apps and app close"));
        arrayList.add(new SettingsData("Exit", "Exit this app"));
        arrayList.add(new SettingsData("About", "Created by : Shamil"));

        SettingsAdapter customAdapter = new SettingsAdapter(this, arrayList);
        list.setAdapter(customAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    //change lock
                    MainActivity.selectLockTypeDialog(SettingsActivity.this);
                } else if (position == 1) {
                    // delete database
                    deleteDatabase("Packages.db");
                    finishAffinity();
                } else if (position == 2) {
                    // exit
                    finishAffinity();
                } else if(position ==3){
                    Toast.makeText(SettingsActivity.this, "Developer : Shamil", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    protected void onStop() {
        updateData(this);
        super.onStop();
    }
}