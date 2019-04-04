package com.gold.footimpression.ui.fragment

import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.library.baseAdapters.BR
import com.gold.footimpression.R
import com.gold.footimpression.bindingadapter.CommonAdapter
import com.gold.footimpression.module.PlannerStateModule
import com.gold.footimpression.module.RoomStateModule
import com.gold.footimpression.ui.base.BaseFragment
import com.gold.footimpression.databinding.RoomStateFragmentBinding
import com.gold.footimpression.module.OrderDetailModule
import com.gold.footimpression.net.CodeUtils
import com.gold.footimpression.presenter.OrderPresenter
import com.gold.footimpression.ui.base.BaseActivity
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

    private var mPresenter: OrderPresenter? = null
    override fun getContentview() = R.layout.room_state_fragment


    override fun initBinding() {
        super.initBinding()
        mBinding = dataBinding as RoomStateFragmentBinding
    }

    override fun initData() {
        super.initData()
        mPresenter = OrderPresenter.getInstance(this.activity)
    }

    override fun initView() {
        super.initView()
        previewView()
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

}
