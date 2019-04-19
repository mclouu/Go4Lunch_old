/*
 * Created by Romain Mathieu => https://github.com/mclouu
 */

package com.romain.mathieu.go4lunch.controller.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.romain.mathieu.go4lunch.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;


public class WebViewActivity extends AppCompatActivity {

    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    private String url;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setTitle(getString(R.string.app_name));

        Intent intent = getIntent();
        if (intent != null) {


            if (intent.hasExtra("website")) {
                url = intent.getStringExtra("website");
            }

            webView.setWebViewClient(new Callback());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(url);
        }

    }

    private class Callback extends WebViewClient {  //HERE IS THE MAIN CHANGE.

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }

    }
}