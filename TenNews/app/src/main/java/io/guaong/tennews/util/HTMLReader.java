package io.guaong.tennews.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.guaong.tennews.model.News;


/**
 * Created by 关桐 on 2017/7/1.
 * 用于解析html代码
 */
public class HTMLReader {

    /**
     * 分析html
     * @param content html代码
     * @return 解析后
     */
    public static Document getDocument(String content){
        Document document;
        document = Jsoup.parse(content);
        return document;
    }

    /**
     * 得到html内容
     * @param url 网址
     * @return 网址全内容
     */
    public static String getDocumentContent(String url){
        String string;
        Document document;
        try {
            document = Jsoup.connect(url).get();
            string = document.html();
        } catch (IOException e) {
            return null;
        }
        return string;
    }

    public static String getBodyContent(String url){
        return getDocument(getDocumentContent(url)).body().html();
    }

    /**
     * 通过class得到元素内容
     * @param content html代码
     * @param className 要解析的标签的class
     * @return 得到所有为className的标签内容
     */
    public static List<String> getElementContentByClass(String content, String className){
        Elements elements = getDocument(content).getElementsByClass(className);
        List<String> contentList = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++){
            String text = elements.get(i).text();
            contentList.add(text);
        }
        return contentList;
    }

    /**
     * 得到标签内属性值
     * @param content html代码
     * @param className 要解析的标签的class
     * @param key 属性名
     * @return 得到所有className标签中key属性的值
     */
    public static List<String> getAttr(String content, String className, String key){
        List<String> list = new ArrayList<>();
        Elements elements = getDocument(content).getElementsByClass(className);
        for (int i = 0; i < elements.size(); i++){
            list.add(elements.get(i).attr(key));
        }
        return list;
    }

    /**
     * 将网络图片转换为bitmap
     * @param url url
     * @return bitmap
     */
    public static Bitmap getBitmap(String url){
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new URL(url).openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static boolean buildNewsList(List<News> newsList, String content){
        if (content == null){
            return false;
        }
        /**
         * 根据要求解析内容
         */
        List<String> titleList = HTMLReader.getElementContentByClass(content, "title");
        List<String> imgPathList = HTMLReader.getAttr(content, "preview-image", "src");
        List<String> urlList = HTMLReader.getAttr(content, "link-button", "href");
        //用于编号使用，在OverlayCard中是后面覆盖前面，因此使用相反编号
        int count = titleList.size();
        /**
         * 解析的内容加入newsList中
         */
        for (int i = 0; i < titleList.size(); i++){
            try {
                newsList.add(new News(titleList.get(i)
                        , BitmapFactory.decodeStream(new URL(imgPathList.get(i)).openConnection().getInputStream())
                        , count
                        , urlList.get(i)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            count--;
        }
        return true;
    }

}
