package io.guaong.tennews.overlay;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.List;

import io.guaong.tennews.model.News;
import io.guaong.tennews.overlay.OverlayCardConfig;

public class OverlayCardCallback extends ItemTouchHelper.SimpleCallback {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<News> newsList;

    public OverlayCardCallback(RecyclerView rv, RecyclerView.Adapter adapter, List<News> newsList) {
        this(0, ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
                rv, adapter, newsList);
    }

    private OverlayCardCallback(int dragDirs, int swipeDirs
            , RecyclerView rv, RecyclerView.Adapter adapter, List<News> newsList) {
        super(dragDirs, swipeDirs);
        recyclerView = rv;
        this.adapter = adapter;
        this.newsList = newsList;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //获得移除的信息
        News removeNews = newsList.remove(viewHolder.getLayoutPosition());
        //将其添加到第一个
        newsList.add(0, removeNews);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        //从原始点到当前点之间距离
        double moveValue = Math.sqrt(dX * dX + dY * dY);
        //斜边比邻边
        double fraction = moveValue / getThreshold();
        //边界修正 最大为1
        if (fraction > 1) {
            fraction = 1;
        }
        //对每个ChildView进行缩放 位移
        int CardCount = recyclerView.getChildCount();
        for (int i = 0; i < CardCount; i++) {
            View view = recyclerView.getChildAt(i);
            //当前层上面有几层
            int overlayCardCount = CardCount - i - 1;
            if (overlayCardCount > 0) {
                view.setScaleX((float) (1 - OverlayCardConfig.SCALE_GAP * overlayCardCount +
                        fraction * OverlayCardConfig.SCALE_GAP));

                if (overlayCardCount < OverlayCardConfig.MAX_SHOW_CARD_NUM - 1) {
                    view.setScaleY((float) (1 - OverlayCardConfig.SCALE_GAP * overlayCardCount +
                            fraction * OverlayCardConfig.SCALE_GAP));
                    view.setTranslationY((float) (OverlayCardConfig.TRANS_Y_GAP * overlayCardCount -
                            fraction * OverlayCardConfig.TRANS_Y_GAP));
                }
            }
        }
    }

    /**
     * 计算出当手势滑动多远才可以收回的阈值
     * 即当滑动一半时可回收
     */
    private float getThreshold() {
        return recyclerView.getWidth() * 0.5f;
    }
}
