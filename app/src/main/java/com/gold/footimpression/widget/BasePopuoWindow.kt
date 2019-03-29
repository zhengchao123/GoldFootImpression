package com.gold.footimpression.widget

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import decoration.scsowing.com.decoration.ui.event.OnItemClick

open class BasePopupWindow(context: Context) {
    var mContext: Context? = null
    var mFullScreen = true
    var mOrientation = Gravity.BOTTOM

    var mContentView: View? = null
    var mPopupWindow: PopupWindow? = null
    var mWidth = WindowManager.LayoutParams.MATCH_PARENT
    var mHeight = WindowManager.LayoutParams.MATCH_PARENT
    var popDatas = mutableListOf<String>()
    var mItemClick: OnItemClick? = null

    init {
        this.mContext = context
    }

    open fun showPop(parentView: View) {
    }

    open fun showPop(popdatas: MutableList<String>, parentView: View) {}
    open fun initPop() {}
    fun isShowing() = if (null != mPopupWindow) mPopupWindow!!.isShowing else false
    fun closePop() {
        if (null != mPopupWindow && mPopupWindow!!.isShowing) {
            mPopupWindow!!.dismiss()
        }
    }

    fun releasePop() {
        closePop()
        mPopupWindow == null
    }
}