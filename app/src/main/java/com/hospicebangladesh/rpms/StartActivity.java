package com.hospicebangladesh.rpms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        View easySplashScreenView = new EasySplashScreen(StartActivity.this)
                .withFullScreen()
                .withTargetActivity(LoginActivity.class)
                .withSplashTimeOut(4000)
                //.withBackgroundResource(android.R.color.holo_green_dark)
                .withBackgroundResource(R.drawable.second)
                //.withHeaderText("Header")
                .withFooterText("Developed By 2A IT")
              //  .withBeforeLogoText("Hospice Bangladesh LTD")
              //  .withLogo(R.drawable.hoslogo)
               // .withAfterLogoText("Hospice Bangladesh LTD")
                .create();

        setContentView(easySplashScreenView);
    }
}
