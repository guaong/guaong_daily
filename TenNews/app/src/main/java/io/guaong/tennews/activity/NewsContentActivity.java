package io.guaong.tennews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import io.guaong.tennews.R;

/**
 * Created by 关桐 on 2017/7/3.
 * 通过主界面打开不同内容，来显示内容的界面
 * 直接连接知乎网址
 */

public class NewsContentActivity extends NewsActivity {

    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String url = "http://daily.zhihu.com";
        WebView webView;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        webView = (WebView)findViewById(R.id.a_content_web);
        progressBar = (ProgressBar)findViewById(R.id.a_content_progress);
        webView.setVisibility(View.GONE);
        webView.loadUrl(url+getIntentData());
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                view.setVisibility(View.VISIBLE);
                super.onPageFinished(view, url);
            }
        });
    }

    private String getIntentData(){
        Intent intent = getIntent();
        return intent.getStringExtra("url");
    }
}
