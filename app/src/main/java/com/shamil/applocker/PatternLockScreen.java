package com.shamil.applocker;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

public class PatternLockScreen {
    static Dialog dialog;
    static SharedPreferences pref;
    static ImageView imageView;

    public static void PatternLockScreen(Context context, String packageName) {
        pref = context.getSharedPreferences("AppLocker", 0);

        dialog = new Dialog(context, R.style.Theme_AppLocker);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptsView = layoutInflater.inflate(R.layout.pattern_lock_screen, null, false);

        imageView = promptsView.findViewById(R.id.app_icon_locked);

        try {
            Drawable icon = context.getPackageManager().getApplicationIcon(packageName);
            imageView.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        PatternLockView patternLockView = promptsView.findViewById(R.id.pattern_lock_view);
        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {
            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {
            }

            @Override
            public void onCleared() {
            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {

                if (PatternLockUtils.patternToString(patternLockView, pattern).equals(pref.getString("code", ""))) {
                    dialog.dismiss();
                } else {
                    patternLockView.setPattern(PatternLockView.PatternViewMode.WRONG, PatternLockUtils.stringToPattern(patternLockView, PatternLockUtils.patternToString(patternLockView, pattern)));
                    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(400);
                }
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        } else {
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
        }
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(promptsView);
        dialog.getWindow().setGravity(Gravity.CENTER);

        dialog.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == KeyEvent.ACTION_UP) {
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(startMain);
                }
                return true;
            }
        });
        dialog.show();
    }
}
