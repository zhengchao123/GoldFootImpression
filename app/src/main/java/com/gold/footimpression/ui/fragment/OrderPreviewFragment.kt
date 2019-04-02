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
import com.gold.footimpression.module.OrderDetailModule
import com.gold.footimpression.module.RoomAndCardModule
import com.gold.footimpression.module.ServiceItemModule
import com.gold.footimpression.net.CodeUtils
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
    private var fromKey = ""
    //订单列表
    private var mCurrentOrdersAdapter: CommonAdapter<OrderDetailModule>? = null
    private var history = ObservableField<Boolean>(false)

    override fun initBinding() {
        super.initBinding()
        mBinding = dataBinding as OrderListFragmentBinding
    }

    override fun initView() {
        super.initView()
        mBinding!!.history = history
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
            preView()
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
            loadOrderList("0", "20")
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
                    mCurrentOrdersAdapter = mOrderDetailLists.putToAdapter()
                    mBinding!!.currentOrderAdapter = mCurrentOrdersAdapter
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
                override fun onItemClick(itemView: View, position: Int, instance: Any) {
                }

                override fun onItemClick(itemView: View, position: Int) {
                }
            })
            it
        }
    }


}
