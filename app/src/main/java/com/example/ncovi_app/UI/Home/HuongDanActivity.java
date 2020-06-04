package com.example.ncovi_app.UI.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.example.ncovi_app.FontChangeCrawler;
import com.example.ncovi_app.R;
import com.example.ncovi_app.databinding.ActivityHuongDanBinding;

public class HuongDanActivity extends AppCompatActivity {
    ActivityHuongDanBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_huong_dan);
        Integer fontRes = getApplicationContext().getSharedPreferences("PREFERENCE", MODE_PRIVATE).getInt("font", R.font.default_font);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getApplicationContext(), fontRes);
        fontChanger.replaceFonts((ViewGroup)binding.getRoot());
        binding.webView.setWebViewClient(new WebViewClient());
        binding.webView.loadUrl("https://ncovi.vnpt.vn/views/huongdan.html");

        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(binding.webView.canGoBack()){
            binding.webView.goBack();
        }else{
            super.onBackPressed();
        }
    }
}
