package io.guaong.tennews.overlay;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import io.guaong.tennews.overlay.OverlayCardConfig;

/**
 * Created by 关桐 on 2017/7/1.
 *
 */
public class OverlayCardLayoutManager extends RecyclerView.LayoutManager{

    /**
     * 生成默认布局参数
     */
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        /**
         * 此处使用WRAP_CONTENT或MATCH_PARENT没有什么区别
         * 说白了就是子视图相对于父视图的关系
         */
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 关于子布局
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //分离和删除附加视图
        detachAndScrapAttachedViews(recycler);
        //当前子视图数量
        int CardsCount = getItemCount();
        if (CardsCount < 1) {
            return;
        }
        //不能显示的卡片数目
        int invisibleCardsCount;
        //边界处理
        if (CardsCount < OverlayCardConfig.MAX_SHOW_CARD_NUM) {
            invisibleCardsCount = 0;
        } else {
            invisibleCardsCount = CardsCount - OverlayCardConfig.MAX_SHOW_CARD_NUM;
        }
        //绘制可见的卡片
        for (int position = invisibleCardsCount; position < CardsCount; position++) {
            View view = recycler.getViewForPosition(position);
            addView(view);
            //子视图页边空白
            measureChildWithMargins(view, 0, 0);
            int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
            int heightSpace = getHeight() - getDecoratedMeasuredHeight(view);
            /**
             * 版面装饰，居中处理，即设置子视图距离左上右下的距离
             * 注意距离都是以左上为参考
             */
            layoutDecoratedWithMargins(view, widthSpace / 2, heightSpace / 2,
                    widthSpace / 2 + getDecoratedMeasuredWidth(view),
                    heightSpace / 2 + getDecoratedMeasuredHeight(view));
            /**
             * 从可见的最底层卡片开始
             */
            //当前层卡片之上卡片数量，即需要缩小多少倍
            int overlayCardCount = CardsCount - position - 1;
            //顶层外卡片变化
            if (overlayCardCount > 0) {
                //每一层都需要X方向的放大
                view.setScaleX(1 - OverlayCardConfig.SCALE_GAP * overlayCardCount);
                //前N层，依次向下位移和Y方向的放大
                view.setTranslationY(OverlayCardConfig.TRANS_Y_GAP * overlayCardCount);
                view.setScaleY(1 - OverlayCardConfig.SCALE_GAP * overlayCardCount);
            }
        }
    }
}
