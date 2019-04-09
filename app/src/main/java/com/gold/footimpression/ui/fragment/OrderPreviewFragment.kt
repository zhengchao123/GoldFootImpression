package com.gold.footimpression.ui.fragment

import android.content.Context
import android.text.TextUtils
import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.library.baseAdapters.BR
import com.gold.footimpression.R
import com.gold.footimpression.bindingadapter.CommonAdapter
import com.gold.footimpression.databinding.OrderListFragmentBinding
import com.gold.footimpression.module.OrderDetailModule
import com.gold.footimpression.module.OrderIncrementModule
import com.gold.footimpression.module.ReiceverModule
import com.gold.footimpression.module.RoomStateModule
import com.gold.footimpression.net.CodeUtils
import com.gold.footimpression.net.utils.LogUtils
import com.gold.footimpression.presenter.OrderPresenter
import com.gold.footimpression.ui.activity.MainActivity
import com.gold.footimpression.ui.base.BaseActivity
import com.gold.footimpression.ui.base.BaseFragment
import com.gold.footimpression.ui.event.EventHandler
import com.gold.footimpression.ui.event.OnItemClick
import com.gold.footimpression.utils.Utils
import com.gold.footimpression.widget.BasePopupWindow
import com.gold.footimpression.widget.ListPopupWindow
import com.google.gson.Gson

class OrderPreviewFragment : BaseFragment() {
    override fun getContentview() = R.layout.order_list_fragment

    private var mBinding: OrderListFragmentBinding? = null
    private var mOrderPresenter: OrderPresenter? = null
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
    private var searchText = ObservableField<String>("")
    private var mActivity: MainActivity? = null
    override fun initBinding() {
        super.initBinding()
        mBinding = dataBinding as OrderListFragmentBinding
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = context as MainActivity?
    }

    override fun initView() {
        super.initView()
        mBinding!!.history = history
        mBinding!!.editZengzhi = editZengzhi
        mBinding!!.orderCreateDetail = orderCreateDetail
        mBinding!!.hasZengzhiDetail = hasZengzhiDetail

        mBinding!!.searchText = searchText
        preView()
        mBinding!!.swipeFreshLayout.setColorSchemeResources(
            R.color.colorPrimary,
            R.color.colorPrimary,
            R.color.colorPrimary
        )
        mBinding!!.swipeFreshHistoryLayout.setColorSchemeResources(
            R.color.colorPrimary,
            R.color.colorPrimary,
            R.color.colorPrimary
        )
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
                        searchText.set("")
                        orderDetailPropAdapter!!.update(mutableListOf())
                        if (!history.get()!!) {
                            orderCreateDetail.set(false)
                        }
                        history.set(true)
                        if (mOrderHistoryDetailLists.size == 0) {
                            loadHistoryList("0", "1000")
                        }else{
                            mCurrentHistoryOrdersAdapter!!.update(mOrderHistoryDetailLists)
                        }
                    }
                    R.id.tv_current -> {
                        searchText.set("")
                        if (history.get()!!) {
                            orderCreateDetail.set(false)
                            mCurrentOrdersAdapter!!.update(mOrderDetailLists)
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

                    R.id.tv_search -> {
                        val searchKey = searchText.get()
                        orderCreateDetail.set(false)
                        editZengzhi.set(false)
                        if (!history.get()!!) {
                            val result = mOrderDetailLists.searchByLeftKey(searchKey)
                            if (result.size > 0) {
                                mCurrentOrdersAdapter!!.update(result)
                            }

                        } else {
                            val result = mOrderHistoryDetailLists.searchByLeftKey(searchKey)
                            if (result.size > 0) {
                                mCurrentHistoryOrdersAdapter!!.update(result)
                            }
                        }


                        Utils.closeSoftKeyBord(mContext, mActivity!!)
                        //TODO
                    }


                }

            }
        }
        mBinding!!.click = click
        mBinding!!.swipeFreshLayout.setOnRefreshListener {
            orderCreateDetail.set(false)
            editZengzhi.set(false)
            loadOrderList("", "", true)
        }

        mBinding!!.swipeFreshHistoryLayout.setOnRefreshListener {
            orderCreateDetail.set(false)
            editZengzhi.set(false)
            loadHistoryList("", "", true)
        }
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

    fun loadOrderList(start: String, limit: String, isFresh: Boolean = false) {


        if (!Utils.isNetworkConnected(mContext)) {
            mBinding!!.swipeFreshLayout.isRefreshing = false
            toast(com.gold.footimpression.R.string.net_error)
        } else {
            if (!isFresh) {
                (this.activity as BaseActivity).showProgressDialog { }
            }

            mOrderPresenter!!.getOrders<MutableList<OrderDetailModule>>(start, limit) { code, msg, result ->
                (this.activity as BaseActivity).closeProgressDialog()
                if (isFresh) {
                    mBinding!!.swipeFreshLayout.isRefreshing = false
                    if (CodeUtils.isSuccess(code)) {
                        toast(R.string.refresh_success)

                        mOrderDetailLists = result!!
                        mOrderDetailLists.add(0, OrderDetailModule(true))
                        mBinding!!.currentOrderAdapter!!.update(mOrderDetailLists)
                    } else {
                        toast(msg!!)
                    }


                } else {
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

    }

    fun loadHistoryList(start: String, limit: String, isFresh: Boolean = false) {


        if (!Utils.isNetworkConnected(mContext)) {
            toast(com.gold.footimpression.R.string.net_error)
            if (isFresh) {
                mBinding!!.swipeFreshHistoryLayout.isRefreshing = false
            }
        } else {
            if (!isFresh) {
                (this.activity as BaseActivity).showProgressDialog { }
            }

            mOrderPresenter!!.getHistory<MutableList<OrderDetailModule>>(start, limit) { code, msg, result ->
                (this.activity as BaseActivity).closeProgressDialog()

                if (isFresh) {
                    mBinding!!.swipeFreshHistoryLayout.isRefreshing = false
                    toast(R.string.refresh_success)
                    mOrderHistoryDetailLists = result!!
                    mOrderHistoryDetailLists.add(0, OrderDetailModule(true))
                    mOrderHistoryDetailLists.forEach {
                        it.history = true
                    }
                    mCurrentHistoryOrdersAdapter!!.update(mOrderHistoryDetailLists)

                } else {
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

    }

    /**
     * huiyuanZhanghao 会员账号
     * paramFuwuStr 增值服务内容
     */


    fun submitCreateService(huiyuanZhanghao: String?, paramFuwuStr: String) {


        if (!Utils.isNetworkConnected(mContext)) {
            toast(com.gold.footimpression.R.string.net_error)
        } else {
            (this.activity as BaseActivity).showProgressDialog { }
            mOrderPresenter!!.submitCreateService<MutableList<OrderDetailModule>>(
                huiyuanZhanghao,
                paramFuwuStr
            ) { code, msg, result ->
                (this.activity as BaseActivity).closeProgressDialog()
                toast(msg!!)
                if (CodeUtils.isSuccess(code)) {
                    editZengzhi.set(false)
                    loadOrderList("", "")
                }
//                else {
//                    toast(msg!!)
//                }
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
                    if (position == 0) {
                        return
                    }
                    when (viewid) {
                        R.id.iv_gouwuche -> {
                            orderCreateDetail.set(false)
                            editZengzhi.set(true)
//                            if (!history.get()!!) {
                            mCurrentOrderSelectPosition = position
//                            if (mOrderDetailLists[mCurrentOrderSelectPosition].orderEditIncrements.size == 0) {
                            if (mCurrentOrdersAdapter!!.getDatas()!![mCurrentOrderSelectPosition].orderEditIncrements.size == 0) {
                                loadZengzhi()
                            } else {
//                                mOrderIncreateAdapter!!.update(mOrderDetailLists[mCurrentOrderSelectPosition].orderEditIncrements)
                                mOrderIncreateAdapter!!.update(mCurrentOrdersAdapter!!.getDatas()!![mCurrentOrderSelectPosition].orderEditIncrements)
                                mOrderIncreateAdapter!!.notifyDataSetChanged()
                            }
//                            }

//                            toast(" click item $position ishistory = ${(instance as OrderDetailModule).history} clickid = start")
                        }
                        R.id.ll_end -> {

                            mCurrentOrderCreateDetailPosition = position

                            if (!history.get()!!) {
//                                if (mOrderDetailLists[mCurrentOrderCreateDetailPosition].orderIncrements.size == 0) {
//                                loadZengzhiDetail(mOrderDetailLists[mCurrentOrderCreateDetailPosition].dingdanUid)
//                                if (mCurrentOrdersAdapter!!.getDatas()!![mCurrentOrderCreateDetailPosition].orderIncrements.size == 0) {
                                    loadZengzhiDetail(mCurrentOrdersAdapter!!.getDatas()!![mCurrentOrderCreateDetailPosition].dingdanUid)

//
//                                } else {
////                                    orderDetailPropAdapter!!.update(mOrderDetailLists[mCurrentOrderCreateDetailPosition].orderIncrements)
//                                    orderDetailPropAdapter!!.update(mCurrentOrdersAdapter!!.getDatas()!![mCurrentOrderCreateDetailPosition].orderIncrements)
//                                    hasZengzhiDetail.set(true)
//                                }
                            } else {
//                                if (mOrderHistoryDetailLists[mCurrentOrderCreateDetailPosition].orderIncrements.size == 0) {
//                                if (mCurrentHistoryOrdersAdapter!!.getDatas()!![mCurrentOrderCreateDetailPosition].orderIncrements.size == 0) {
//                                    loadZengzhiDetail(mOrderHistoryDetailLists[mCurrentOrderCreateDetailPosition].dingdanUid)
                                    loadZengzhiDetail(mCurrentHistoryOrdersAdapter!!.getDatas()!![mCurrentOrderCreateDetailPosition].dingdanUid)
//                                } else {
////                                    orderDetailPropAdapter!!.update(mOrderHistoryDetailLists[mCurrentOrderCreateDetailPosition].orderIncrements)
//                                    orderDetailPropAdapter!!.update(mCurrentHistoryOrdersAdapter!!.getDatas()!![mCurrentOrderCreateDetailPosition].orderIncrements)
//                                    hasZengzhiDetail.set(true)
//                                }
                            }
                            orderCreateDetail.set(true)
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
        (this.activity as BaseActivity).closeProgressDialog()
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
                    if (history.get()!!) {
                        mOrderHistoryDetailLists[mCurrentOrderCreateDetailPosition].orderIncrements =
                            result!!.filterFuwuCode()
                        orderDetailPropAdapter =
                            mOrderHistoryDetailLists[mCurrentOrderCreateDetailPosition].orderIncrements.putIntoCreateDetailAdapter()
                    } else {
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
                orderIncrementModule.typeHead = true
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

//                mOrderDetailLists[mCurrentOrderSelectPosition].orderEditIncrements[mCurrentPositionCreation].dingdanUid =
//                    mOrderDetailLists[mCurrentOrderSelectPosition].dingdanUid
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
                it.dingdanUid = mOrderDetailLists[mCurrentOrderSelectPosition].dingdanUid
            }
        }
        return results
    }


    private fun MutableList<OrderDetailModule>.searchByLeftKey(key: String?): MutableList<OrderDetailModule> {
        val results = mutableListOf<OrderDetailModule>()
        this.forEach {
            try {
                if (it.firstItem) {
                    results.add(it)
                    return@forEach
                }
                if (it.dingdanUid.contains(key!!)) {
                    results.add(it)
                    return@forEach
                }
                if (it.dingdanhao.contains(key!!)) {
                    results.add(it)
                    return@forEach
                }
                if (it.dingdanSeq.contains(key!!)) {
                    results.add(it)
                    return@forEach
                }
                if (it.dingdanLaiyuanName.contains(key!!)) {
                    results.add(it)
                    return@forEach
                }
                if (it.dingdanRiqi.contains(key!!)) {
                    results.add(it)
                    return@forEach
                }
                if (it.fuwuXiangmuMingcheng.contains(key!!)) {
                    results.add(it)
                    return@forEach
                }
                if (it.dingdanJineFw.contains(key!!)) {
                    results.add(it)
                    return@forEach
                }




                if (it.daodianTimeStr.contains(key!!)) {
                    results.add(it)
                    return@forEach
                }
                if (it.daojishi.contains(key!!)) {
                    results.add(it)
                    return@forEach
                }
                if (it.dingdanStatusName.contains(key!!)) {
                    results.add(it)
                    return@forEach
                }

                if (it.fuwuShichang.contains(key!!)) {
                    results.add(it)
                    return@forEach
                }
                if (it.huiyuanName.contains(key!!)) {
                    results.add(it)
                    return@forEach
                }
                if (it.huiyuanTel.contains(key!!)) {
                    results.add(it)
                    return@forEach
                }


                if (it.huiyuanZhanghao.contains(key!!)) {
                    results.add(it)
                    return@forEach
                }
                if (it.jiedaiGonghao.contains(key!!)) {
                    results.add(it)
                    return@forEach
                }
                if (it.jiedaiName.contains(key!!)) {
                    results.add(it)
                    return@forEach
                }



                if (it.jiesuanStatusValue().contains(key!!)) {
                    results.add(it)
                    return@forEach
                }
                if (it.jishiGonghao.contains(key!!)) {
                    results.add(it)
                    return@forEach
                }
                if (it.jishiName.contains(key!!)) {
                    results.add(it)
                    return@forEach
                }


                if (it.mendianMingcheng.contains(key!!)) {
                    results.add(it)
                    return@forEach
                }
                if (it.price.contains(key!!)) {
                    results.add(it)
                    return@forEach
                }
                if (it.shangzhongShijian.contains(key!!)) {
                    results.add(it)
                    return@forEach
                }

                if (it.shifouLiusu.contains(key!!)) {
                    results.add(it)
                    return@forEach
                }
                if (it.shoupaihao.contains(key!!)) {
                    results.add(it)
                    return@forEach
                }
                if (it.weizhifu.contains(key!!)) {
                    results.add(it)
                    return@forEach
                }

                if (it.yizhifu.contains(key!!)) {
                    results.add(it)
                    return@forEach
                }
                if (it.xiazhongShijian.contains(key!!)) {
                    results.add(it)
                    return@forEach
                }


            } catch (e: Exception) {
                return@forEach
            }

        }
        return results
    }


}



