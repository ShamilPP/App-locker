package com.applocker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PinCodeChange extends AppCompatActivity {
    Button button1, button2, button3, button4, button5, button6, button7, button8, button9, button0;
    ImageView imageView1, imageView2, imageView3, imageView4;
    TextView textView;
    String pinCode = "";
    String firstPin = "";
    boolean ifNewEnter = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_lock_screen);

        imageView1 = findViewById(R.id.locked_icon_1);
        imageView2 = findViewById(R.id.locked_icon_2);
        imageView3 = findViewById(R.id.locked_icon_3);
        imageView4 = findViewById(R.id.locked_icon_4);

        textView = findViewById(R.id.password_change_text);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        button9 = findViewById(R.id.button9);
        button0 = findViewById(R.id.button0);

        textView.setText("Enter new Password");
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick("1");
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick("2");
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick("3");
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick("4");
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick("5");
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick("6");
            }
        });
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick("7");
            }
        });
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick("8");
            }
        });
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick("9");
            }
        });
        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick("0");
            }
        });
    }

    void buttonClick(String number) {

        if (pinCode.length() >= 4) {
            pinCode = "";
            imageView1.setBackgroundResource(R.drawable.circle_not_filled);
            imageView2.setBackgroundResource(R.drawable.circle_not_filled);
            imageView3.setBackgroundResource(R.drawable.circle_not_filled);
            imageView4.setBackgroundResource(R.drawable.circle_not_filled);
        }
        pinCode = pinCode + number;

        if (pinCode.length() == 4) {
            change(pinCode);
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

    private void change(String pinCode) {
        if (ifNewEnter == true) {
            firstPin = pinCode;
            imageView1.setImageResource(R.drawable.green_filled);
            imageView2.setImageResource(R.drawable.green_filled);
            imageView3.setImageResource(R.drawable.green_filled);
            imageView4.setImageResource(R.drawable.green_filled);
            pinCode = "";
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ifNewEnter = false;
                    textView.setText("ReEnter Password");
                    imageView1.setImageResource(R.drawable.circle_not_filled);
                    imageView2.setImageResource(R.drawable.circle_not_filled);
                    imageView3.setImageResource(R.drawable.circle_not_filled);
                    imageView4.setImageResource(R.drawable.circle_not_filled);

                    imageView1.setBackgroundResource(R.drawable.circle_not_filled);
                    imageView2.setBackgroundResource(R.drawable.circle_not_filled);
                    imageView3.setBackgroundResource(R.drawable.circle_not_filled);
                    imageView4.setBackgroundResource(R.drawable.circle_not_filled);
                }
            }, 400);

        } else {
            if (pinCode.equals(firstPin)) {
                pinCode = "";
                imageView1.setImageResource(R.drawable.green_filled);
                imageView2.setImageResource(R.drawable.green_filled);
                imageView3.setImageResource(R.drawable.green_filled);
                imageView4.setImageResource(R.drawable.green_filled);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText("");
                        // save
                        SharedPreferences pref = PinCodeChange.this.getSharedPreferences("AppLocker", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        if(pref.contains("lock")){
                            Toast.makeText(PinCodeChange.this, "Lock Changed", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(PinCodeChange.this, "Lock added", Toast.LENGTH_SHORT).show();
                        }
                        editor.putString("code", firstPin);
                        editor.putString("lock", "pin");
                        editor.commit();
                        finish();
                        Intent intent = new Intent(PinCodeChange.this, MainActivity.class);
                        startActivity(intent);
                    }
                }, 400);
            } else {

                Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(400);

                imageView1.setImageResource(R.drawable.red_filled);
                imageView2.setImageResource(R.drawable.red_filled);
                imageView3.setImageResource(R.drawable.red_filled);
                imageView4.setImageResource(R.drawable.red_filled);
                pinCode = "";
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

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
        }
    }
}