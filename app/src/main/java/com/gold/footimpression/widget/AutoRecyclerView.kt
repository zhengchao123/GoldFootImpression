package com.gold.footimpression.widget

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.*
import com.gold.footimpression.R
import com.gold.footimpression.net.utils.DensyUtils
import com.squareup.picasso.Picasso

open class AutoRecyclerView(context: Context, attrs: AttributeSet?, defStyle: Int) : RecyclerView(context, attrs, defStyle) {
    var di: DividerItemDecoration? = null
    var mContext: Context? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        var tar = context.obtainStyledAttributes(attrs, R.styleable.loadimage)
        di = DividerItemDecoration(context, tar.getInt(R.styleable.loadimage_orientation, 1))
        val hasFixSize = tar.getBoolean(R.styleable.loadimage_has_fix_size, false)
        val diliverStyle = tar.getInt(R.styleable.loadimage_diliver_style, 0)
        di!!.setDrawable(ContextCompat.getDrawable(context, when (diliverStyle) {
            0 -> R.drawable.recyclerview_diliver_line_white_15
            1 -> R.drawable.recyclerview_diliver_line_f7f7f7_2
            else -> R.drawable.recyclerview_diliver_line_transparent_15
        })!!)
//        di!!.setDrawable(ContextCompat.getDrawable(context, if (diliverStyle == 0) R.drawable.recyclerview_diliver_line_white_15 else R.drawable.recyclerview_diliver_line_f7f7f7_2)!!)
        setHasFixedSize(hasFixSize)
        this.mContext = context
        this.addItemDecoration(di!!)
        initLinearLayoutHORIZONTALOrVERTICAL(context,
                tar.getInt(R.styleable.loadimage_orientation, 1),
                tar.getInt(R.styleable.loadimage_view_style, 0),
                tar.getInt(R.styleable.loadimage_column, 1)
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
}