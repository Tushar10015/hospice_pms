package com.hospicebangladesh.rpms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.appus.splash.Splash;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Splash.Builder splash = new Splash.Builder(this, getSupportActionBar());


      /* Set custom color of background: */
      //  splash.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

       /* Set custom image for background:*/
     //   splash.setBackgroundImage(getResources().getDrawable(R.drawable.default_splash_image));

        /*Set custom image for splash:*/
       // splash.setSplashImage(getResources().getDrawable(R.drawable.test2));

       /* Set custom color of splash image:*/
      //  splash.setSplashImageColor(getResources().getColor(R.color.blue));

     /*//   Set custom splash pivot:
        splash.setPivotXOffset(getResources().getInteger(R.integer.my_x_pivot))
        splash.setPivotYOffset(getResources().getInteger(R.integer.my_y_pivot))
     //   Set custom splash animation type:

        splash.setAnimationType(Splash.AnimationType.TYPE_2)*/

        splash.perform();

    }
}
