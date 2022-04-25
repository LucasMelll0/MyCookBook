package com.example.mycookbook.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.example.mycookbook.R;

public class SplashScreen  extends Activity {

    private static int TIME_OUT = 3000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

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
