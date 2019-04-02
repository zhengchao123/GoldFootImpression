package com.gold.footimpression.ui.fragment

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.inputmethod.EditorInfo
import androidx.databinding.ObservableField
import androidx.databinding.library.baseAdapters.BR
import com.gold.footimpression.R
import com.gold.footimpression.bindingadapter.CommonAdapter
import com.gold.footimpression.databinding.OrderInputFragmentBinding
import com.gold.footimpression.databinding.ServiceItemsFragmentBinding
import com.gold.footimpression.module.*
import com.gold.footimpression.net.CodeUtils
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
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import java.util.*


class ServiceItemsFragment : BaseFragment() {

    var mBinding: ServiceItemsFragmentBinding? = null


    //团购码
    private var mLoginPresenter: UserAcountPresenter? = null
    //服务项目数据
    private var mServiceItems = mutableListOf<ServiceItemModule>()
    //服务技师数据
    private var mPlanners = mutableListOf<PlannerModule>()
    //服务项目
    private var mServiceItemsAdapter: CommonAdapter<ServiceItemModule>? = null
    //技师
    private var mPlannersAdapter: CommonAdapter<PlannerModule>? = null
    //时间
    private var timeModule: TimeModule? = null

    //存储已被选中的技师
    private var selectedPlanners = mutableSetOf<PlannerModule>()
    private var serviceCode = ""
    //门店编码
    private var doorCode = ""
    private var plannerVisible = ObservableField<Boolean>(false)
    private var visible = ObservableField<Boolean>(false)
    override fun getContentview() = com.gold.footimpression.R.layout.service_items_fragment


    override fun initBinding() {
        super.initBinding()
        mBinding = (dataBinding as com.gold.footimpression.databinding.ServiceItemsFragmentBinding)
        mLoginPresenter = UserAcountPresenter(this.activity)
    }

    override fun initView() {
        super.initView()

        mBinding!!.plannerVisible = plannerVisible
        mBinding!!.visible = visible

        val data = this.arguments
        doorCode = data!!["doorCode"] as String
        timeModule = data!!["time"] as TimeModule
        visible.set(false)
        loadServiceItems()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            val data = this.arguments
            val time = data!!["time"] as TimeModule
            val dcode =  data!!["doorCode"] as String
            if(!time.equals(timeModule)|| !dcode.equals(doorCode)){
                doorCode = data!!["doorCode"] as String
                timeModule = data!!["time"] as TimeModule
                visible.set(false)
                loadServiceItems()
            }

        }
    }

    override fun onStop() {
        super.onStop()
        mLoginPresenter!!.release()
    }

    override fun initListener() {
        super.initListener()

        val click = object : EventHandler(mContext, false) {
            override fun onClickView(view: View?) {
                super.onClickView(view)
                when (view!!.id) {
                    R.id.tv_customer_source -> {

                    }
                    R.id.iv_back -> {
                        (this@ServiceItemsFragment.activity as MainActivity).showFragment("ORDER_INPUT_FRAGMENT")
                    }
                    R.id.tv_comfire->{
                        (this@ServiceItemsFragment.activity as MainActivity).services.clear()
                        (this@ServiceItemsFragment.activity as MainActivity).services.addAll(mServiceItems)
                        (this@ServiceItemsFragment.activity as MainActivity).showFragment("SERVICE_EDIT_ITEMS")
                    }
                }
            }
        }
        mBinding!!.click = click


    }

    /**
     * 加載服务项目
     */
    private fun loadServiceItems() {

        if (!Utils.isNetworkConnected(mContext)) {
            toast(R.string.net_error)
        } else {
            (this.activity as BaseActivity).showProgressDialog { }
            mLoginPresenter!!.getServiceItems<MutableList<ServiceItemModule>>() { code, msg, result ->
                (this.activity as BaseActivity).closeProgressDialog()
                if (CodeUtils.isSuccess(code)) {

                    mServiceItems.clear()
                    mServiceItems.addAll(result!!)
                    mServiceItemsAdapter = mServiceItems.putToAdapter()
                    mBinding!!.itemsAdapter = mServiceItemsAdapter
                } else {
                    toast(msg!!)
                }
            }
        }

    }

    /**
     * 加載技师
     */
    private fun loadTechnician() {

        if (!Utils.isNetworkConnected(mContext)) {
            toast(com.gold.footimpression.R.string.net_error)
        } else {
            (this.activity as BaseActivity).showProgressDialog { }
            mLoginPresenter!!.getPlanners<MutableList<PlannerModule>>(
                doorCode,
                if (null != timeModule) timeModule!!.fullTime!! else "",
                if (null != timeModule) timeModule!!.shortTime!! else "",
                serviceCode,
                "true"
            ) { code, msg, result ->
                (this.activity as BaseActivity).closeProgressDialog()
                if (CodeUtils.isSuccess(code)) {
                    plannerVisible.set(true)
                    mPlanners.clear()
                    result!!.filterFuwuCode(serviceCode)
//                    mPlanners.addAll(result!!.filterFuwuCode(serviceCode))
                    mServiceItems.forEach { item ->
                        if (item.fuwuXiangmuBianma!!.equals(serviceCode)) {
                            item.filterPlanners = result.filterSelect()
                            mPlanners.addAll(item.filterPlanners)
                            item.planners = result
                            return@forEach
                        }
                    }
                    mPlannersAdapter = mPlanners.putPlannerToAdapter()
                    mBinding!!.technicianAdapter = mPlannersAdapter
                } else {
                    toast(msg!!)
                    visible.set(false)
                    plannerVisible.set(false)
                }
            }
        }

    }


    fun MutableList<ServiceItemModule>.putToAdapter(): CommonAdapter<ServiceItemModule> {

        return CommonAdapter(
            mContext!!, this, R.layout.item_service_items,
            BR.serviceItemModule
        ).let {
            it.setOnItemClick(object : OnItemClick {
                override fun onItemClick(itemView: View, position: Int, instance: Any,viewid:Int) {
                }

                override fun onItemClick(itemView: View, position: Int) {
                    toast("click $position value ${this@putToAdapter[position].fuwuXiangmuMingcheng}")

                    this@putToAdapter.forEach { item ->
                        item.selected = false
                    }
                    this@putToAdapter[position].selected = true
                    serviceCode = this@putToAdapter[position].fuwuXiangmuBianma!!
                    this@ServiceItemsFragment.mServiceItemsAdapter!!.update(this@putToAdapter)

                    var needLoad = true
                    mServiceItems.forEach { item ->
                        if (item.fuwuXiangmuBianma!!.equals(serviceCode)) {
                            if (item.planners.size > 0) {
                                needLoad = false
                                item.filterPlanners = item.planners.filterSelect()
//

                                //有数据不用再请求了
                                plannerVisible.set(true)
                                mPlanners.clear()
                                mPlanners.addAll(item.filterPlanners)

                                mPlannersAdapter = mPlanners.putPlannerToAdapter()
                                mBinding!!.technicianAdapter = mPlannersAdapter


                                return@forEach
                            }

                        }
                    }
                    if (needLoad) {
                        loadTechnician()
                    }

                }
            })
            it
        }
    }

    fun MutableList<PlannerModule>.filterFuwuCode(fuwuxiangmuCode: String): MutableList<PlannerModule> {
        this.forEach {
            it.fuwuxiangmuCode = fuwuxiangmuCode
        }
        return this
    }

    //当某个技师在某个服务项目中选中了后，需要在另一个服务项目中不显示
    fun MutableList<PlannerModule>.filterSelect(): MutableList<PlannerModule> {
        val result = mutableListOf<PlannerModule>()
        this.forEach link@{
            val gonghao = it.gonghao
            if (selectedPlanners.size == 0) {
                result.add(it)
            } else {
                result.add(it)

                selectedPlanners.forEach { item ->
                    if (gonghao!!.equals(item.gonghao)) {
                        if (!serviceCode.equals(item.fuwuxiangmuCode)) {
                            result.remove(it)
                        }

                    }
                }
            }

        }
        return result
    }

    fun MutableList<PlannerModule>.putPlannerToAdapter(): CommonAdapter<PlannerModule> {

        return CommonAdapter(
            mContext!!, this, R.layout.item_planner_items,
            BR.plannerModule, BR.click
        ).let {
            it.setOnItemClick(object : OnItemClick {
                override fun onItemClick(itemView: View, position: Int, instance: Any,viewid:Int) {
                    when (itemView.id) {
                        R.id.iv_selected -> {

                            for (i in 0 until this@ServiceItemsFragment.mPlanners.size) {
                                if (i == position) {
                                    mPlanners[position].selected =
                                        !mPlanners[position].selected
                                    mPlannersAdapter!!.update(mPlanners)
                                    if (mPlanners[position].selected) {
                                        selectedPlanners.add(mPlanners[position])
                                    } else {
                                        selectedPlanners.remove(mPlanners[position])
                                    }
                                    if (selectedPlanners.size > 0) {
                                        visible.set(true)
                                    } else {
                                        visible.set(false)
                                    }
                                    break
                                }
                            }
                        }
                        R.id.iv_hour -> {
//                            toast(" click hour $position")
                            for (i in 0 until this@ServiceItemsFragment.mPlanners.size) {
                                if (i == position) {
                                    this@ServiceItemsFragment.mPlanners[position].hour =
                                        !this@ServiceItemsFragment.mPlanners[position].hour
                                    this@ServiceItemsFragment.mPlannersAdapter!!.update(this@ServiceItemsFragment.mPlanners)
                                    break
                                }
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
}

