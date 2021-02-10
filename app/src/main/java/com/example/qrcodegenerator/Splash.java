package com.example.qrcodegenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread t = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                }catch(Exception e){
                    e.printStackTrace();
                }finally{
                    Intent I = new Intent(Splash.this,MainActivity.class);
                    startActivity(I);
                }
            }

        };
        t.start();
    }
}

