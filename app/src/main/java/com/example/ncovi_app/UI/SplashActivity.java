package com.example.ncovi_app.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.example.ncovi_app.R;
import com.example.ncovi_app.UI.Home.MapActivity;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        String language = getApplicationContext().getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("language", Locale.getDefault().getLanguage());
        String country = getApplicationContext().getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("country", Locale.getDefault().getCountry());
        onChangeLanguage(getApplicationContext(), new Locale(language, country));
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }, 500);
    }
    private void onChangeLanguage(Context context, Locale locale){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        Configuration configuration = new Configuration();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            configuration.setLocale(locale);
        }else{
            configuration.locale = locale;
        }

        getResources().updateConfiguration(configuration, displayMetrics);
        context.getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .edit()
                .putString("language", locale.getLanguage())
                .commit();
        context.getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .edit()
                .putString("country", locale.getCountry())
                .commit();
    }

}
