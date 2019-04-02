package com.gold.footimpression.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.*
import com.gold.footimpression.net.utils.DensyUtils
import com.squareup.picasso.Picasso


open class AutoRecyclerView(context: Context, attrs: AttributeSet?, defStyle: Int) :
    RecyclerView(context, attrs, defStyle) {
    var di: DividerItemDecoration? = null
    var mContext: Context? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        var tar = context.obtainStyledAttributes(attrs, com.gold.footimpression.R.styleable.loadimage)
        di = DividerItemDecoration(context, tar.getInt(com.gold.footimpression.R.styleable.loadimage_orientation, 1))
        val hasFixSize = tar.getBoolean(com.gold.footimpression.R.styleable.loadimage_has_fix_size, false)
        val diliverStyle = tar.getInt(com.gold.footimpression.R.styleable.loadimage_diliver_style, 0)
        di!!.setDrawable(
            ContextCompat.getDrawable(
                context, when (diliverStyle) {
                    0 -> com.gold.footimpression.R.drawable.recyclerview_diliver_line_white_15
                    1 -> com.gold.footimpression.R.drawable.recyclerview_diliver_line_f7f7f7_2
                    2 -> com.gold.footimpression.R.drawable.recyclerview_diliver_line_gray
                    else -> com.gold.footimpression.R.drawable.recyclerview_diliver_line_transparent_15
                }
            )!!
        )
//        di!!.setDrawable(ContextCompat.getDrawable(context, if (diliverStyle == 0) R.drawable.recyclerview_diliver_line_white_15 else R.drawable.recyclerview_diliver_line_f7f7f7_2)!!)
        setHasFixedSize(hasFixSize)
        this.mContext = context
        this.addItemDecoration(di!!)
        initLinearLayoutHORIZONTALOrVERTICAL(
            context,
            tar.getInt(com.gold.footimpression.R.styleable.loadimage_orientation, 1),
            tar.getInt(com.gold.footimpression.R.styleable.loadimage_view_style, 0),
            tar.getInt(com.gold.footimpression.R.styleable.loadimage_column, 1)
        )

    }

    fun initLinearLayoutHORIZONTALOrVERTICAL(context: Context, orientation: Int, view_style: Int, conlumn: Int) {

        when (view_style) {
            0 -> {
                val layoutManager = object : LinearLayoutManager(context) {
                    override fun canScrollVertically() = true
                }
                layoutManager.orientation = if (orientation == 0) HORIZONTAL else VERTICAL
                this.layoutManager = layoutManager
            }
            1 -> {
                val layoutManager = GridLayoutManager(context, conlumn)
                layoutManager.orientation = if (orientation == 0) HORIZONTAL else VERTICAL
                this.layoutManager = layoutManager
            }
            2 -> {
                val layoutManager = StaggeredGridLayoutManager(conlumn, RecyclerView.VERTICAL)
                layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
                this.removeItemDecoration(di!!)
                this.addItemDecoration(StaggerdRecyclerViewItemDecoration(context, DensyUtils.dp2px(mContext!!, 10f)))
                this.layoutManager = layoutManager
            }

        }

    }

    fun setconlumn(conlumn: Int) {
        val layoutManager = this.layoutManager as GridLayoutManager
        layoutManager.spanCount = conlumn
        this.layoutManager = layoutManager
    }

    override fun onScrollStateChanged(newState: Int) {
        super.onScrollStateChanged(newState)
        if (this.layoutManager is StaggeredGridLayoutManager) {
            var first = IntArray(2)
            (this.layoutManager as StaggeredGridLayoutManager).findFirstCompletelyVisibleItemPositions(first)
//            if (newState == RecyclerView.SCROLL_STATE_IDLE && (first[0] == 1 || first[1] == 1)) {
////                (layoutManager as StaggeredGridLayoutManager).invalidateSpanAssignments()
////                this.adapter!!.notifyDataSetChanged()
//            }
//            (this.layoutManager as StaggeredGridLayoutManager).invalidateSpanAssignments()
//            invalidateItemDecorations();
        }

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            Picasso.with(mContext!!).resumeTag("round_corner_recycler_img_url");
        } else {
            Picasso.with(mContext!!).pauseTag("round_corner_recycler_img_url");
        }
    }

    private var mLastXIntercept: Float = 0f
    private var mLastYIntercept: Float = 0f
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {


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
                if (Math.abs(deltaX) < Math.abs(deltaY)) {
                    parent.requestDisallowInterceptTouchEvent(true)
                } else {
                    parent.requestDisallowInterceptTouchEvent(false)
//                    intercepted = false;
                }
            }
            MotionEvent.ACTION_UP -> {
                intercepted = false
            }
        }
        mLastXIntercept = x
        mLastYIntercept = y
//        return intercepted
        return super.dispatchTouchEvent(ev)


//        val x = ev.x.toInt()
//        val y = ev.y.toInt()
//        when (ev.action) {
//            MotionEvent.ACTION_DOWN -> {
//                parent.requestDisallowInterceptTouchEvent(true)
//            }
//            MotionEvent.ACTION_MOVE -> {
//                parent.requestDisallowInterceptTouchEvent(true)
//            }
//            MotionEvent.ACTION_UP -> {
//            }
//        }
//        return super.dispatchTouchEvent(ev)
    }


}