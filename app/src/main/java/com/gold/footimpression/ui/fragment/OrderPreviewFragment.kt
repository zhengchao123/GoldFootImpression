package com.gold.footimpression.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableField
import com.gold.footimpression.R
import com.gold.footimpression.databinding.OrderListFragmentBinding
import com.gold.footimpression.module.OrderDetailModule
import com.gold.footimpression.presenter.OrderPresenter
import com.gold.footimpression.ui.base.BaseFragment
import com.gold.footimpression.ui.event.EventHandler

class OrderPreviewFragment : BaseFragment() {
    override fun getContentview() = R.layout.order_list_fragment

    private var mBinding: OrderListFragmentBinding? = null
    private var mOrderPresenter: OrderPresenter? = null
    private var mOrderDetailLists = mutableListOf<OrderDetailModule>()
    private var fromKey = ""

    private var history = ObservableField<Boolean>(false)

    override fun initBinding() {
        super.initBinding()
        mBinding = dataBinding as OrderListFragmentBinding
    }

    override fun initView() {
        super.initView()
        mBinding!!.history = history
    }

    override fun initData() {
        super.initData()
        mOrderPresenter = OrderPresenter.getInstance(this.activity)
    }

    override fun initListener() {
        super.initListener()

        val click = object : EventHandler(mContext, false) {
            override fun onClickView(view: View?) {
                when (view!!.id) {
                    R.id.tv_history -> {
                        history.set(true)
                    }
                    R.id.tv_current -> {
                        history.set(false)
                    }
                }

            }
        }
        mBinding!!.click = click

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
        }
    }

    fun preView() {


        val data = this.arguments
        if (null != data && data.containsKey("fromKey")) {
            fromKey = data.getString("fromKey", "")
        }
        if (!TextUtils.equals(fromKey, "SERVICEEDIT") && mOrderDetailLists.size > 0) {
//直接加载
        } else {
//请求数据
        }
    }

    fun loadOrderList() {}
}
