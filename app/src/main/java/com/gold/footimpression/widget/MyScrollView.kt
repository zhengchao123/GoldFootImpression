package com.gold.footimpression.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.HorizontalScrollView
import androidx.recyclerview.widget.DividerItemDecoration
import android.R.attr.y
import android.R.attr.x




open class MyScrollView(context: Context, attrs: AttributeSet?, defStyle: Int) : HorizontalScrollView(context, attrs, defStyle) {
    var di: DividerItemDecoration? = null
    var mContext: Context? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)


    private var mLastXIntercept: Float = 0f
    private var mLastYIntercept: Float = 0f
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var intercepted = false
        val x = ev.x
        val y = ev.y
        val action = ev.action and MotionEvent.ACTION_MASK
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                intercepted = false
                //初始化mActivePointerId
                super.onInterceptTouchEvent(ev)
            }
            MotionEvent.ACTION_MOVE -> {
                //横坐标位移增量
                val deltaX = x - mLastXIntercept
                //纵坐标位移增量
                val deltaY = y - mLastYIntercept
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    intercepted = true;
                } else {
                    intercepted = false;
                }
            }
            MotionEvent.ACTION_UP -> {
                intercepted = false
            }
        }
        mLastXIntercept = x
        mLastYIntercept = y
        return intercepted
    }

}