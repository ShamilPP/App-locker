package com.applocker;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class PinCodeLockScreen {
    static Dialog dialog;
    static String pinCode = "";
    static SharedPreferences pref;

    public static void PinCodeLockScreen(Context context, String packageName) {
        pref = context.getSharedPreferences("AppLocker", 0);
        Button button1, button2, button3, button4, button5, button6, button7, button8, button9, button0;
        ImageView imageView;

        dialog = new Dialog(context, R.style.Theme_AppLocker);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptsView = layoutInflater.inflate(R.layout.pin_lock_screen, null, false);

        imageView = promptsView.findViewById(R.id.app_icon_locked);

        try {
            Drawable icon = context.getPackageManager().getApplicationIcon(packageName);
            imageView.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        button1 = promptsView.findViewById(R.id.button1);
        button2 = promptsView.findViewById(R.id.button2);
        button3 = promptsView.findViewById(R.id.button3);
        button4 = promptsView.findViewById(R.id.button4);
        button5 = promptsView.findViewById(R.id.button5);
        button6 = promptsView.findViewById(R.id.button6);
        button7 = promptsView.findViewById(R.id.button7);
        button8 = promptsView.findViewById(R.id.button8);
        button9 = promptsView.findViewById(R.id.button9);
        button0 = promptsView.findViewById(R.id.button0);

        buttonClick(context, button1, "1", dialog, promptsView);
        buttonClick(context, button2, "2", dialog, promptsView);
        buttonClick(context, button3, "3", dialog, promptsView);
        buttonClick(context, button4, "4", dialog, promptsView);
        buttonClick(context, button5, "5", dialog, promptsView);
        buttonClick(context, button6, "6", dialog, promptsView);
        buttonClick(context, button7, "7", dialog, promptsView);
        buttonClick(context, button8, "8", dialog, promptsView);
        buttonClick(context, button9, "9", dialog, promptsView);
        buttonClick(context, button0, "0", dialog, promptsView);

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

    public static void buttonClick(Context context, Button button, String number, Dialog dialog, View view) {

        ImageView imageView1 = view.findViewById(R.id.locked_icon_1);
        ImageView imageView2 = view.findViewById(R.id.locked_icon_2);
        ImageView imageView3 = view.findViewById(R.id.locked_icon_3);
        ImageView imageView4 = view.findViewById(R.id.locked_icon_4);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinCode = pinCode + number;
                if (pinCode.equals(pref.getString("code", null))) {
                    imageView1.setImageResource(R.drawable.green_filled);
                    imageView2.setImageResource(R.drawable.green_filled);
                    imageView3.setImageResource(R.drawable.green_filled);
                    imageView4.setImageResource(R.drawable.green_filled);
                    pinCode = "";

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pinCode = "";
                            dialog.dismiss();

                        }
                    }, 400);

                } else if (pinCode.length() == 4) {

                    //Error detected vibrator mobile
                    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(400);

                    imageView1.setImageResource(R.drawable.red_filled);
                    imageView2.setImageResource(R.drawable.red_filled);
                    imageView3.setImageResource(R.drawable.red_filled);
                    imageView4.setImageResource(R.drawable.red_filled);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pinCode = "";
                            imageView1.setBackgroundResource(R.drawable.circle_not_filled);
                            imageView2.setBackgroundResource(R.drawable.circle_not_filled);
                            imageView3.setBackgroundResource(R.drawable.circle_not_filled);
                            imageView4.setBackgroundResource(R.drawable.circle_not_filled);

                            imageView1.setImageResource(R.drawable.circle_not_filled);
                            imageView2.setImageResource(R.drawable.circle_not_filled);
                            imageView3.setImageResource(R.drawable.circle_not_filled);
                            imageView4.setImageResource(R.drawable.circle_not_filled);
                        }
                    }, 500);
                }

                if (pinCode.length() == 1) {
                    imageView1.setBackgroundResource(R.drawable.circle_filled);
                    imageView2.setBackgroundResource(R.drawable.circle_not_filled);
                    imageView3.setBackgroundResource(R.drawable.circle_not_filled);
                    imageView4.setBackgroundResource(R.drawable.circle_not_filled);
                } else if (pinCode.length() == 2) {
                    imageView1.setBackgroundResource(R.drawable.circle_filled);
                    imageView2.setBackgroundResource(R.drawable.circle_filled);
                    imageView3.setBackgroundResource(R.drawable.circle_not_filled);
                    imageView4.setBackgroundResource(R.drawable.circle_not_filled);
                } else if (pinCode.length() == 3) {
                    imageView1.setBackgroundResource(R.drawable.circle_filled);
                    imageView2.setBackgroundResource(R.drawable.circle_filled);
                    imageView3.setBackgroundResource(R.drawable.circle_filled);
                    imageView4.setBackgroundResource(R.drawable.circle_not_filled);
                } else if (pinCode.length() == 4) {
                    imageView1.setBackgroundResource(R.drawable.circle_filled);
                    imageView2.setBackgroundResource(R.drawable.circle_filled);
                    imageView3.setBackgroundResource(R.drawable.circle_filled);
                    imageView4.setBackgroundResource(R.drawable.circle_filled);
                }
            }
        });
    }
}
