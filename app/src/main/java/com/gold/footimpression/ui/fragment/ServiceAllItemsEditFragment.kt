package com.gold.footimpression.ui.fragment

import android.app.Activity.RESULT_OK
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.library.baseAdapters.BR
import com.gold.footimpression.R
import com.gold.footimpression.bindingadapter.CommonAdapter
import com.gold.footimpression.module.*
import com.gold.footimpression.net.CodeUtils
import com.gold.footimpression.net.utils.DensyUtils
import com.gold.footimpression.net.utils.LogUtils
import com.gold.footimpression.presenter.UserAcountPresenter
import com.gold.footimpression.ui.activity.EditOrderActivity
import com.gold.footimpression.ui.base.BaseFragment
import com.gold.footimpression.ui.event.EventHandler
import com.gold.footimpression.ui.event.OnItemClick
import com.gold.footimpression.utils.Utils
import com.gold.footimpression.widget.BasePopupWindow
import com.gold.footimpression.widget.ListPopupWindow
import com.google.gson.Gson


class ServiceAllItemsEditFragment : BaseFragment() {

    var mBinding: com.gold.footimpression.databinding.ServiceAllItemsEditFragmentBinding? = null


    //团购码
    private var mLoginPresenter: UserAcountPresenter? = null
    //服务项目数据
    private var mServiceItems = mutableListOf<ServiceItemModule>()
    //接待人员列表
    private var mReicevers = mutableListOf<ReiceverModule>()
    //服务项目
    private var mServiceItemsAdapter: CommonAdapter<ServiceItemModule>? = null


    //当前选中的编辑服务项目
    private var mCurrentServiceItem = ServiceItemModule()
    private var visible = ObservableField<Boolean>(false)

    override fun getContentview() = com.gold.footimpression.R.layout.service_all_items_edit_fragment
    //房间和手牌信息
    private var mRoomCardModule = RoomAndCardModule()


    private var mActivity: EditOrderActivity? = null

    override fun initBinding() {
        super.initBinding()
        mBinding = (dataBinding as com.gold.footimpression.databinding.ServiceAllItemsEditFragmentBinding)


        mLoginPresenter = UserAcountPresenter(mActivity!!)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = context as EditOrderActivity?
    }

    override fun initView() {
        super.initView()

        mBinding!!.visible = visible
        preDada()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            preDada()
        }
    }

    fun preDada() {
        val data = this.arguments
        if (null != data && data.containsKey("dingdanUid")) {
            val dingdanUid = data.getString("dingdanUid", "")
            LogUtils.i(TAG, "dingdanUid =$dingdanUid")

            loadOrderDetail(dingdanUid)
        }
    }

    fun preview() {
        visible.set(false)
        mServiceItemsAdapter = mServiceItems.putToAdapter()
        mBinding!!.itemAdapter = mServiceItemsAdapter
        LogUtils.i(
            TAG,
            "=== preview itessize ${mServiceItems.size} adapter size ${mServiceItemsAdapter!!.getDatas()!!.size}"
        )
        mBinding!!.editServiceModule = mCurrentServiceItem.mServiceEditModule
    }

    private fun clearInputFocuse() {
        mBinding!!.priceZhekou.clearFocus()
        mBinding!!.etPriceGroup.clearFocus()
        mBinding!!.etXuanchuanyuanCode.clearFocus()
        mBinding!!.etYouhuiAmount.clearFocus()
    }

    override fun onStop() {
        super.onStop()
        mLoginPresenter!!.release()

        closePopWindow(mRoomPopwindow)
        closePopWindow(mShoupaiPopwindow)
        mShoupaiPopwindow = null
        mRoomPopwindow = null

    }

    private val editDetails = mutableListOf<OrderEditDetailModule>()

    override fun initListener() {
        super.initListener()

        val click = object : EventHandler(mContext, false) {
            override fun onClickView(view: View?) {
                super.onClickView(view)

                Utils.closeSoftKeyBord(mContext, mActivity!!)
                clearInputFocuse()
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
                        if (null != mActivity) {
                            Utils.closeSoftKeyBord(mContext, mActivity!!)
                        }

                        if (mCurrentServiceItem.mServiceEditModule.roomNum.equals("")) {
                            toast(R.string.please_select_room)
                            return
                        }
                        if (mCurrentServiceItem.mServiceEditModule.shoupaiNumValue.equals("")) {
                            toast(R.string.please_select_shoupai)
                            return
                        }
                        mSelectedShoupaiHao.remove(mCurrentSelectShoupai)
                        mSelectedShoupaiHao.remove(mPreCurrentSelectShoupai)
                        mSelectedShoupaiHao.add(mCurrentSelectShoupai)
                        mCurrentSelectShoupai = ""

                        mCurrentServiceItem.selected = true
                        mServiceItems[mCurrentPosition] = mCurrentServiceItem.clone()
                        mServiceItemsAdapter!!.update(mServiceItems)
                        var hasEditAll = true
                        mServiceItems.forEach {
                            if (!it.selected) {
                                hasEditAll = false
                            }
                        }
                        visible.set(hasEditAll)
                    }
                    R.id.tv_comfire -> {
                        editDetails.clear()
                        mServiceItems.forEach {
                            if (it.selected) {

                                val editDetail = OrderEditDetailModule()

                                editDetail.dingdanUid = it.mServiceEditModule.dingdanUid
                                editDetail.jiedaiGonghao = it.mServiceEditModule.reicever.gonghao
                                editDetail.jiedaiName = it.mServiceEditModule.reicever.name
                                editDetail.zhongfangBianma = it.mServiceEditModule.roomNum
                                editDetail.shoupaihao = it.mServiceEditModule.shoupaiNumValue

                                editDetails.add(editDetail)
                            } else {
                                toast(R.string.has_un_edti_service)
                                return
                            }
                        }
                        updateDingDan(Gson().toJson(editDetails))
                    }
                    R.id.iv_back -> {
                        mCurrentPosition = 0
                        mActivity!!.finish()
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
                override fun onItemClick(itemView: View, position: Int, instance: Any, viewid: Int) {
                }

                override fun onItemClick(itemView: View, position: Int) {
                    mServiceItems.forEach { item ->
                        item.clicked = false
                    }
                    mServiceItems[position].clicked = true
                    mCurrentServiceItem = mServiceItems[position].clone()
                    mBinding!!.editServiceModule = mCurrentServiceItem.mServiceEditModule
                    mCurrentPosition = position
                    mServiceItemsAdapter!!.update(mServiceItems)
                }
            })
            it
        }
    }


    /**
     * 获取房间和手牌
     */
    fun loadRoom(fromRoom: Boolean = true) {
        if (!Utils.isNetworkConnected(mContext)) {
            toast(com.gold.footimpression.R.string.net_error)
        } else {
            mActivity!!.showProgressDialog { }
            mLoginPresenter!!.getRoom<RoomAndCardModule>() { code, msg, result ->
                mActivity!!.closeProgressDialog()

                if (CodeUtils.isSuccess(code)) {
                    mRoomCardModule = result as RoomAndCardModule
                    if (fromRoom) {
                        if ((result as RoomAndCardModule).zhongfang.size == 0) {
                            toast(R.string.no_room)
                        } else {
                            initRoomPopwindow((result as RoomAndCardModule).zhongfang)
                            showPop(mRoomPopwindow, mBinding!!.llRoom)
                        }

                    } else {
                        if ((result as RoomAndCardModule).shoupai.size == 0) {
                            toast(R.string.no_shoupai)
                        } else {
                            initShoupaiPopwindow((result as RoomAndCardModule).shoupai)
                            showPop(mShoupaiPopwindow, mBinding!!.llCard)
                        }

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
            mActivity!!.showProgressDialog { }
            mLoginPresenter!!.getReicevers<MutableList<ReiceverModule>>() { code, msg, result ->
                mActivity!!.closeProgressDialog()

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
    fun loadOrderDetail(digndanUid: String) {
        if (!Utils.isNetworkConnected(mContext)) {
            toast(com.gold.footimpression.R.string.net_error)
        } else {
            mActivity!!.showProgressDialog { }
            mLoginPresenter!!.getOrderDetail<MutableList<OrderDetailModule>>(digndanUid) { code, msg, result ->
                mActivity!!.closeProgressDialog()

                if (CodeUtils.isSuccess(code)) {

                    mServiceItems = result!!.constructItemServices()
                    preview()
                } else {
                    toast(msg!!)
                }
            }
        }
    }

    /**
     * 获取接待
     */
    fun updateDingDan(paramStr: String) {
        if (!Utils.isNetworkConnected(mContext)) {
            toast(com.gold.footimpression.R.string.net_error)
        } else {
            mActivity!!.showProgressDialog { }
            mLoginPresenter!!.updateDingdan<Unit>(paramStr) { code, msg, result ->
                mActivity!!.closeProgressDialog()
                toast(msg!!)
                if (CodeUtils.isSuccess(code)) {
                    mActivity!!.setResult(RESULT_OK)
                    mActivity!!.finish()
                } else {

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
            override fun onItemClick(itemView: View, position: Int, instance: Any, viewid: Int) {
            }

            override fun onItemClick(itemView: View, position: Int) {
                mRoomPopwindow!!.closePop()
                mCurrentServiceItem.mServiceEditModule.roomNum = lists[position].zhongfangBianma

            }

        }

    }

    private var mSelectedShoupaiHao = mutableListOf<String>()

    private var mCurrentSelectShoupai: String = ""
    private var mPreCurrentSelectShoupai: String = ""
    /**
     * init 手牌popwindow
     */
    fun initShoupaiPopwindow(lists: MutableList<RoomAndCardModule.Card>) {
        mShoupaiPopwindow = ListPopupWindow(mContext!!)
        mShoupaiPopwindow!!.setWidth(mBinding!!.llCard.width)
        mShoupaiPopwindow!!.popDatas = lists.filterShoupaiStringArrayName()
        mShoupaiPopwindow!!.mItemClick = object : OnItemClick {
            override fun onItemClick(itemView: View, position: Int, instance: Any, viewid: Int) {
            }

            override fun onItemClick(itemView: View, position: Int) {
                mShoupaiPopwindow!!.closePop()
                mPreCurrentSelectShoupai = mCurrentServiceItem.mServiceEditModule.shoupaiNumValue
                mCurrentSelectShoupai = mShoupaiPopwindow!!.popDatas[position]
                mCurrentServiceItem.mServiceEditModule.shoupaiNumValue = mShoupaiPopwindow!!.popDatas[position]
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
            override fun onItemClick(itemView: View, position: Int, instance: Any, viewid: Int) {
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
            override fun onItemClick(itemView: View, position: Int, instance: Any, viewid: Int) {
            }

            override fun onItemClick(itemView: View, position: Int) {
                mReiceverPopwindow!!.closePop()
                mCurrentServiceItem.mServiceEditModule.reicever = lists[position]

            }

        }

    }


    private fun MutableList<OrderDetailModule>.constructItemServices(): MutableList<ServiceItemModule> {
        val result = mutableListOf<ServiceItemModule>()
        var i = 0
        this.forEach {
            val item = ServiceItemModule()
            item.clicked = i == 0
            i++
            //接待人员
            var reciver = ReiceverModule(Utils.getDisplayName()!!, Utils.getGonghao()!!)

//            val reciver = ReiceverModule(it.jiedaiName, it.jiedaiGonghao)
            item.fuwuXiangmuMingcheng = it.fuwuXiangmuMingcheng
            item.price = it.price
            item.fuwuShichang = it.fuwuShichang
            item.plannerName = it.jishiName
//            item.mServiceEditModule.roomNum = it.mendianBianma
//            item.mServiceEditModule.shoupaiNumValue = it.shoupaihao
            item.mServiceEditModule.shoupaihao = it.shoupaihao
            item.mServiceEditModule.dingdanUid = it.dingdanUid
            item.mServiceEditModule.reicever = reciver

            result.add(item)
            mCurrentServiceItem = result[0].clone()
        }
        return result
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
            if (!mSelectedShoupaiHao.contains(it.shoupaihao)) {
                result.add(it.shoupaihao)
            }

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


