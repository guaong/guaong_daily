package io.guaong.tennews.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by 关桐 on 2017/7/3.
 * 该项目所有activity都继承自各类
 * 该类为一个没有actionBar的activity
 */

public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cancelActionBar();
    }

    /**
     * 取消ActionBar
     */
    private void cancelActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }
    }
}
