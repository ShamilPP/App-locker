package com.applocker.Settings;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.applocker.R;

import java.util.ArrayList;


public class SettingsAdapter implements ListAdapter {
    ArrayList<SettingsData> arrayList;
    Context context;

    public SettingsAdapter(Context context, ArrayList<SettingsData> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SettingsData settingsData = arrayList.get(position);
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.settings_custom, null);
            TextView title = convertView.findViewById(R.id.custom_title);
            TextView sub = convertView.findViewById(R.id.custom_sub);
            Switch switchV = convertView.findViewById(R.id.custom_switch);
            title.setText(settingsData.title);
            sub.setText(settingsData.sub);
            // hide switch
            PackageManager p = context.getPackageManager();
            ComponentName componentName = new ComponentName(context, com.applocker.MainActivity.class);
            boolean switchCurrent = true;
            if (p.getComponentEnabledSetting(componentName) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
                switchCurrent = false;
            }
            if (settingsData.switchValue.equals("true")) {
                switchV.setVisibility(View.VISIBLE);
                switchV.setChecked(switchCurrent);
            }
            switchV.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                  if(isChecked==true){
                      p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                  }else{
                      p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                  }
                }
            });

        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return arrayList.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}