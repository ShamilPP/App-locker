package com.applocker;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyListAdapter extends ArrayAdapter<AppAdapter> {

    private final Context context;
    private final PackageManager packageManager;
    private List<AppAdapter> appList = null;

    public MyListAdapter(Context context, List<AppAdapter> list) {
        super(context, R.layout.custom_apps, list);

        this.context = context;
        this.packageManager = context.getPackageManager();
        this.appList = list;
    }

    @Override
    public int getCount() {
        return ((null != appList) ? appList.size() : 0);
    }

    @Override
    public AppAdapter getItem(int position) {
        return ((null != appList) ? appList.get(position) : null);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (null == view) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.custom_apps, null);
        }

        AppAdapter data = appList.get(position);
        if (null != data) {
            TextView appName = view.findViewById(R.id.app_name);
            TextView appPackage = view.findViewById(R.id.app_package);
            ImageView appIcon = view.findViewById(R.id.app_icon);
            ImageView imgLocked = view.findViewById(R.id.imgLocked);

            appName.setText(data.appName);
            appPackage.setText(data.packageName);
            appIcon.setImageDrawable(data.appIcon);
            imgLocked.setBackgroundResource(data.isLocked);

        }
        return view;
    }
}