package com.applocker.Settings;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.applocker.R;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        final ListView list = findViewById(R.id.settings_listview);
        ArrayList<SettingsData> arrayList = new ArrayList<SettingsData>();
        arrayList.add(new SettingsData("Change Lock","Select your own lock pattern or pin"));
        arrayList.add(new SettingsData("Delete all locked apps","Delete your selected locked apps and app close"));
        arrayList.add(new SettingsData("Exit","Exit this app"));

        SettingsAdapter customAdapter = new SettingsAdapter(this, arrayList);
        list.setAdapter(customAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SettingsActivity.this, position+" Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}