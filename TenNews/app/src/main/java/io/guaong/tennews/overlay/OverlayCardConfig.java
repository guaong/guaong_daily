package io.guaong.tennews.overlay;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by 关桐 on 2017/7/1.
 *
 */
 public class OverlayCardConfig {

    //能够同时显示的卡片的最大数量
    static int MAX_SHOW_CARD_NUM;
    //卡片比例
    static float SCALE_GAP;
    //底部偏差
    static int TRANS_Y_GAP;

    public static void initConfig(Context context) {
        MAX_SHOW_CARD_NUM = 3;
        SCALE_GAP = 0.04f;
        //仿照写的具体效果未知
        TRANS_Y_GAP = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, context.getResources().getDisplayMetrics());
    }
}
