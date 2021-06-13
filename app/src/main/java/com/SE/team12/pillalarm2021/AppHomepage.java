package com.SE.team12.pillalarm2021;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class AppHomepage extends AppCompatActivity {

    WebView browser;

    /**
     * @brief make web view to visit app home page
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        browser = findViewById(R.id.webkit);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.setWebViewClient(new WebViewClient());
        browser.loadUrl("http://" + "49.170.22.142:8193");
    }
}
