package io.guaong.tennews.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Timer;
import java.util.TimerTask;

import io.guaong.tennews.R;

/**
 * Created by 关桐 on 2017/7/4.
 * 该activity是启动app的画面
 */

public class StartActivity extends NewsActivity {

    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_start);
        webView = (WebView)findViewById(R.id.a_start_web);
        webView.loadUrl("file:///android_asset/html/start.html");
        WebSettings webSettings = webView.getSettings();
        //能够识别html标签
        webSettings.setDomStorageEnabled(true);
        //能够识别js代码
        webSettings.setJavaScriptEnabled(true);
        //使用web view而不是系统浏览器
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        });
        //定时，5秒后启动下一activity
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(StartActivity.this, FallsNewsListActivity.class);
                startActivity(intent);
                finish();
            }
        }, 5000);
    }

    /*使返回键失效*/
    @Override
    public void onBackPressed() {}
}
