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
import com.gold.footimpression.widget.BasePopupWindow
import com.gold.footimpression.widget.ListPopupWindow
import com.google.gson.Gson
import com.google.gson.JsonElement

class OrderPreviewFragment : BaseFragment() {
    override fun getContentview() = R.layout.order_list_fragment

    private var mBinding: OrderListFragmentBinding? = null
    private var mOrderPresenter: OrderPresenter? = null
    private var orderDetailModule: OrderDetailModule? = null
    private var mOrderDetailLists = mutableListOf<OrderDetailModule>()
    private var mOrderHistoryDetailLists = mutableListOf<OrderDetailModule>()
    //编辑增值服务
//    private var mOrderIncreateLists = mutableListOf<OrderIncrementModule>()
    private var fromKey = ""
    //订单列表
    private var mCurrentOrdersAdapter: CommonAdapter<OrderDetailModule>? = null
    private var mCurrentHistoryOrdersAdapter: CommonAdapter<OrderDetailModule>? = null
    private var orderDetailPropAdapter: CommonAdapter<OrderIncrementModule>? = null

    private var mReiceverLists = mutableListOf<ReiceverModule>()
    private var mOrderIncreateAdapter: CommonAdapter<OrderIncrementModule>? = null
    private var history = ObservableField<Boolean>(false)
    private var editZengzhi = ObservableField<Boolean>(false)
    private var orderCreateDetail = ObservableField<Boolean>(false)
    private var hasZengzhiDetail = ObservableField<Boolean>(false)

    override fun initBinding() {
        super.initBinding()
        mBinding = dataBinding as OrderListFragmentBinding
    }

    override fun initView() {
        super.initView()
        mBinding!!.history = history
        mBinding!!.editZengzhi = editZengzhi
        mBinding!!.orderCreateDetail = orderCreateDetail
        mBinding!!.hasZengzhiDetail = hasZengzhiDetail
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
                        if (!history.get()!!) {
                            orderCreateDetail.set(false)
                        }
                        history.set(true)
                        if (mOrderHistoryDetailLists.size == 0) {
                            loadHistoryList("0", "1000")
                        }
                    }
                    R.id.tv_current -> {
                        if (history.get()!!) {
                            orderCreateDetail.set(false)
                        }

                        history.set(false)
                    }
                    R.id.tv_back -> {
                        editZengzhi.set(false)
                    }
                    R.id.tv_detail_back -> {
                        orderCreateDetail.set(false)
                    }
                    R.id.tv_submit_create_service -> {


                        submitCreateService(
                            mOrderDetailLists[mCurrentOrderSelectPosition].huiyuanZhanghao,
                            Gson().toJson(mOrderDetailLists[mCurrentOrderSelectPosition].orderEditIncrements.filterUsefulData())
                        )
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

    /**
     * huiyuanZhanghao 会员账号
     * paramFuwuStr 增值服务内容
     */


    fun submitCreateService(huiyuanZhanghao: String, paramFuwuStr: String) {


        if (!Utils.isNetworkConnected(mContext)) {
            toast(com.gold.footimpression.R.string.net_error)
        } else {
            (this.activity as BaseActivity).showProgressDialog { }
            mOrderPresenter!!.submitCreateService<MutableList<OrderDetailModule>>(
                huiyuanZhanghao,
                paramFuwuStr
            ) { code, msg, result ->
                (this.activity as BaseActivity).closeProgressDialog()

                if (CodeUtils.isSuccess(code)) {
//                    mOrderHistoryDetailLists = result!!
//                    mOrderHistoryDetailLists.add(0, OrderDetailModule(true))
//                    mOrderHistoryDetailLists.forEach {
//                        it.history = true
//                    }
//                    mCurrentHistoryOrdersAdapter = mOrderHistoryDetailLists.putToAdapter()
//                    mBinding!!.historyOrderAdapter = mCurrentHistoryOrdersAdapter

                    editZengzhi.set(false)
                    loadOrderList("", "")
                } else {
                    toast(msg!!)
                }
            }
        }

    }


    private var digndanId: String = ""

    //当前选中的订单position
    private var mCurrentOrderSelectPosition: Int = 0
    //当前选中的查看增值详情的订单position
    private var mCurrentOrderCreateDetailPosition: Int = 0

    fun MutableList<OrderDetailModule>.putToAdapter(): CommonAdapter<OrderDetailModule> {

        return CommonAdapter(
            mContext!!, this, R.layout.item_order_list,
            BR.orderDetailModule, BR.click
        ).let {
            it.setOnItemClick(object : OnItemClick {
                override fun onItemClick(itemView: View, position: Int, instance: Any, viewid: Int) {
                    when (viewid) {
                        R.id.iv_gouwuche -> {
                            if (position == 0) {
                                return
                            }
                            orderCreateDetail.set(false)
                            editZengzhi.set(true)
                            if (!history.get()!!) {
                                mCurrentOrderSelectPosition = position
                                if (mOrderDetailLists[mCurrentOrderSelectPosition].orderEditIncrements.size == 0) {
                                    loadZengzhi()
                                } else {
                                    mOrderIncreateAdapter!!.update(mOrderDetailLists[mCurrentOrderSelectPosition].orderEditIncrements)
                                    mOrderIncreateAdapter!!.notifyDataSetChanged()
                                }
                            }

//                            toast(" click item $position ishistory = ${(instance as OrderDetailModule).history} clickid = start")
                        }
                        R.id.ll_end -> {

                            mCurrentOrderCreateDetailPosition = position
                            orderCreateDetail.set(true)
                            if (!history.get()!!) {
//                                if (mOrderDetailLists[mCurrentOrderCreateDetailPosition].orderIncrements.size == 0) {
                                    loadZengzhiDetail(mOrderDetailLists[mCurrentOrderCreateDetailPosition].dingdanUid)
//                                } else {
//                                    hasZengzhiDetail.set(true)
//                                    orderDetailPropAdapter!!.update(mOrderDetailLists[mCurrentOrderCreateDetailPosition].orderIncrements)
//                                    orderDetailPropAdapter!!.notifyDataSetChanged()
//                                }
                            } else {
                                if (mOrderHistoryDetailLists[mCurrentOrderCreateDetailPosition].orderIncrements.size == 0) {
                                    loadZengzhiDetail(mOrderHistoryDetailLists[mCurrentOrderCreateDetailPosition].dingdanUid)
                                } else {
                                    hasZengzhiDetail.set(true)
                                    orderDetailPropAdapter!!.update(mOrderHistoryDetailLists[mCurrentOrderCreateDetailPosition].orderIncrements)
                                    orderDetailPropAdapter!!.notifyDataSetChanged()
                                }
                            }

                        }
                    }

                }

                override fun onItemClick(itemView: View, position: Int) {
//                    toast(" click item $position ")
                }
            })
            it
        }
    }

    private var mCurrentPositionCreation: Int = 0

    private var startLocation = IntArray(2)

    fun MutableList<OrderIncrementModule>.putIncreToAdapter(): CommonAdapter<OrderIncrementModule> {

        return CommonAdapter(
            mContext!!, this, R.layout.item_edit_service,
            BR.orderIncrementModule, BR.click
        ).let {
            it.setOnItemClick(object : OnItemClick {
                override fun onItemClick(itemView: View, position: Int, instance: Any, viewid: Int) {
                    mCurrentPositionCreation = position
                    itemView.getLocationOnScreen(startLocation)
                    LogUtils.i(TAG, "  view location x=${startLocation[0]} y=${startLocation[1]}")
                    when (viewid) {
                        R.id.iv_add -> {
                            mOrderDetailLists[mCurrentOrderSelectPosition].orderEditIncrements[position].amount =
                                "" + (mOrderDetailLists[mCurrentOrderSelectPosition].orderEditIncrements[position].amount.toInt() + 1)
                            mOrderIncreateAdapter!!.update(mOrderDetailLists[mCurrentOrderSelectPosition].orderEditIncrements)
                            mOrderIncreateAdapter!!.notifyDataSetChanged()
                            LogUtils.i(TAG, " click add")
                        }
                        R.id.iv_less -> {
                            mOrderDetailLists[mCurrentOrderSelectPosition].orderEditIncrements[position].amount =
                                "" + (mOrderDetailLists[mCurrentOrderSelectPosition].orderEditIncrements[position].amount.toInt() - 1)
                            mOrderIncreateAdapter!!.update(mOrderDetailLists[mCurrentOrderSelectPosition].orderEditIncrements)
                            mOrderIncreateAdapter!!.notifyDataSetChanged()
                            LogUtils.i(TAG, " click less")
                        }
                        R.id.tv_reicver_num -> {
                            if (mReiceverLists.size == 0) {
                                loadTcPersons(itemView.width, itemView)
                            } else {
                                showPop(mTcPersoPopwindow, itemView)
                            }


                        }
                    }
                }

                override fun onItemClick(itemView: View, position: Int) {
                }

            })
            it
        }
    }

    fun MutableList<OrderIncrementModule>.putIntoCreateDetailAdapter(): CommonAdapter<OrderIncrementModule> {

        return CommonAdapter(
            mContext!!, this, R.layout.item_zengzhi_detail,
            BR.orderIncrementModule, BR.click
        ).let {
            it.setOnItemClick(object : OnItemClick {
                override fun onItemClick(itemView: View, position: Int, instance: Any, viewid: Int) {
                }

                override fun onItemClick(itemView: View, position: Int) {
                }

            })
            it
        }
    }


    /**
     * 弹出popwindow 知道popwindow类型
     */
    fun showPop(popwindow: BasePopupWindow?, view: View) {
        if (popwindow!!.isShowing()) {
            popwindow.closePop()
        }
        popwindow.showPop(view);
    }

    override fun onStop() {
        super.onStop()
        mOrderPresenter!!.release()
        closePopWindow(mTcPersoPopwindow)
    }

    /**
     * close popwindow制定popwindow
     */
    fun closePopWindow(popwindow: BasePopupWindow?): Boolean {
        if (null != popwindow && popwindow!!.isShowing()) {
            popwindow.closePop()
            return true
        }
        return false
    }

    fun loadZengzhi() {

        if (!Utils.isNetworkConnected(mContext)) {
            toast(com.gold.footimpression.R.string.net_error)
        } else {
            (this.activity as BaseActivity).showProgressDialog { }
            mOrderPresenter!!.getZengzhi<MutableList<OrderIncrementModule>> { code, msg, result ->
                (this.activity as BaseActivity).closeProgressDialog()

                if (CodeUtils.isSuccess(code)) {
//                    mOrderIncreateLists = result!!.filterFuwuCode()
                    mOrderDetailLists.forEach {
                        it.orderEditIncrements.clear()
                        result!!.filterFuwuCode().forEach { item ->
                            it.orderEditIncrements.add(item.clone())
                        }
//                        it.orderEditIncrements = result!!.filterFuwuCode()
                    }
//                    mOrderDetailLists[mCurrentOrderSelectPosition].orderEditIncrements = result!!.filterFuwuCode()
                    mOrderIncreateAdapter =
                        mOrderDetailLists[mCurrentOrderSelectPosition].orderEditIncrements.putIncreToAdapter()
                    mBinding!!.editZengzhifuwuAdapter = mOrderIncreateAdapter
                } else {
                    toast(msg!!)
                }
            }
        }
    }

    fun loadZengzhiDetail(dingdanUid: String) {

        if (!Utils.isNetworkConnected(mContext)) {
            toast(com.gold.footimpression.R.string.net_error)
        } else {
            (this.activity as BaseActivity).showProgressDialog { }
            mOrderPresenter!!.getZengzhiDetails<MutableList<OrderIncrementModule>>(dingdanUid) { code, msg, result ->
                (this.activity as BaseActivity).closeProgressDialog()

                if (CodeUtils.isSuccess(code)) {
                    hasZengzhiDetail.set(true)
                    if(history.get()!!){
                        mOrderHistoryDetailLists[mCurrentOrderCreateDetailPosition].orderIncrements = result!!.filterFuwuCode()
                        orderDetailPropAdapter =
                            mOrderHistoryDetailLists[mCurrentOrderCreateDetailPosition].orderIncrements.putIntoCreateDetailAdapter()
                    }else{
                        mOrderDetailLists[mCurrentOrderCreateDetailPosition].orderIncrements = result!!.filterFuwuCode()
                        orderDetailPropAdapter =
                            mOrderDetailLists[mCurrentOrderCreateDetailPosition].orderIncrements.putIntoCreateDetailAdapter()
                    }

                    mBinding!!.orderDetailPropAdapter = orderDetailPropAdapter
                } else {
                    if (CodeUtils.isSuccessNodata(code)) {
                        hasZengzhiDetail.set(false)
                    }
                    toast(msg!!)
                }
            }
        }
    }

    fun loadTcPersons(width: Int, view: View) {

        if (!Utils.isNetworkConnected(mContext)) {
            toast(com.gold.footimpression.R.string.net_error)
        } else {
            (this.activity as BaseActivity).showProgressDialog { }
            mOrderPresenter!!.getJishiAuth<MutableList<ReiceverModule>> { code, msg, result ->
                (this.activity as BaseActivity).closeProgressDialog()

                if (CodeUtils.isSuccess(code)) {
                    mReiceverLists.clear()
                    mReiceverLists.addAll(result!!)
                    initTcPersonPopwindow(mReiceverLists, width)
                    showPop(mTcPersoPopwindow, view)
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

                val orderIncrementModule = OrderIncrementModule()
                orderIncrementModule.typeHead =true
                orderIncrementModule.typeHeadName = it.zengzhiFuwuTypeName!!
                arry.add(orderIncrementModule)
                arry.add(it)
//                it.typeHead = true
//                it.typeHeadName = it.zengzhiFuwuTypeName!!
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


    private var mTcPersoPopwindow: ListPopupWindow? = null

    /**
     * init 提成人
     */
    fun initTcPersonPopwindow(lists: MutableList<ReiceverModule>, width: Int) {
        mTcPersoPopwindow = ListPopupWindow(mContext!!)
        mTcPersoPopwindow!!.setWidth(width)
        mTcPersoPopwindow!!.popDatas = lists.filterReiceverNameStringArrayName()
        mTcPersoPopwindow!!.mItemClick = object : OnItemClick {
            override fun onItemClick(itemView: View, position: Int, instance: Any, viewid: Int) {
            }

            override fun onItemClick(itemView: View, position: Int) {
                mOrderDetailLists[mCurrentOrderSelectPosition].orderEditIncrements[mCurrentPositionCreation].name =
                    lists[position].name
                mOrderDetailLists[mCurrentOrderSelectPosition].orderEditIncrements[mCurrentPositionCreation].tcrenGonghao =
                    lists[position].gonghao
                mOrderDetailLists[mCurrentOrderSelectPosition].orderEditIncrements[mCurrentPositionCreation].jishiGonghao =
                    lists[position].gonghao

                mOrderDetailLists[mCurrentOrderSelectPosition].orderEditIncrements[mCurrentPositionCreation].dingdanUid =
                    mOrderDetailLists[mCurrentOrderSelectPosition].dingdanUid
                closePopWindow(mTcPersoPopwindow)
            }

        }

    }

    private fun MutableList<ReiceverModule>.filterReiceverNameStringArrayName(): MutableList<String> {
        val results = mutableListOf<String>()
        this.forEach {
            results.add(it.gonghao)
        }
        return results
    }

    /**
     * 提交增值服务的时候 ，处理下数据，编辑了的数量大于零的 才提价到服务器
     */
    private fun MutableList<OrderIncrementModule>.filterUsefulData(): MutableList<OrderIncrementModule> {
        val results = mutableListOf<OrderIncrementModule>()
        this.forEach {
            if (it.amount.toInt() > 0) {
                results.add(it)
            }
        }
        return results
    }
}



