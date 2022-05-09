package com.example.mycookbook.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.mycookbook.R;

public class SplashScreen  extends Activity {

    private static int TIME_OUT = 3000;
    private AppCompatImageView imagemLogo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        imagemLogo = findViewById(R.id.imageview_logo_splash_screen);
        Animation animacao = AnimationUtils.loadAnimation(this, R.anim.rotate_animation);
        imagemLogo.startAnimation(animacao);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, ListaDeReceitas.class);
                startActivity(intent);
                finish();
            }
        }, TIME_OUT);
    }
}
