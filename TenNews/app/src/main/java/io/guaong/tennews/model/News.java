package io.guaong.tennews.model;

import android.graphics.Bitmap;

/**
 * 新闻实体类
 * 包含：
 */
public class News {

    private String title;
    private Bitmap img;
    private int index;
    private String url;

    public News(String title, Bitmap img, int index, String url){
        this.title = title;
        this.img = img;
        this.index = index;
        this.url = url;
    }

    public Bitmap getImg() {
        return img;
    }

    public String getTitle() {
        return title;
    }

    public int getIndex() {
        return index;
    }

    public String getUrl() {
        return url;
    }
}
