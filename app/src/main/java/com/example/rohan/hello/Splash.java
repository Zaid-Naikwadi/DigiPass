package com.example.rohan.hello;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        getSupportActionBar().hide();
        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep( 3700);
                    Intent intent = new Intent(Splash.this,UserTypeLoginActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception ex) {

                }
            }
        };
        thread.start();
    }
}
