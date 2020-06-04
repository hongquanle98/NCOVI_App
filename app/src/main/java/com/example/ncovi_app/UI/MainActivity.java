package com.example.ncovi_app.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.example.ncovi_app.FontChangeCrawler;
import com.example.ncovi_app.R;
import com.example.ncovi_app.UI.Home.HomeFragment;
import com.example.ncovi_app.UI.PhanAnh.PhanAnhFragment;
import com.example.ncovi_app.UI.Setting.SettingFragment;
import com.example.ncovi_app.UI.SucKhoe.SucKhoeFragment;
import com.example.ncovi_app.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//        getApplicationContext().getSharedPreferences("PREFERENCE", MODE_PRIVATE)
//                .edit()
//                .putInt("font", R.font.banh_mi_font)
//                .commit();
        Integer fontRes = getApplicationContext().getSharedPreferences("PREFERENCE", MODE_PRIVATE).getInt("font", R.font.default_font);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getApplicationContext(), fontRes);
        fontChanger.replaceFonts((ViewGroup)binding.getRoot());
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().remove("firstrun").commit();
        //getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().remove("default_font").commit();
        setEvent();
    }



    private void setEvent() {
        binding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.sucKhoe:
                        fragment = new SucKhoeFragment();
                        break;
                    case R.id.phanAnh:
                        fragment = new PhanAnhFragment();
                        break;
                    case R.id.setting:
                        fragment = new SettingFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                return true;
            }
        });
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

}
