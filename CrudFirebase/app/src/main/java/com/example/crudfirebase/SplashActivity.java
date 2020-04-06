package com.example.crudfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent menu;
                menu = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(menu);
            }
        }, 4000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
