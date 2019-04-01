package com.gold.footimpression.ui.fragment

import android.text.TextUtils
import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.library.baseAdapters.BR
import com.gold.footimpression.R
import com.gold.footimpression.bindingadapter.CommonAdapter
import com.gold.footimpression.module.*
import com.gold.footimpression.module.OrderModule.ServiceInfo
import com.gold.footimpression.net.CodeUtils
import com.gold.footimpression.net.utils.DensyUtils
import com.gold.footimpression.net.utils.LogUtils
import com.gold.footimpression.presenter.UserAcountPresenter
import com.gold.footimpression.ui.activity.MainActivity
import com.gold.footimpression.ui.base.BaseActivity
import com.gold.footimpression.ui.base.BaseFragment
import com.gold.footimpression.ui.event.EventHandler
import com.gold.footimpression.ui.event.OnItemClick
import com.gold.footimpression.utils.Utils
import com.gold.footimpression.utils.ViewUtils
import com.gold.footimpression.widget.BasePopupWindow
import com.gold.footimpression.widget.ListPopupWindow
import com.google.gson.Gson
import okhttp3.internal.Util


class ServiceItemsEditFragment : BaseFragment() {

    var mBinding: com.gold.footimpression.databinding.ServiceItemsEditFragmentBinding? = null


    //团购码
    private var mLoginPresenter: UserAcountPresenter? = null
    //服务项目数据
    private var mServiceItems = mutableListOf<ServiceItemModule>()
    //接待人员列表
    private var mReicevers = mutableListOf<ReiceverModule>()
    //服务项目
    private var mServiceItemsAdapter: CommonAdapter<ServiceItemModule>? = null

    //是团购
    private var group = ObservableField<Boolean>(false)
    //是优惠券
    private var coupon = ObservableField<Boolean>(false)
    //房间号
//    private var roomNum = ObservableField<String>("")
//    //手牌号
//    private var shoupaiNumValue = ObservableField<String>("")

    //当前选中的编辑服务项目
    private var mCurrentServiceItem = ServiceItemModule()
    //    //折扣价
//    private var discountPrice = ObservableField<String>("")
    //团购价
//    private var groupPrice = ObservableField<String>("")
    private var visible = ObservableField<Boolean>(false)

    override fun getContentview() = com.gold.footimpression.R.layout.service_items_edit_fragment
    //房间和手牌信息
    private var mRoomCardModule = RoomAndCardModule()

    //    private var mServiceEditModule = EditServiceModule()
    //接待人员
    private var reiceverModule = ReiceverModule(Utils.getDisplayName()!!, Utils.getGonghao()!!)


    override fun initBinding() {
        super.initBinding()
        mBinding = (dataBinding as com.gold.footimpression.databinding.ServiceItemsEditFragmentBinding)


        mBinding!!.coupon = coupon
        mBinding!!.group = group

//        mBinding!!.reiceverModule = reiceverModule
//        mBinding!!.roomNum = roomNum
//        mBinding!!.shoupaiNumValue = shoupaiNumValue
//        mBinding!!.discountPrice = discountPrice
//        mBinding!!.groupPrice = groupPrice
        mLoginPresenter = UserAcountPresenter(this.activity)
    }

    override fun initView() {
        super.initView()

        mBinding!!.visible = visible
        preview()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            preview()
        }
    }

    fun preview() {
        group.set((this.activity as MainActivity).group.get())
        coupon.set((this.activity as MainActivity).coupon.get())

        if (!coupon.get()!!) {
            mBinding!!.priceZhekou.requestFocus()
        }
//        mBinding!!.etPriceGroup.isEnabled = group.get()!!

        mServiceItems = (this.activity as MainActivity).services.filterArrays()
        visible.set(false)
        mServiceItemsAdapter = mServiceItems.putToAdapter()
        mBinding!!.itemsAdapter = mServiceItemsAdapter

        mCurrentServiceItem.mServiceEditModule.reicever = reiceverModule
        mBinding!!.editServiceModule = mCurrentServiceItem.mServiceEditModule

    }

    override fun onStop() {
        super.onStop()
        mLoginPresenter!!.release()

        closePopWindow(mRoomPopwindow)
        closePopWindow(mShoupaiPopwindow)
        mShoupaiPopwindow = null
        mRoomPopwindow = null

    }

    override fun initListener() {
        super.initListener()

        val click = object : EventHandler(mContext, false) {
            override fun onClickView(view: View?) {
                super.onClickView(view)
                when (view!!.id) {
                    R.id.tv_bucha -> {
                        if (null == mBuchaPopwindow) {
                            initBuchaPopwindow()
                            showPop(mBuchaPopwindow, mBinding!!.llBucha)
                        } else {
                            showPop(mBuchaPopwindow, mBinding!!.llBucha)
                        }
                    }
                    R.id.tv_edit_sure -> {
                        if( null != this@ServiceItemsEditFragment.activity){
                            Utils.closeSoftKeyBord(mContext, this@ServiceItemsEditFragment.activity!!)
                        }

                        if (group.get()!!) {
                            if (mCurrentServiceItem.mServiceEditModule.groupPrice.equals("")) {
                                toast(R.string.please_enter_group_price)
                                return
                            }
                        }

                        if (mCurrentServiceItem.mServiceEditModule.roomNum.equals("")) {
                            toast(R.string.please_select_room)
                            return
                        }
                        if (mCurrentServiceItem.mServiceEditModule.shoupaiNumValue.equals("")) {
                            toast(R.string.please_select_shoupai)
                            return
                        }
                        if (coupon.get()!!) {
                            if (mCurrentServiceItem.mServiceEditModule.xcrenCode.equals("")) {
                                toast(R.string.please_enter_xcrenCode)
                                return
                            }
                            if (mCurrentServiceItem.mServiceEditModule.youhuijia.equals("")) {
                                toast(R.string.please_enter_youhuijia)
                                return
                            }
                        }

                        visible.set(true)
                        mCurrentServiceItem.selected = true
                        mServiceItems[mCurrentPosition] = mCurrentServiceItem.clone()
                        mCurrentServiceItem.copy(mServiceItems[mCurrentPosition])
                        mServiceItemsAdapter!!.update(mServiceItems)
                    }
                    R.id.tv_comfire -> {
                        (this@ServiceItemsEditFragment.activity as MainActivity).mOrderModule.paramFuwuStr.clear()
                        mServiceItems.forEach {
                            if (it.selected) {
                                val mServiceInfo = ServiceInfo()
                                mServiceInfo.zhongfangBianma = mCurrentServiceItem.mServiceEditModule.roomNum
                                mServiceInfo.dingdanJineFwdz = mCurrentServiceItem.mServiceEditModule.discountPrice
                                mServiceInfo.tuangoujia = mCurrentServiceItem.mServiceEditModule.groupPrice
                                mServiceInfo.fuwuXiangmuBianma = mCurrentServiceItem.fuwuXiangmuBianma
                                mServiceInfo.jishiGonghao = mCurrentServiceItem.plannerGonghao
                                mServiceInfo.dianzhong = mCurrentServiceItem.selectHourService
                                mServiceInfo.shoupaihao = mCurrentServiceItem.mServiceEditModule.shoupaiNumValue
                                mServiceInfo.jiedaiGonghao = mCurrentServiceItem.mServiceEditModule.reicever.gonghao
                                mServiceInfo.xcrenName = mCurrentServiceItem.mServiceEditModule.xcrenName
                                mServiceInfo.xcrenCode = mCurrentServiceItem.mServiceEditModule.xcrenCode
                                mServiceInfo.youhuijia = mCurrentServiceItem.mServiceEditModule.youhuijia
                                mServiceInfo.buchajia =
                                    if (mCurrentServiceItem.mServiceEditModule.buchajiaValue.equals("是")) "1" else "0"
                                mServiceInfo.buchajiaValue = mCurrentServiceItem.mServiceEditModule.buchajiaValue
                                (this@ServiceItemsEditFragment.activity as MainActivity).mOrderModule.paramFuwuStr.add(
                                    mServiceInfo
                                )
                            }
                        }

                        (this@ServiceItemsEditFragment.activity as MainActivity).mOrderModule.paramStr.mendianBianma =
                            Utils.getUserBumenCode()
                        LogUtils.i(
                            TAG,
                            "=== submit order str = ${Gson().toJson((this@ServiceItemsEditFragment.activity as MainActivity).mOrderModule)}"
                        )
                        var orderModule = (this@ServiceItemsEditFragment.activity as MainActivity).mOrderModule
                        submitOrder(Gson().toJson(orderModule.paramFuwuStr), Gson().toJson(orderModule.paramStr))

                    }
                    R.id.iv_back -> {
                        mCurrentPosition = 0
                        (this@ServiceItemsEditFragment.activity as MainActivity).showFragment("SERVICE_ITEMS_FRAGMENT")
                    }
                    R.id.et_room_num -> {
                        if (mRoomCardModule.zhongfang.size == 0) {
                            loadRoom()
                        } else {
                            initRoomPopwindow(mRoomCardModule.zhongfang)
                            showPop(mRoomPopwindow!!, mBinding!!.llRoom)
                        }

                    }
                    R.id.shoupai_num -> {
                        if (mRoomCardModule.shoupai.size == 0) {
                            loadRoom(false)
                        } else {
                            initShoupaiPopwindow(mRoomCardModule.shoupai)
                            showPop(mShoupaiPopwindow!!, mBinding!!.llCard)
                        }
                    }

                    R.id.et_reciver_name -> {
                        if (mReicevers.size == 0) {
                            loadReciver()
                        } else {
                            showPop(mReiceverPopwindow!!, mBinding!!.llReciverName)
                        }
                    }

                }
            }
        }
        mBinding!!.click = click


    }


    private var mCurrentPosition: Int = 0

    fun MutableList<ServiceItemModule>.putToAdapter(): CommonAdapter<ServiceItemModule> {

        return CommonAdapter(
            mContext!!, this, R.layout.item_service_edit_items,
            BR.serviceItemModule
        ).let {
            it.setOnItemClick(object : OnItemClick {
                override fun onItemClick(itemView: View, position: Int, instance: Any) {
                }

                override fun onItemClick(itemView: View, position: Int) {
                    mServiceItems.forEach { item ->
                        item.clicked = false
                    }
                    mServiceItems[position].clicked = true
//                    mServiceItems[position].copy(mCurrentServiceItem)
                    mCurrentServiceItem = mServiceItems[position].clone()
                    mServiceItems[position].copy(mCurrentServiceItem)

                    if (mCurrentServiceItem.mServiceEditModule.reicever.name.equals("")) {
                        mCurrentServiceItem.mServiceEditModule.reicever = reiceverModule
                    }
                    mCurrentPosition = position
                    mBinding!!.editServiceModule = mCurrentServiceItem.mServiceEditModule
                    mServiceItemsAdapter!!.update(mServiceItems)
                }
            })
            it
        }
    }


    //处理服务项目列表的选中状态
    fun MutableList<ServiceItemModule>.filterArrays(): MutableList<ServiceItemModule> {
        val result = mutableListOf<ServiceItemModule>()
        var i = 0
        this.forEach {
            it.filterPlanners.forEach { item ->
                if (item.selected) {
                    val value = ServiceItemModule()


                    value.selected = false
                    value.filterPlanners = it.filterPlanners
                    value.planners = it.planners
                    value.fuwuXiangmuMingcheng = it.fuwuXiangmuMingcheng
                    value.fuwuShichang = it.fuwuShichang
                    value.price = it.price
                    value.priceMember = it.priceMember
                    value.plannerName = item.name!!
                    value.plannerGonghao = item.gonghao!!
                    value.selectHourService = item.hour
                    value.fuwuXiangmuBianma = it.fuwuXiangmuBianma
//                    it.avalibleData = true
                    if (i == 0) {
                        mCurrentServiceItem = value.clone()
                        value.copy(mCurrentServiceItem)
                        value.clicked = true
                    }
                    result.add(value)
                    i++

                }
            }

        }
        return result
    }

    /**
     * 获取房间和手牌
     */
    fun loadRoom(fromRoom: Boolean = true) {
        if (!Utils.isNetworkConnected(mContext)) {
            toast(com.gold.footimpression.R.string.net_error)
        } else {
            (this.activity as BaseActivity).showProgressDialog { }
            mLoginPresenter!!.getRoom<RoomAndCardModule>() { code, msg, result ->
                (this.activity as BaseActivity).closeProgressDialog()

                if (CodeUtils.isSuccess(code)) {
                    mRoomCardModule = result as RoomAndCardModule
                    if (fromRoom) {
                        initRoomPopwindow((result as RoomAndCardModule).zhongfang)
                        showPop(mRoomPopwindow, mBinding!!.llRoom)
                    } else {
                        initShoupaiPopwindow((result as RoomAndCardModule).shoupai)
                        showPop(mShoupaiPopwindow, mBinding!!.llCard)
                    }

                } else {
                    toast(msg!!)
                }
            }
        }
    }

    /**
     * 获取接待
     */
    fun loadReciver() {
        if (!Utils.isNetworkConnected(mContext)) {
            toast(com.gold.footimpression.R.string.net_error)
        } else {
            (this.activity as BaseActivity).showProgressDialog { }
            mLoginPresenter!!.getReicevers<MutableList<ReiceverModule>>() { code, msg, result ->
                (this.activity as BaseActivity).closeProgressDialog()

                if (CodeUtils.isSuccess(code)) {
                    mReicevers = result!!
                    initReiceverPopwindow(result)
                    showPop(mReiceverPopwindow!!, mBinding!!.llReciverName)

                } else {
                    toast(msg!!)
                }
            }
        }
    }


    /**
     * 获取接待
     */
    fun submitOrder(paramFuwuStr: String, paramStr: String) {
        if (!Utils.isNetworkConnected(mContext)) {
            toast(com.gold.footimpression.R.string.net_error)
        } else {
            (this.activity as BaseActivity).showProgressDialog { }
            mLoginPresenter!!.submitOrder<Unit>(paramFuwuStr, paramStr) { code, msg, result ->
                (this.activity as BaseActivity).closeProgressDialog()

                if (CodeUtils.isSuccess(code)) {
                    toast(R.string.submit_order_success)
                } else {
                    toast(msg!!)
                }
            }
        }
    }


    private var mRoomPopwindow: ListPopupWindow? = null
    private var mShoupaiPopwindow: ListPopupWindow? = null
    private var mReiceverPopwindow: ListPopupWindow? = null
    private var mBuchaPopwindow: ListPopupWindow? = null

    /**
     * init 房间popwindow
     */
    fun initRoomPopwindow(lists: MutableList<RoomAndCardModule.Room>) {
        mRoomPopwindow = ListPopupWindow(mContext!!)
        mRoomPopwindow!!.setWidth(mBinding!!.llRoom.width)
        mRoomPopwindow!!.popDatas = lists.filterStringArrayName()
        mRoomPopwindow!!.mItemClick = object : OnItemClick {
            override fun onItemClick(itemView: View, position: Int, instance: Any) {
            }

            override fun onItemClick(itemView: View, position: Int) {
                mRoomPopwindow!!.closePop()
//                roomNum.set(lists[position].zhongfangBianma)
                mCurrentServiceItem.mServiceEditModule.roomNum = lists[position].zhongfangBianma

            }

        }

    }

    /**
     * init 手牌popwindow
     */
    fun initShoupaiPopwindow(lists: MutableList<RoomAndCardModule.Card>) {
        mShoupaiPopwindow = ListPopupWindow(mContext!!)
        mShoupaiPopwindow!!.setWidth(mBinding!!.llCard.width)
        mShoupaiPopwindow!!.popDatas = lists.filterShoupaiStringArrayName()
        mShoupaiPopwindow!!.mItemClick = object : OnItemClick {
            override fun onItemClick(itemView: View, position: Int, instance: Any) {
            }

            override fun onItemClick(itemView: View, position: Int) {
                mShoupaiPopwindow!!.closePop()
//                shoupaiNumValue.set(lists[position].shoupaihao)
                mCurrentServiceItem.mServiceEditModule.shoupaiNumValue = lists[position].shoupaihao
            }

        }

    }


    /**
     * init 接待popwindow
     */
    fun initBuchaPopwindow() {
        mBuchaPopwindow = ListPopupWindow(mContext!!)
        mBuchaPopwindow!!.setWidth(mBinding!!.llBucha.width)
        mBuchaPopwindow!!.mOffsetY = -DensyUtils.dp2px(mContext!!, 50f) * 2
        mBuchaPopwindow!!.needTransation = true
        mBuchaPopwindow!!.popDatas = mutableListOf("否", "是")
        mBuchaPopwindow!!.mItemClick = object : OnItemClick {
            override fun onItemClick(itemView: View, position: Int, instance: Any) {
            }

            override fun onItemClick(itemView: View, position: Int) {
                mBuchaPopwindow!!.closePop()
                mCurrentServiceItem.mServiceEditModule.buchajiaValue = mBuchaPopwindow!!.popDatas[position]
                mCurrentServiceItem.mServiceEditModule.buchajia = "" + position
            }

        }

    }


    /**
     * init 接待popwindow
     */
    fun initReiceverPopwindow(lists: MutableList<ReiceverModule>) {
        mReiceverPopwindow = ListPopupWindow(mContext!!)
        mReiceverPopwindow!!.setWidth(mBinding!!.llReciverName.width)
        mReiceverPopwindow!!.popDatas = lists.filterReiceverNameStringArrayName()
        mReiceverPopwindow!!.mItemClick = object : OnItemClick {
            override fun onItemClick(itemView: View, position: Int, instance: Any) {
            }

            override fun onItemClick(itemView: View, position: Int) {
                mReiceverPopwindow!!.closePop()
                mCurrentServiceItem.mServiceEditModule.reicever = lists[position]

            }

        }

    }

    private fun MutableList<RoomAndCardModule.Room>.filterStringArrayName(): MutableList<String> {
        val result = mutableListOf<String>()
        this.forEach {
            result.add(it.zhongfangBianma)
        }
        return result
    }

    private fun MutableList<RoomAndCardModule.Card>.filterShoupaiStringArrayName(): MutableList<String> {
        val result = mutableListOf<String>()
        this.forEach {
            result.add(it.shoupaihao)
        }
        return result
    }

    private fun MutableList<ReiceverModule>.filterReiceverNameStringArrayName(): MutableList<String> {
        val result = mutableListOf<String>()
        this.forEach {
            result.add(it.name)
        }
        return result
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
}

private fun ServiceItemModule.copy(mCurrentServiceItem: ServiceItemModule) {

//    mCurrentServiceItem.fuwuXiangmuMingcheng = this.fuwuXiangmuMingcheng
//    mCurrentServiceItem.fuwuShichang = this.fuwuShichang
//
//    mCurrentServiceItem.price = this.price
//    mCurrentServiceItem.filterPlanners = this.filterPlanners

    mCurrentServiceItem.mServiceEditModule = this.mServiceEditModule.clone()
    mCurrentServiceItem.mServiceEditModule.reicever = this.mServiceEditModule.reicever.clone()
//    mCurrentServiceItem.avalibleData = this.avalibleData
//    mCurrentServiceItem.selectHourService = this.selectHourService
//    mCurrentServiceItem.selected = this.selected
//    mCurrentServiceItem.fuwuXiangmuBianma = this.fuwuXiangmuBianma
//    mCurrentServiceItem.clicked = this.clicked
//    mCurrentServiceItem.priceMember = this.priceMember
//    mCurrentServiceItem.plannerName = this.plannerName
//    mCurrentServiceItem.plannerGonghao = this.plannerGonghao
//    mCurrentServiceItem.planners = this.planners

}

