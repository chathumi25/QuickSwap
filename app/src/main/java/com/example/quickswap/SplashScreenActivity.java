package com.example.quickswap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {
    private static final int SPLASH_DELAY_MS = 2500; // 2.5s

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int layoutId = getResources().getIdentifier("splash_screen_activity", "layout", getPackageName());
        if (layoutId == 0) throw new RuntimeException("Layout 'splash_screen_activity' not found in res/layout/");
        setContentView(layoutId);

        int logoId = getResources().getIdentifier("logo", "id", getPackageName());
        int appNameId = getResources().getIdentifier("appName", "id", getPackageName());
        int progressBarId = getResources().getIdentifier("progressBar", "id", getPackageName());

        ImageView logo = findViewById(logoId);
        TextView appName = findViewById(appNameId);
        ProgressBar progressBar = findViewById(progressBarId);

        int fadeInId = getResources().getIdentifier("fade_in", "anim", getPackageName());
        if (fadeInId != 0) {
            Animation fadeIn = AnimationUtils.loadAnimation(this, fadeInId);
            logo.startAnimation(fadeIn);
            appName.startAnimation(fadeIn);
        }

        appName.setText("QuickSwap");

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
            finish();
        }, SPLASH_DELAY_MS);
    }
}
