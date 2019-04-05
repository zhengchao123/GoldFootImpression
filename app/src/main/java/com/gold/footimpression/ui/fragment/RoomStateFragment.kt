package com.gold.footimpression.ui.fragment

import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.library.baseAdapters.BR
import com.gold.footimpression.R
import com.gold.footimpression.bindingadapter.CommonAdapter
import com.gold.footimpression.databinding.RoomStateFragmentBinding
import com.gold.footimpression.module.PlannerStateModule
import com.gold.footimpression.module.RoomStateModule
import com.gold.footimpression.net.CodeUtils
import com.gold.footimpression.presenter.OrderPresenter
import com.gold.footimpression.ui.base.BaseActivity
import com.gold.footimpression.ui.base.BaseFragment
import com.gold.footimpression.ui.event.EventHandler
import com.gold.footimpression.ui.event.OnItemClick
import com.gold.footimpression.utils.Utils
import com.google.gson.Gson
import java.lang.Exception

class RoomStateFragment : BaseFragment() {
    private var leftSearchText = ObservableField<String>()
    private var rightSearchText = ObservableField<String>()
    private var roomAdapter = CommonAdapter<RoomStateModule>()
    private var jishiAdapter = CommonAdapter<PlannerStateModule>()

    private var mRooms = mutableListOf<RoomStateModule>()
    private var mPlanners = mutableListOf<PlannerStateModule>()
    private var mBinding: RoomStateFragmentBinding? = null

    private var mPresenter: OrderPresenter? = null
    override fun getContentview() = R.layout.room_state_fragment


    override fun initBinding() {
        super.initBinding()
        mBinding = dataBinding as RoomStateFragmentBinding
        mBinding!!.leftSearchText = leftSearchText
        mBinding!!.rightSearchText = rightSearchText
    }

    override fun initData() {
        super.initData()
        mPresenter = OrderPresenter.getInstance(this.activity)
    }

    override fun initView() {
        super.initView()
        previewView()
    }

    override fun initListener() {
        super.initListener()
        super.initListener()

        val click = object : EventHandler(mContext, false) {
            override fun onClickView(view: View?) {
                when (view!!.id) {
                    R.id.tv_search -> {
                        var searchLeftKey = leftSearchText.get()
                        var searchRightKey = rightSearchText.get()
                        var leftResult = mRooms.searchByLeftKey(searchLeftKey)
                        var rightResult = mPlanners.searchByRightKey(searchRightKey)
                        if (leftResult.size > 0) {
                            roomAdapter.update(leftResult)
                        }
                        if (rightResult.size > 0) {
                            jishiAdapter.update(rightResult)
                        }

                        Utils.closeSoftKeyBord(mContext, this@RoomStateFragment.activity!!)
                        //TODO
                    }

                }

            }
        }
        mBinding!!.click = click
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            previewView()
        }
    }

    private fun previewView() {
        if (mRooms.size == 0) {
            loadRoomState()
        }
        if (mPlanners.size == 0) {
            loadPlanners()
        }

    }

    private fun loadRoomState() {

        if (!Utils.isNetworkConnected(mContext)) {
            toast(com.gold.footimpression.R.string.net_error)
        } else {
            (this.activity as BaseActivity).showProgressDialog { }
            mPresenter!!.getRoomStae<MutableList<RoomStateModule>>() { code, msg, result ->
                (this.activity as BaseActivity).closeProgressDialog()

                if (CodeUtils.isSuccess(code)) {
                    mRooms.clear()
                    mRooms.addAll(result!!)
                    roomAdapter = mRooms.putToAdapter()
                    mBinding!!.roomAdapter = roomAdapter
                } else {
                    toast(msg!!)
                }
            }
        }

    }


    private fun loadPlanners() {

        if (!Utils.isNetworkConnected(mContext)) {
            toast(com.gold.footimpression.R.string.net_error)
        } else {
            (this.activity as BaseActivity).showProgressDialog { }
            mPresenter!!.getPlannerState<MutableList<PlannerStateModule>> { code, msg, result ->
                (this.activity as BaseActivity).closeProgressDialog()

                if (CodeUtils.isSuccess(code)) {
                    mPlanners.clear()
                    mPlanners.addAll(result!!)
                    jishiAdapter = mPlanners.putToPlannerAdapter()
                    mBinding!!.jishiAdapter = jishiAdapter
                } else {
                    toast(msg!!)
                }
            }
        }

    }

    fun MutableList<RoomStateModule>.putToAdapter(): CommonAdapter<RoomStateModule> {

        return CommonAdapter(
            mContext!!, this, R.layout.item_room_state,
            BR.roomStateModule
        ).let {
            it.setOnItemClick(object : OnItemClick {
                override fun onItemClick(itemView: View, position: Int, instance: Any, viewid: Int) {

                }

                override fun onItemClick(itemView: View, position: Int) {
//                    toast(" click item $position ")
                }
            })
            it
        }
    }

    fun MutableList<PlannerStateModule>.putToPlannerAdapter(): CommonAdapter<PlannerStateModule> {

        return CommonAdapter(
            mContext!!, this, R.layout.item_planner_state,
            BR.plannerState
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

    private fun MutableList<RoomStateModule>.searchByLeftKey(key: String?): MutableList<RoomStateModule> {
        var results = mutableListOf<RoomStateModule>()
        this.forEach {
            try {
                if (it.roomStateStr().contains(key!!)) {
                    results.add(it)
                    return@forEach
                }
                if (it.zhongfangBianma.contains(key!!)) {
                    results.add(it)
                    return@forEach
                }
                if (it.chuangweishu.contains(key!!)) {
                    results.add(it)
                    return@forEach
                }
                if (it.kongxianshu.contains(key!!)) {
                    results.add(it)
                    return@forEach
                }
                if (it.floor.contains(key!!)) {
                    results.add(it)
                    return@forEach
                }
                if (it.daojishi.contains(key!!)) {
                    results.add(it)
                    return@forEach
                }
            } catch (e: Exception) {
                return@forEach
            }

        }
        return results
    }

    private fun MutableList<PlannerStateModule>.searchByRightKey(key: String?): MutableList<PlannerStateModule> {
        var results = mutableListOf<PlannerStateModule>()
        this.forEach {
            try {
                if (it.gonghao.contains(key!!)) {
                    results.add(it)
                    return@forEach
                }
                if (it.name.contains(key)) {
                    results.add(it)
                    return@forEach
                }
                if (it.plannerStateStr().contains(key)) {
                    results.add(it)
                    return@forEach
                }
                if (it.daojishi.contains(key)) {
                    results.add(it)
                    return@forEach
                }
                if (it.zhongfangBianma.contains(key)) {
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


