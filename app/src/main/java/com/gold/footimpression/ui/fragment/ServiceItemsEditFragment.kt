package com.gold.footimpression.ui.fragment

import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.library.baseAdapters.BR
import com.gold.footimpression.R
import com.gold.footimpression.bindingadapter.CommonAdapter
import com.gold.footimpression.module.PlannerModule
import com.gold.footimpression.module.ServiceItemModule
import com.gold.footimpression.net.CodeUtils
import com.gold.footimpression.presenter.UserAcountPresenter
import com.gold.footimpression.ui.activity.MainActivity
import com.gold.footimpression.ui.base.BaseActivity
import com.gold.footimpression.ui.base.BaseFragment
import com.gold.footimpression.ui.event.EventHandler
import com.gold.footimpression.ui.event.OnItemClick
import com.gold.footimpression.utils.Utils


class ServiceItemsEditFragment : BaseFragment() {

    var mBinding: com.gold.footimpression.databinding.ServiceItemsEditFragmentBinding? = null


    //团购码
    private var mLoginPresenter: UserAcountPresenter? = null
    //服务项目数据
    private var mServiceItems = mutableListOf<ServiceItemModule>()
    //服务项目
    private var mServiceItemsAdapter: CommonAdapter<ServiceItemModule>? = null

    //是团购
    private var group = ObservableField<Boolean>(false)
    //是优惠券
    private var coupon = ObservableField<Boolean>(false)

    private var visible = ObservableField<Boolean>(false)
    override fun getContentview() = com.gold.footimpression.R.layout.service_items_edit_fragment


    override fun initBinding() {
        super.initBinding()
        mBinding = (dataBinding as com.gold.footimpression.databinding.ServiceItemsEditFragmentBinding)

        mBinding!!.coupon = coupon
        mBinding!!.group = group
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
                    R.id.tv_edit_sure -> {

                    }
                    R.id.tv_comfire -> {

                    }
                    R.id.iv_back -> {
                        (this@ServiceItemsEditFragment.activity as MainActivity).showFragment("SERVICE_ITEMS_FRAGMENT")
                    }

                }
            }
        }
        mBinding!!.click = click


    }


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
                    value.clicked = i == 0
                    value.selected = false
                    value.filterPlanners = it.filterPlanners
                    value.planners = it.planners
                    value.fuwuXiangmuMingcheng = it.fuwuXiangmuMingcheng
                    value.fuwuShichang = it.fuwuShichang
                    value.price = it.price
                    value.priceMember = it.priceMember
                    value.plannerName = item.name!!
                    it.avalibleData = true
                    result.add(value)
                    i++

                }
            }

        }
        return result
    }


}

