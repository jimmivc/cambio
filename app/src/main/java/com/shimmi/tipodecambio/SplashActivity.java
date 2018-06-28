package com.shimmi.tipodecambio;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                openActivity();
            }
        }, 2000);   //5 seconds
    }

    private void openActivity(){
        Intent main = new Intent(this,MainActivity.class);
        startActivity(main);
    }
}
