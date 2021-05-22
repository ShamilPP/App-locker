package com.applocker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

public class PatternChange extends AppCompatActivity {
    boolean ifFirst = true;
    String PatternCode = "";

    PatternLockView patternLockView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pattern_lock_screen);

        patternLockView = findViewById(R.id.pattern_lock_view);
        textView = findViewById(R.id.password_change_text);

        textView.setText("Draw New Pattern");
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
                change(pattern);
            }
        });
    }

    private void change(List<PatternLockView.Dot> pattern) {
        if (ifFirst == true) {
            PatternCode = PatternLockUtils.patternToString(patternLockView, pattern);
            ifFirst = false;
            patternLockView.clearPattern();
            textView.setText("ReDraw Pattern");
        } else {
            if (PatternLockUtils.patternToString(patternLockView, pattern).equals(PatternCode)) {
                //Save
                SharedPreferences pref = PatternChange.this.getSharedPreferences("AppLocker", 0);
                SharedPreferences.Editor editor = pref.edit();
                if(pref.contains("lock")){
                    Toast.makeText(this, "Lock Changed", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Lock added", Toast.LENGTH_SHORT).show();
                }
                editor.putString("code", PatternCode);
                editor.putString("lock", "pattern");
                editor.commit();
                finish();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                patternLockView.setPattern(PatternLockView.PatternViewMode.WRONG, PatternLockUtils.stringToPattern(patternLockView, PatternLockUtils.patternToString(patternLockView, pattern)));
                Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(400);
            }
        }
    }
}
