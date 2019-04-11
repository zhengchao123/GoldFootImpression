package com.gold.footimpression.ui.fragment

import android.content.Context
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
import com.gold.footimpression.ui.activity.MainActivity
import com.gold.footimpression.ui.base.BaseFragment
import com.gold.footimpression.ui.event.EventHandler
import com.gold.footimpression.ui.event.OnItemClick
import com.gold.footimpression.utils.Utils

class RoomStateFragment : BaseFragment() {
    private var leftSearchText = ObservableField<String>()
    private var rightSearchText = ObservableField<String>()
    private var roomAdapter = CommonAdapter<RoomStateModule>()
    private var jishiAdapter = CommonAdapter<PlannerStateModule>()

    private var mRooms = mutableListOf<RoomStateModule>()
    private var mPlanners = mutableListOf<PlannerStateModule>()
    private var mBinding: RoomStateFragmentBinding? = null
    private var mActivity: MainActivity? = null
    private var mPresenter: OrderPresenter? = null
    override fun getContentview() = R.layout.room_state_fragment


    override fun initBinding() {
        super.initBinding()
        mBinding = dataBinding as RoomStateFragmentBinding
        mBinding!!.leftSearchText = leftSearchText
        mBinding!!.rightSearchText = rightSearchText
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = context as MainActivity?
    }
    override fun initData() {
        super.initData()
        mPresenter = OrderPresenter.getInstance(mActivity)
    }

    override fun initView() {
        super.initView()
        previewView()

        mBinding!!.swipeFreshLayout.setColorSchemeResources(
            R.color.colorPrimary,
            R.color.colorPrimary,
            R.color.colorPrimary
        )
        mBinding!!.swipeFreshJishiLayout.setColorSchemeResources(
            R.color.colorPrimary,
            R.color.colorPrimary,
            R.color.colorPrimary
        )
    }

    override fun initListener() {
        super.initListener()

        val click = object : EventHandler(mContext, false) {
            override fun onClickView(view: View?) {
                when (view!!.id) {
                    R.id.tv_search -> {
                        val searchLeftKey = leftSearchText.get()
                        val searchRightKey = rightSearchText.get()
                        val leftResult = mRooms.searchByLeftKey(searchLeftKey)
                        val rightResult = mPlanners.searchByRightKey(searchRightKey)
                        if (leftResult.size > 0) {
                            roomAdapter.update(leftResult)
                        }
                        if (rightResult.size > 0) {
                            jishiAdapter.update(rightResult)
                        }

                        Utils.closeSoftKeyBord(mContext, mActivity!!)
                        //TODO
                    }

                }

            }
        }
        mBinding!!.click = click

        mBinding!!.swipeFreshLayout.setOnRefreshListener {
            loadRoomState(true)
        }
        mBinding!!.swipeFreshJishiLayout.setOnRefreshListener {
            loadPlanners(true)
        }

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
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

    private fun loadRoomState(isFresh: Boolean = false) {

        if (!Utils.isNetworkConnected(mContext)) {
            toast(R.string.net_error)
            if (isFresh) {
                mBinding!!.swipeFreshLayout.isRefreshing = false
            }

        } else {
            if (!isFresh) {
                mActivity!!.showProgressDialog { }
            }
            mPresenter!!.getRoomStae<MutableList<RoomStateModule>>() { code, msg, result ->
                mActivity!!.closeProgressDialog()

                if (isFresh) {
                    mBinding!!.swipeFreshLayout.isRefreshing = false
                    if (CodeUtils.isSuccess(code)) {
                        toast(R.string.refresh_success)
                        mRooms.clear()
                        mRooms.addAll(result!!)
                        roomAdapter.update(mRooms)
                    } else {
                        toast(msg!!)
                    }
                } else {
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

    }


    private fun loadPlanners(isFresh: Boolean = false) {
//        mBinding!!.swipeFreshLayout.isRefreshing = false
        if (!Utils.isNetworkConnected(mContext)) {
            toast(R.string.net_error)
            if (isFresh) {
                mBinding!!.swipeFreshJishiLayout.isRefreshing = false
            }
        } else {
            if (!isFresh) {
                mActivity!!.showProgressDialog { }
            }

            mPresenter!!.getPlannerState<MutableList<PlannerStateModule>> { code, msg, result ->
                mActivity!!.closeProgressDialog()
                if (isFresh) {
                    mBinding!!.swipeFreshJishiLayout.isRefreshing = false
                    if (CodeUtils.isSuccess(code)) {
                        toast(R.string.refresh_success)
                        mPlanners.clear()
                        mPlanners.addAll(result!!)
                        jishiAdapter!!.update(mPlanners)
                    } else {
                        toast(msg!!)
                    }
                } else {
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


