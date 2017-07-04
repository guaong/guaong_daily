package io.guaong.tennews.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.guaong.tennews.R;
import io.guaong.tennews.model.News;
import io.guaong.tennews.util.HTMLReader;
import io.guaong.tennews.util.ImageUtil;
import io.guaong.tennews.util.WindowHelper;

/**
 * Created by 关桐 on 2017/7/4.
 * 现在使用的主界面，简单的列表布局
 */

public class FallsNewsListActivity extends NewsActivity {

    private RecyclerView recyclerView;
    private List<News> newsList;
    private Typeface typeFaceBody;
    private Typeface typeFaceHead;
    private ProgressBar progressBar;
    private Bitmap bitmap;
    private TextView headText;
    private boolean isFirstBack = true;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list_falls);
        recyclerView = (RecyclerView)findViewById(R.id.a_list_falls_recycler);
        progressBar = (ProgressBar)findViewById(R.id.a_list_falls_progress);
        headText = (TextView)findViewById(R.id.a_list_falls_text);
        newsList = new ArrayList<>();
        setTextViewFontStyle();
        new ReceiveTask().execute();
    }

    @Override
    public void onBackPressed() {
        timer = new Timer();
        if (isFirstBack){
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            isFirstBack = false;
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    isFirstBack = true;
                }
            };
            timer.schedule(timerTask, 2000);
        }else {
            finish();
            System.exit(0);
            Process.killProcess(Process.myPid());
        }
    }

    private void setRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new FallsAdapter(newsList));
    }

    /**
     * 设置字体样式
     * 在onCreate()中直接加载，而不是在需要字体时加载，优化性能
     */
    private void setTextViewFontStyle(){
        typeFaceBody = Typeface.createFromAsset(getAssets(), "fonts/xinsong.ttf");
        typeFaceHead = Typeface.createFromAsset(getAssets(), "fonts/flotp.TTF");
        headText.setTypeface(typeFaceBody);
    }

    class ReceiveTask extends AsyncTask<Void, Integer, Boolean>{

        @Override
        protected Boolean doInBackground(Void... params) {
            bitmap = HTMLReader.getBitmap(HTMLReader.getBodyContent("http://guolin.tech/api/bing_pic"));
            return HTMLReader
                    .buildNewsList(newsList, HTMLReader.getDocumentContent("https://guaong.github.io"));
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressBar.setVisibility(View.GONE);
            if (aBoolean){
                setRecyclerView();
            }else {
                Toast.makeText(FallsNewsListActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 使用两套布局，一个是头，用于展示一张图片
     * 而一个是主体，即用来加载每一条文章
     */
    class FallsAdapter extends RecyclerView.Adapter<FallsAdapter.FallsHolder>{

        private List<News> newsList;
        private final int ITEM_TYPE_HEAD = 1;
        private final int ITEM_TYPE_BODY = 2;

        class FallsHolder extends RecyclerView.ViewHolder{

            private ImageView bodyImgView;
            private TextView textView;
            private RelativeLayout relativeLayout;
            private ImageView headImgView;

            FallsHolder(View view){
                super(view);
                bodyImgView = (ImageView)view.findViewById(R.id.i_list_falls_img);
                textView = (TextView)view.findViewById(R.id.i_list_falls_text);
                relativeLayout = (RelativeLayout)view.findViewById(R.id.i_list_falls_layout);
                headImgView = (ImageView)view.findViewById(R.id.i_list_falls_head_img);
            }

        }

        FallsAdapter(List<News> newsList){
            this.newsList = newsList;
        }

        @Override
        public FallsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            if (viewType == ITEM_TYPE_HEAD){
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_news_list_falls_head, parent, false);
            }else {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_news_list_falls, parent, false);
            }
            return new FallsHolder(view);
        }

        @Override
        public void onBindViewHolder(FallsHolder holder, int position) {
            if (position == 0){
                holder.headImgView.setImageBitmap(bitmap);
                ImageUtil.setImgViewSize(holder.headImgView
                    , WindowHelper.getWindowSize(FallsNewsListActivity.this).x * 1080 / 1920
                    , WindowHelper.getWindowSize(FallsNewsListActivity.this).x);
            }else {
                holder.textView.setText(newsList.get(position-1).getTitle());
                holder.bodyImgView.setImageBitmap(newsList.get(position-1).getImg());
                holder.textView.setTypeface(typeFaceBody);
                ImageUtil.setImgViewSize(holder.bodyImgView
                        , WindowHelper.getWindowSize(FallsNewsListActivity.this).x / 4
                        , WindowHelper.getWindowSize(FallsNewsListActivity.this).x / 4);
                holder.relativeLayout
                        .setOnClickListener(new NewsOnClickListener(newsList.get(position-1).getUrl()));
            }
        }

        @Override
        public int getItemCount() {
            return newsList.size()+1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0){
                return ITEM_TYPE_HEAD;
            }else {
                return ITEM_TYPE_BODY;
            }
        }

        /**
         * 点击监听
         */
        class NewsOnClickListener implements View.OnClickListener{

            private String url;

            NewsOnClickListener(String url){
                this.url = url;
            }

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FallsNewsListActivity.this, NewsContentActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        }

    }
}
