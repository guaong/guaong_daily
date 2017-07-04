package io.guaong.tennews.util;

import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * Created by 关桐 on 2017/7/4.
 */

public class ImageUtil {

    public static void setImgViewSize(ImageView imageView, int height, int width){
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.height = height;
        layoutParams.width = width;
        imageView.setLayoutParams(layoutParams);
    }

}
