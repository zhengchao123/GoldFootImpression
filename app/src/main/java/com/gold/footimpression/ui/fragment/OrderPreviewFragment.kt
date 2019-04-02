package com.gold.footimpression.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableField
import androidx.databinding.library.baseAdapters.BR
import com.gold.footimpression.R
import com.gold.footimpression.bindingadapter.CommonAdapter
import com.gold.footimpression.databinding.OrderListFragmentBinding
import com.gold.footimpression.module.*
import com.gold.footimpression.net.CodeUtils
import com.gold.footimpression.net.utils.LogUtils
import com.gold.footimpression.presenter.OrderPresenter
import com.gold.footimpression.ui.base.BaseActivity
import com.gold.footimpression.ui.base.BaseFragment
import com.gold.footimpression.ui.event.EventHandler
import com.gold.footimpression.ui.event.OnItemClick
import com.gold.footimpression.utils.Utils

class OrderPreviewFragment : BaseFragment() {
    override fun getContentview() = R.layout.order_list_fragment

    private var mBinding: OrderListFragmentBinding? = null
    private var mOrderPresenter: OrderPresenter? = null
    private var orderDetailModule: OrderDetailModule? = null
    private var mOrderDetailLists = mutableListOf<OrderDetailModule>()
    private var mOrderHistoryDetailLists = mutableListOf<OrderDetailModule>()
    //增值服务
    private var mOrderIncreateLists = mutableListOf<OrderIncrementModule>()
    private var fromKey = ""
    //订单列表
    private var mCurrentOrdersAdapter: CommonAdapter<OrderDetailModule>? = null
    private var mCurrentHistoryOrdersAdapter: CommonAdapter<OrderDetailModule>? = null
    private var mOrderIncreateAdapter: CommonAdapter<OrderIncrementModule>? = null
    private var history = ObservableField<Boolean>(false)
    private var editZengzhi = ObservableField<Boolean>(false)

    override fun initBinding() {
        super.initBinding()
        mBinding = dataBinding as OrderListFragmentBinding
    }

    override fun initView() {
        super.initView()
        mBinding!!.history = history
        mBinding!!.editZengzhi = editZengzhi
        preView()
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
                        if (mOrderHistoryDetailLists.size == 0) {
                            loadHistoryList("0", "1000")
                        }
                    }
                    R.id.tv_current -> {
                        history.set(false)
                    }
                    R.id.tv_back->{
                        editZengzhi.set(false)
                    }
                }

            }
        }
        mBinding!!.click = click

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            preView()
        }
    }

    fun preView() {


        val data = this.arguments
        if (null != data && data.containsKey("fromKey")) {
            fromKey = data.getString("fromKey", "")
        }
        if (history.get()!!) {
            if (!TextUtils.equals(fromKey, "SERVICEEDIT") && mOrderHistoryDetailLists.size > 0) {
            } else {
                loadHistoryList("0", "10000")
            }
        } else {
            if (!TextUtils.equals(fromKey, "SERVICEEDIT") && mOrderDetailLists.size > 0) {
            } else {
                loadOrderList("0", "10000")

            }
        }

    }

    fun loadOrderList(start: String, limit: String) {


        if (!Utils.isNetworkConnected(mContext)) {
            toast(com.gold.footimpression.R.string.net_error)
        } else {
            (this.activity as BaseActivity).showProgressDialog { }
            mOrderPresenter!!.getOrders<MutableList<OrderDetailModule>>(start, limit) { code, msg, result ->
                (this.activity as BaseActivity).closeProgressDialog()

                if (CodeUtils.isSuccess(code)) {
                    mOrderDetailLists = result!!
                    mOrderDetailLists.add(0, OrderDetailModule(true))
                    mCurrentOrdersAdapter = mOrderDetailLists.putToAdapter()
                    mBinding!!.currentOrderAdapter = mCurrentOrdersAdapter
                } else {
                    toast(msg!!)
                }
            }
        }

    }

    fun loadHistoryList(start: String, limit: String) {


        if (!Utils.isNetworkConnected(mContext)) {
            toast(com.gold.footimpression.R.string.net_error)
        } else {
            (this.activity as BaseActivity).showProgressDialog { }
            mOrderPresenter!!.getHistory<MutableList<OrderDetailModule>>(start, limit) { code, msg, result ->
                (this.activity as BaseActivity).closeProgressDialog()

                if (CodeUtils.isSuccess(code)) {
                    mOrderHistoryDetailLists = result!!
                    mOrderHistoryDetailLists.add(0, OrderDetailModule(true))
                    mOrderHistoryDetailLists.forEach {
                        it.history = true
                    }
                    mCurrentHistoryOrdersAdapter = mOrderHistoryDetailLists.putToAdapter()
                    mBinding!!.historyOrderAdapter = mCurrentHistoryOrdersAdapter
                } else {
                    toast(msg!!)
                }
            }
        }

    }


    fun MutableList<OrderDetailModule>.putToAdapter(): CommonAdapter<OrderDetailModule> {

        return CommonAdapter(
            mContext!!, this, R.layout.item_order_list,
            BR.orderDetailModule, BR.click
        ).let {
            it.setOnItemClick(object : OnItemClick {
                override fun onItemClick(itemView: View, position: Int, instance: Any, viewid: Int) {
                    when (viewid) {
                        R.id.iv_back -> {
                            editZengzhi.set(false)
                        }
                        R.id.iv_gouwuche -> {

                            toast(" click item $position ishistory = ${(instance as OrderDetailModule).history} clickid = start")
                        }
                        R.id.ll_end -> {
                            editZengzhi.set(true)
                            if (mOrderIncreateLists.size == 0) {
                                loadZengzhi()
                            }
                        }
                    }

                }

                override fun onItemClick(itemView: View, position: Int) {
                    toast(" click item $position ")
                }
            })
            it
        }
    }

    fun MutableList<OrderIncrementModule>.putIncreToAdapter(): CommonAdapter<OrderIncrementModule> {

        return CommonAdapter(
            mContext!!, this, R.layout.item_edit_service,
            BR.orderIncrementModule, BR.click
        ).let {
            it.setOnItemClick(object : OnItemClick {
                override fun onItemClick(itemView: View, position: Int, instance: Any, viewid: Int) {
                    LogUtils.i(TAG, "  view location x=${itemView.x} y=${itemView.y}")
                    when (viewid) {
                        R.id.iv_add -> {
                            LogUtils.i(TAG, " click add")
                        }
                        R.id.iv_less -> {
                            LogUtils.i(TAG, " click less")
                        }
                    }
                }

                override fun onItemClick(itemView: View, position: Int) {
                }

        })
        it
    }
}

fun loadZengzhi() {

    if (!Utils.isNetworkConnected(mContext)) {
        toast(com.gold.footimpression.R.string.net_error)
    } else {
        (this.activity as BaseActivity).showProgressDialog { }
        mOrderPresenter!!.getZengzhi<MutableList<OrderIncrementModule>> { code, msg, result ->
            (this.activity as BaseActivity).closeProgressDialog()

            if (CodeUtils.isSuccess(code)) {
                mOrderIncreateLists = result!!.filterFuwuCode()
                mOrderIncreateAdapter = mOrderIncreateLists.putIncreToAdapter()
                mBinding!!.editZengzhifuwuAdapter = mOrderIncreateAdapter
            } else {
                toast(msg!!)
            }
        }
    }
}

//增值服务数据分组
fun MutableList<OrderIncrementModule>.filterFuwuCode(): MutableList<OrderIncrementModule> {
    val mapItem = mutableMapOf<String, MutableList<OrderIncrementModule>>()
    this.forEach {
        if (!mapItem.containsKey(it.zengzhiFuwuTypeName)) {
            val arry = mutableListOf<OrderIncrementModule>()
            arry.add(it)
            it.typeHead = true
            it.typeHeadName = it.zengzhiFuwuTypeName!!
            mapItem[it.zengzhiFuwuTypeName!!] = arry
        } else {
            mapItem[it.zengzhiFuwuTypeName]!!.add(it)
        }
    }
    val results = mutableListOf<OrderIncrementModule>()
    mapItem.forEach { item ->
        results.addAll(item.value)
    }
    return results
}

}
