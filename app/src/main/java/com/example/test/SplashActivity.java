package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;



public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Objects.requireNonNull(getSupportActionBar()).hide();
        Intent iHome = new Intent(SplashActivity.this,LoginActivity.class);
        TextView splash_text = findViewById(R.id.splash_text);
        Animation animate = AnimationUtils.loadAnimation(this,R.anim.splash_animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                splash_text.startAnimation(animate);
            }
        },1500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(iHome);
                finish();
            }
        },1900);

    }
}