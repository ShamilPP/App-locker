package com.applocker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SelectChangeLock extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_change_lock);

        Button pattern = findViewById(R.id.pattern_button);
        Button pin = findViewById(R.id.pin_button);

        pattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(SelectChangeLock.this, PatternChange.class);
                startActivity(intent);
            }
        });
        pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(SelectChangeLock.this, PinCodeChange.class);
                startActivity(intent);
            }
        });
    }
}