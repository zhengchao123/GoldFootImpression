package com.gold.footimpression.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import com.gold.footimpression.R
import com.gold.footimpression.databinding.LayoutTitleBinding
import com.gold.footimpression.net.utils.DensyUtils
import com.gold.footimpression.net.utils.LogUtils
import com.gold.footimpression.ui.event.EventHandler
import com.qmuiteam.qmui.util.QMUIStatusBarHelper

class TitleBar(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : CardView(context, attrs, defStyleAttr) {
    var mContext: Context? = null
    var statusView: View? = null
    var titleText = ObservableField<String>()
    val TAG = this.javaClass.simpleName

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)


    init {
        mContext = context
        initView()
    }

    private var mBinding: LayoutTitleBinding? = null

    fun initView() {
        this.cardElevation = DensyUtils.dp2px(mContext!!,5.0f ).toFloat()
        this.radius = 0f

        mBinding = DataBindingUtil.inflate<LayoutTitleBinding>(LayoutInflater.from(mContext), R.layout.layout_title, null, false)
        statusView = View(mContext)
        mBinding!!.rlContainer.setBackgroundColor(ContextCompat.getColor(mContext!!, R.color.title_bar_bg_color))
        statusView!!.background = mBinding!!.rlContainer.background
        mBinding!!.llContainer.addView(statusView, 0, FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, QMUIStatusBarHelper.getStatusbarHeight(mContext)))
        addView(mBinding!!.root, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT))

        titleText.set("TiTle")
        mBinding!!.title = titleText
    }

    fun setTitle(title: String) {
        LogUtils.i(TAG, "=== setTitle $title")
        titleText.set(title)
    }

    fun initListener(clickEvent: EventHandler) {
        if (null != clickEvent) {
            mBinding!!.click = clickEvent
        }

    }

    fun immersiveTitle(immersive: Boolean = true) {
        statusView!!.visibility = if (immersive) View.VISIBLE else View.GONE
    }

}