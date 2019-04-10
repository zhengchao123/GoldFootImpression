package com.gold.footimpression.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.databinding.ObservableField
import com.gold.footimpression.R
import com.gold.footimpression.databinding.OrderInputFragmentBinding
import com.gold.footimpression.module.CustomerSourceModule
import com.gold.footimpression.module.TimeModule
import com.gold.footimpression.module.VIPInfoModule
import com.gold.footimpression.net.CodeUtils
import com.gold.footimpression.presenter.UserAcountPresenter
import com.gold.footimpression.ui.activity.MainActivity
import com.gold.footimpression.ui.base.BaseActivity
import com.gold.footimpression.ui.base.BaseFragment
import com.gold.footimpression.ui.event.EventHandler
import com.gold.footimpression.ui.event.OnItemClick
import com.gold.footimpression.utils.Utils
import com.gold.footimpression.widget.BasePopupWindow
import com.gold.footimpression.widget.ListPopupWindow
import java.util.*


class OrderInputFragment : BaseFragment() {

    var mBinding: OrderInputFragmentBinding? = null

    //门店编码
    private var mDoorCode = ObservableField<String>("")
    //门店名称
    private var mDoorName = ObservableField<String>("")
    //会员账号
    private var mVipAcount = ObservableField<String>("")
    //时间
    private var time = ObservableField<String>("")
    //顾客来源
    private var customSource = ObservableField<String>("")
    private var customSourceCode = ObservableField<String>("")
    //是否团
    private var groupBuyAble = ObservableField<Boolean>(false)
    //团购平台
    private var platform = ObservableField<String>("")
    private var platformCode = ObservableField<String>("")
    //团购码
    private var groupCode = ObservableField<String>("")
    private var mSelectTimePosition = -1
    private var mVipModule = VIPInfoModule()
    private var mLoginPresenter: UserAcountPresenter? = null
    private var customSources = mutableListOf<CustomerSourceModule>()
    private var customPlatforms = mutableListOf<CustomerSourceModule>()
    private var mTimes = mutableListOf<TimeModule>()
    private var mCertificatesPop: BasePopupWindow? = null
    private var mTimePop: BasePopupWindow? = null
    private var mActivity: MainActivity? = null
    override fun getContentview() = com.gold.footimpression.R.layout.order_input_fragment

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = context as MainActivity?
    }

    override fun initBinding() {
        super.initBinding()
        mBinding = dataBinding as OrderInputFragmentBinding
        mLoginPresenter = UserAcountPresenter(this.activity)
    }

    override fun initView() {
        super.initView()
        mBinding!!.doorCode = mDoorCode
        mBinding!!.doorName = mDoorName
        mBinding!!.vipAcount = mVipAcount
        mBinding!!.vipModule = mVipModule
        mBinding!!.customSource = customSource
        mBinding!!.groupBuyAble = groupBuyAble
        mBinding!!.platform = platform
        mBinding!!.groupCode = groupCode
        mBinding!!.time = time
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        mActivity!!.mOrderModule.paramStr.tuangouquanHao = groupCode.get()
    }

    override fun initData() {
        super.initData()
        mDoorCode.set(Utils.getUserBumenCode())
        mDoorName.set(Utils.getUserBumenName())
        initTimes()
    }

    override fun onStop() {
        super.onStop()
        mLoginPresenter!!.release()
        closePopWindow(mCertificatesPop)
        closePopWindow(mTimePop)
        mCertificatesPop = null
        mTimePop = null
    }

    override fun initListener() {
        super.initListener()

        val click = object : EventHandler(mContext, false) {
            override fun onClickView(view: View?) {
                super.onClickView(view)

//                Utils.doPhotoPrint(mActivity!!,R.mipmap.icon_service)
//                return
                Utils.closeSoftKeyBord(mContext, mActivity!!)
                clearInputFocuse()
                when (view!!.id) {
                    com.gold.footimpression.R.id.tv_customer_source -> {
                        clearInputFocuse()
                        if (customSources.size == 0) {
                            loadDicts("203")
                        } else {
                            initCustomSourcePop("203", customSources)
                            showPop(mCertificatesPop, mBinding!!.tvCustomerSourceLayout)
                        }

                    }
                    com.gold.footimpression.R.id.tv_platform -> {
                        if (!groupBuyAble.get()!!) {
                            toast(com.gold.footimpression.R.string.group_promot)
                        } else {
                            if (customPlatforms.size == 0) {
                                loadDicts("201")
                            } else {
                                initCustomSourcePop("201", customPlatforms)
                                showPop(mCertificatesPop, mBinding!!.tvPlatformLayout)
                            }

                        }

                    }
                    R.id.tv_platform_code -> {
                        if (!groupBuyAble.get()!!) {
                            mBinding!!.tvPlatformCode.isFocusableInTouchMode = false
                            mBinding!!.tvPlatformCode.isFocusable = false
                            toast(com.gold.footimpression.R.string.group_promot_code)
                        } else {
                            mBinding!!.tvPlatformCode.isFocusableInTouchMode = true
                            mBinding!!.tvPlatformCode.isFocusable = true
                            mBinding!!.tvPlatformCode.requestFocus()
                            Utils.showSoftKeyBord(mContext, mBinding!!.tvPlatformCode)
                        }
                    }
                    com.gold.footimpression.R.id.tv_time -> {
                        if (null == mTimePop) {
                            initTimePop(mTimes.filterTimeStringArrayName())
                        }
                        showPop(mTimePop, mBinding!!.tvTimeLayout)
                    }
                    R.id.tv_next -> {
                        if (TextUtils.equals("", customSource.get())) {
                            toast(R.string.please_select_custom_sourse)
                            return
                        } else {
                            if (customSource.get()!!.contains("团购")) {
                                if (TextUtils.isEmpty(platform.get())) {
                                    toast(R.string.please_select_platform)
                                    return
                                }
                                if (TextUtils.isEmpty(groupCode.get())) {
                                    toast(R.string.please_enter_group_num)
                                    return
                                }
                            }
                        }
                        if (TextUtils.equals("", time.get())) {
                            toast(R.string.please_select_time)
                            return
                        }


                        val data = Bundle()
                        data.putString("doorCode", mDoorCode.get())
                        data.putSerializable("time", mTimes[mSelectTimePosition])
                        mActivity!!.group.set(customSource.get()!!.contains("团购"))
                        mActivity!!.coupon.set(customSource.get()!!.contains("优惠券"))
                        mActivity!!.showFragment("SERVICE_ITEMS_FRAGMENT", data)
                    }
                }
                clearInputFocuse()
            }
        }
        mBinding!!.click = click

        mBinding!!.tvVipAcount.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // 先隐藏键盘
                Utils.closeSoftKeyBord(mContext, mActivity!!)
                if (!TextUtils.isEmpty(v.text)) {
                    loadVipInfo()
                }

            }
            false
        }


    }

    private fun clearInputFocuse() {
        var view = (mBinding!!.tvVipAcount)
        view.clearFocus()
    }

    private fun loadVipInfo() {

        if (!Utils.isNetworkConnected(mContext)) {
            toast(com.gold.footimpression.R.string.net_error)
        } else {
            (this.activity as BaseActivity).showProgressDialog { }
            mLoginPresenter!!.getVipInfo<VIPInfoModule>(
                mVipAcount.get()!!
            ) { code, msg, result ->
                (this.activity as BaseActivity).closeProgressDialog()

                if (CodeUtils.isSuccess(code)) {
                    mVipModule.cardTypeName = result!!.cardTypeName
                    mVipModule.cardno = result.cardno
                    mVipModule.huiyuanName = result.huiyuanName
                    mVipModule.huiyuanTel = result.huiyuanTel
                    mVipModule.huiyuanZhanghao = result.huiyuanZhanghao
                    mVipModule.xianjin = result.xianjin
                    mActivity!!.mOrderModule.paramStr.huiyuanZhanghao =
                        mVipModule.huiyuanZhanghao
                    mActivity!!.mOrderModule.paramStr.huiyuanTel =
                        mVipModule.huiyuanTel
                } else {
                    toast(msg!!)
                }
            }
        }

    }

    fun loadDicts(key: String) {
        if (!Utils.isNetworkConnected(mContext)) {
            toast(com.gold.footimpression.R.string.net_error)
        } else {
            (this.activity as BaseActivity).showProgressDialog { }
            mLoginPresenter!!.getDicts<MutableList<CustomerSourceModule>>(
                key
            ) { code, msg, result ->
                (this.activity as BaseActivity).closeProgressDialog()

                if (CodeUtils.isSuccess(code)) {
//                    toast(msg!!)

                    if (TextUtils.equals(key, "203")) {
                        customSources.clear()
                        customSources.addAll(result!!)
                        initCustomSourcePop(key, customSources)
                        showPop(mCertificatesPop, mBinding!!.tvCustomerSourceLayout)
                    } else {
                        customPlatforms.clear()
                        customPlatforms.addAll(result!!)
                        initCustomSourcePop(key, customPlatforms)
                        showPop(mCertificatesPop, mBinding!!.tvPlatformLayout)
                    }

                } else {
                    toast(msg!!)
                }
            }
        }
    }


    /**
     * init 客戶來源，客户平台popwindow
     */
    fun initCustomSourcePop(key: String, lists: MutableList<CustomerSourceModule>) {
        mCertificatesPop = ListPopupWindow(mContext!!)
        if (TextUtils.equals(key, "203")) {
            mCertificatesPop!!.setWidth(mBinding!!.tvCustomerSourceLayout.width)
        } else {
            mCertificatesPop!!.setWidth(mBinding!!.tvPlatformLayout.width)
        }
        mCertificatesPop!!.popDatas = lists.filterStringArrayName()
        mCertificatesPop!!.mItemClick = object : OnItemClick {
            override fun onItemClick(itemView: View, position: Int, instance: Any, viewid: Int) {
            }

            override fun onItemClick(itemView: View, position: Int) {
                mCertificatesPop!!.closePop()


                if (TextUtils.equals(key, "203")) {
                    customSource.set(lists[position].cname)
                    customSourceCode.set(lists[position].ccode)
                    platform.set("")
                    platformCode.set("")
                    groupCode.set("")
                    mActivity!!.mOrderModule.paramStr.kehuLaiyuan =
                        customSourceCode.get()

                    if (lists[position].cname!!.contains("团购")) {
                        groupBuyAble.set(true)
                    } else {
                        groupBuyAble.set(false)
                    }
                } else {
                    platform.set(lists[position].cname)
                    platformCode.set(lists[position].ccode)
                    mActivity!!.mOrderModule.paramStr.tuangouHuodong =
                        platformCode.get()
                }

            }

        }

    }


    /**
     * init 时间popwindow
     */
    fun initTimePop(lists: MutableList<String>) {
        mTimePop = ListPopupWindow(mContext!!)
        (mTimePop as ListPopupWindow)!!.needTransation = true
        mTimePop!!.setWidth(mBinding!!.tvTimeLayout.width)
        mTimePop!!.popDatas = lists
        mTimePop!!.mItemClick = object : OnItemClick {
            override fun onItemClick(itemView: View, position: Int, instance: Any, viewid: Int) {
            }

            override fun onItemClick(itemView: View, position: Int) {
                mTimePop!!.closePop()
                mSelectTimePosition = position
                time.set(mTimes[position].shortTime)


                mActivity!!.mOrderModule.paramStr.daodianTime =
                    mTimes[position].fullTime
                mActivity!!.mOrderModule.paramStr.daodianHMStr =
                    mTimes[position].shortTime

            }

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

    private fun MutableList<CustomerSourceModule>.filterStringArrayName(): MutableList<String> {
        val result = mutableListOf<String>()
        this.forEach {
            result.add(it.cname!!)
        }
        return result
    }

    private fun MutableList<TimeModule>.filterTimeStringArrayName(): MutableList<String> {
        val result = mutableListOf<String>()
        this.forEach {
            result.add(it.shortTime!!)
        }
        return result
    }

    private fun initTimes() {
        var currentTime = System.currentTimeMillis()
        val calender = Calendar.getInstance()

        var minite = calender.get(Calendar.MINUTE)
        var yushu = minite % 10
        if (yushu > 0) {
            currentTime += (10 - yushu) * 60 * 1000
            calender.timeInMillis = currentTime
        }
        getAfterDay(calender)
        calender.set(Calendar.HOUR_OF_DAY, 0)
        calender.set(Calendar.MINUTE, 0)
        calender.set(Calendar.SECOND, 0)
        while (true) {
            if (currentTime < calender.timeInMillis) {
                mTimes.add(TimeModule(currentTime))
                currentTime += 1000 * 10 * 60
            } else {
                return
            }

        }
    }

    /**
     * 获取当前时间的后一天时间
     *
     * @param cl
     */
    private fun getAfterDay(cl: Calendar): Calendar {
        var day = cl.get(Calendar.DATE);
        cl.set(Calendar.DATE, day + 1);
        return cl;
    }


}


