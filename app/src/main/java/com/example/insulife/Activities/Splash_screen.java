package com.example.insulife.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.insulife.Activities.Login_activity;
import com.example.insulife.R;


public class Splash_screen extends AppCompatActivity {
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> {
            Intent i;
            preferences =getSharedPreferences("onIntroScreen",MODE_PRIVATE);
            boolean isFirst=preferences.getBoolean("firstTime",true);

            if(isFirst){
                preferences.edit().putBoolean("firstTime",false).apply();
                i=new Intent(this, IntroActivity.class);
            }else i=new Intent(this, Login_activity.class);

            startActivity(i);
            finish();
        },1000);
    }
}