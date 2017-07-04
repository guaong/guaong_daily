package io.guaong.tennews.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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

import io.guaong.tennews.util.HTMLReader;
import io.guaong.tennews.model.News;
import io.guaong.tennews.overlay.OverlayCardCallback;
import io.guaong.tennews.overlay.OverlayCardConfig;
import io.guaong.tennews.overlay.OverlayCardLayoutManager;
import io.guaong.tennews.R;
import io.guaong.tennews.util.WindowHelper;

/**
 * 第一版使用的activity
 * 该activity使用overlayCard布局
 * 即卡片叠加布局
 * 现已不在使用
 */
public class OverlayNewsListActivity extends NewsActivity {

    private RecyclerView recyclerView;
    private Typeface typeFace;
    private List<News> newsList;
    private ProgressBar progressBar;
    private boolean isOpenActivity = false;
    private OverlayCardAdapter overlayCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list_overlay);
        recyclerView = (RecyclerView)findViewById(R.id.a_list_overlay_recycler);
        progressBar = (ProgressBar)findViewById(R.id.a_list_overlay_progress);
        new ReceiveTask().execute();
        newsList = new ArrayList<>();
        setTextViewFontStyle();
        OverlayCardConfig.initConfig(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isOpenActivity){
            isOpenActivity = false;
            overlayCardAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 设置字体样式
     * 在onCreate()中直接加载，而不是在需要字体时加载，优化性能
     */
    private void setTextViewFontStyle(){
        typeFace = Typeface.createFromAsset(getAssets(), "fonts/xinsong.ttf");
    }

    /**
     * 设置RecyclerView
     * 使用自定义的OverlayCardLayoutManager
     */
    private void setRecyclerView(){
        //设置布局处理者
        recyclerView.setLayoutManager(new OverlayCardLayoutManager());
        //实例化适配器
        overlayCardAdapter = new OverlayCardAdapter(newsList);
        //设置适配器
        recyclerView.setAdapter(overlayCardAdapter);
        //回调
        OverlayCardCallback overlayCardCallback = new OverlayCardCallback(recyclerView, overlayCardAdapter, newsList);
        //滑动手势
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(overlayCardCallback);
        //绑定RecyclerView
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    /**
     * 给RecyclerView的适配器
     */
    class OverlayCardAdapter extends RecyclerView.Adapter<OverlayCardAdapter.OverlayCardHolder> {

        private List<News> newsList;

        class OverlayCardHolder extends RecyclerView.ViewHolder{

            private ImageView imageView;
            private TextView titleText;
            private TextView pageText;
            private RelativeLayout relativeLayout;

            OverlayCardHolder(View itemView) {
                super(itemView);
                imageView = (ImageView)itemView.findViewById(R.id.i_list_overlay_img);
                titleText = (TextView)itemView.findViewById(R.id.i_list_overlay_title);
                pageText = (TextView)itemView.findViewById(R.id.i_list_overlay_page);
                relativeLayout = (RelativeLayout)itemView.findViewById(R.id.i_list_overlay_relative);
            }
        }

        OverlayCardAdapter(List<News> newsList){
            this.newsList = newsList;
        }

        @Override
        public OverlayCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater
                    .from(parent.getContext()).inflate(R.layout.item_news_list_overlay, parent, false);
            return new OverlayCardHolder(view);
        }

        @Override
        public void onBindViewHolder(OverlayCardHolder holder, int position) {
            holder.titleText.setText(newsList.get(position).getTitle());
            holder.imageView.setImageBitmap(newsList.get(position).getImg());
            holder.titleText.setTypeface(typeFace);
            setImgViewSize(holder.imageView);
            holder.pageText.setText("- " + newsList.get(position).getIndex() + " / " + newsList.size() + " -");
            holder.relativeLayout.setOnClickListener(new NewsOnClickListener(newsList.get(position).getUrl(), position));
        }

        @Override
        public int getItemCount() {
            return newsList.size();
        }

        private void setImgViewSize(ImageView imageView){
            int width, height;
            width = WindowHelper.getWindowSize(OverlayNewsListActivity.this).x - 20;
            height = width - 20;
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            layoutParams.height = height;
            layoutParams.width = width;
            imageView.setLayoutParams(layoutParams);
        }

        /**
         * 点击监听
         */
        class NewsOnClickListener implements View.OnClickListener{

            private String url;
            private int position;

            NewsOnClickListener(String url, int position){
                this.url = url;
                this.position = position;
            }

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OverlayNewsListActivity.this, NewsContentActivity.class);
                intent.putExtra("url", url);
                //获得移除的信息
                News removeNews = newsList.remove(position);
                //将其添加到第一个
                newsList.add(0, removeNews);
                isOpenActivity = true;
                startActivity(intent);
            }
        }

    }

    /**
     * 消息传递
     */
    class ReceiveTask extends AsyncTask<Void, Integer, Boolean>{

        @Override
        protected Boolean doInBackground(Void... params) {
            return HTMLReader.buildNewsList(newsList, HTMLReader.getDocumentContent("http://guaong.github.io"));
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressBar.setVisibility(View.GONE);
            if (aBoolean){
                setRecyclerView();
            }else {
                Toast.makeText(OverlayNewsListActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
