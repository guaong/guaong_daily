package io.guaong.tennews.util;

import android.app.Activity;
import android.graphics.Point;
import android.util.DisplayMetrics;

/**
 * Created by 关桐 on 2017/7/3.
 * 用于得到一些关于手机屏幕信息的工具类
 */

public class WindowHelper {

    /**
     * 获取设备尺寸
     * @return 用Point返回设备的width和height
     */
    public static Point getWindowSize(Activity activity){
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return new Point(dm.widthPixels, dm.heightPixels);
    }

}