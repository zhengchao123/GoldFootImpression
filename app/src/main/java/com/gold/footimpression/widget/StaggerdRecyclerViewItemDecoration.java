package com.gold.footimpression.widget;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class StaggerdRecyclerViewItemDecoration extends RecyclerView.ItemDecoration {

    private Context context;
    private int interval;

    public StaggerdRecyclerViewItemDecoration(Context context, int interval) {
        this.context = context;
        this.interval = interval;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
        // 获取item在span中的下标
        int position = params.getSpanIndex();
        View  viewItem = parent.findContainingItemView(view);

        if (position % 2 == 0) {
            outRect.left = interval;
            outRect.right = interval / 2;
        } else {
            // item为奇数位，设置其左间隔为5dp
            outRect.right = interval;
            outRect.left = interval / 2;
        }

//        outRect .left = 0;
//        outRect.right = 0 ;
//
//        // 下方间隔
//        outRect.bottom = -interval;
        outRect.top = interval;
    }

}
