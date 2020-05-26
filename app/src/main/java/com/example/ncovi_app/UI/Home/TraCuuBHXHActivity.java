package com.example.ncovi_app.UI.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.example.ncovi_app.R;
import com.example.ncovi_app.databinding.ActivityTraCuuBhxhBinding;

public class TraCuuBHXHActivity extends AppCompatActivity {
    ActivityTraCuuBhxhBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_tra_cuu_bhxh);

        binding.webView.setWebViewClient(new WebViewClient());
        binding.webView.loadUrl("https://baohiemxahoi.gov.vn/tracuu/pages/tra-cuu-ho-gia-dinh.aspx");

        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //if (item.getItemId() == android.R.id.) {
            finish();
            //return true;
        //}
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
