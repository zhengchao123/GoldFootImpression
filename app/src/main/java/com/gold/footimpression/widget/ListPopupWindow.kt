package com.gold.footimpression.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import com.gold.footimpression.BR
import com.gold.footimpression.R
import com.gold.footimpression.bindingadapter.CommonAdapter
import com.gold.footimpression.databinding.LayoutListPopViewBinding
import com.gold.footimpression.ui.event.EventHandler
import com.gold.footimpression.utils.ViewUtils

class ListPopupWindow(context: Context) : BasePopupWindow(context) {
    var viewBinding: com.gold.footimpression.databinding.LayoutListPopViewBinding? = null
    var adapterPop: CommonAdapter<String>? = null
//    var popDatas = mutableListOf<String>()

    override fun showPop(parentView: View) {
        showPop(popDatas, parentView)
    }

    override fun showPop(popdatas: MutableList<String>, parentView: View) {
        if (null == popdatas || popdatas.size == 0) {
            return
        } else {
            if (null == mPopupWindow) {
                initPop()
            }
            if (mPopupWindow!!.isShowing) {
                mPopupWindow!!.dismiss()
            }
            mPopupWindow!!.showAtLocation(parentView, mOrientation, 0, 0)
        }
    }


    override fun initPop() {
        mPopupWindow = PopupWindow(mWidth, mHeight)
        ViewUtils.fitPopupWindowOverStatusBar(mPopupWindow!!, true)
        viewBinding = DataBindingUtil.inflate<LayoutListPopViewBinding>(
            LayoutInflater.from(mContext),
            R.layout.layout_list_pop_view,
            null,
            false
        )
        viewBinding!!.click = object : EventHandler(mContext, false) {
            override fun onClickView(view: View?) {
                super.onClickView(view)
                closePop()
            }
        }
        adapterPop = CommonAdapter(mContext!!, popDatas, R.layout.item_pop_list, BR.content)
        if (null != mItemClick) {
            adapterPop!!.setOnItemClick(mItemClick!!)
        }

        viewBinding?.apply {
            if (null == viewBinding!!.adapter) {
                viewBinding!!.adapter = adapterPop
            } else {
                adapterPop!!.update(popDatas)
            }

            mContentView = viewBinding!!.root
        }
        mPopupWindow?.apply {
            isTouchable = true;
            isOutsideTouchable = true
            setContentView(mContentView)
        }

    }


}