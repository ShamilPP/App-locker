package com.applocker;

import android.graphics.drawable.Drawable;

public class AppAdapter {

    public String appName;
    public String packageName;
    public int isLocked;
    public Drawable appIcon;

    public AppAdapter(String appName, String packageName, Drawable appIcon, int isLocked) {
        super();
        this.appName = appName;
        this.packageName = packageName;
        this.appIcon = appIcon;
        this.isLocked = isLocked;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public int getLocked() {
        return isLocked;
    }

    public void setLocked(int isLocked) {
        this.isLocked = isLocked;
    }
}
