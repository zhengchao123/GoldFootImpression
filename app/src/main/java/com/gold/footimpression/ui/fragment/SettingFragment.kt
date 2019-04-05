package com.gold.footimpression.ui.fragment

import android.content.Intent
import android.text.TextUtils
import android.view.View
import androidx.databinding.ObservableField
import com.gold.footimpression.R
import com.gold.footimpression.application.GoldFootApplication
import com.gold.footimpression.databinding.SettingFragmentBinding
import com.gold.footimpression.net.CodeUtils
import com.gold.footimpression.presenter.OrderPresenter
import com.gold.footimpression.ui.activity.LoginActivity
import com.gold.footimpression.ui.base.BaseActivity
import com.gold.footimpression.ui.base.BaseFragment
import com.gold.footimpression.ui.event.EventHandler
import com.gold.footimpression.utils.Utils

class SettingFragment : BaseFragment() {

    private var newPwd2 = ObservableField<String>("")
    private var newPwd = ObservableField<String>("")
    private var oldPwd = ObservableField<String>("")
    private var updatePwd = ObservableField<Boolean>(false)

    private var mBinding: SettingFragmentBinding? = null
    private var mOrderPresenter: OrderPresenter? = null
    override fun getContentview() = R.layout.setting_fragment
    override fun initBinding() {
        super.initBinding()
        mBinding = dataBinding as SettingFragmentBinding
    }

    override fun initData() {
        super.initData()
        mOrderPresenter = OrderPresenter.getInstance(this.activity)
        mBinding!!.newPwd = newPwd
        mBinding!!.newPwd2 = newPwd2
        mBinding!!.oldPwd = oldPwd
        mBinding!!.updatePwd = updatePwd
    }

    override fun initListener() {
        super.initListener()


        val click = object : EventHandler(mContext, false) {
            override fun onClickView(view: View?) {
                when (view!!.id) {
                    R.id.tv_update_pwd -> {
                        updatePwd.set(!updatePwd.get()!!)
                        if(!updatePwd.get()!!){
                            Utils.closeSoftKeyBord(mContext,this@SettingFragment.activity!!)
                        }
                    }
                    R.id.tv_log_out -> {
                        logOut()
                    }
                    R.id.tv_done -> {
                        if (TextUtils.isEmpty(oldPwd.get()!!)) {
                            toast(R.string.please_enter_old_pwd)
                            return
                        }
                        if (TextUtils.isEmpty(newPwd.get()!!)) {
                            toast(R.string.please_enter_new_pwd)
                            return
                        }
                        if (TextUtils.isEmpty(newPwd2.get()!!)) {
                            toast(R.string.please_enter_new_pwd)
                            return
                        }
                        if (!TextUtils.equals(newPwd2.get()!!, newPwd.get()!!)) {
                            toast(R.string.not_equal_new_pwd)
                            return
                        } else {
                            updatePwd()
                        }
                    }
                }

            }
        }
        mBinding!!.click = click
    }

    override fun onStop() {
        super.onStop()
        mOrderPresenter!!.release()
        (this.activity as BaseActivity).closeProgressDialog()
    }

    private fun updatePwd() {


        if (!Utils.isNetworkConnected(mContext)) {
            toast(com.gold.footimpression.R.string.net_error)
        } else {
            (this.activity as BaseActivity).showProgressDialog { }
            mOrderPresenter!!.updatePwd<Unit>(oldPwd.get(), newPwd.get()) { code, msg, result ->
                (this.activity as BaseActivity).closeProgressDialog()
                toast(msg!!)
                if (CodeUtils.isSuccess(code)) {
                    Utils.clearUserInfo()
                    val intent = Intent(GoldFootApplication.getInstance(), LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    GoldFootApplication.getInstance().startActivity(intent)
                } else {
                    toast(msg)
                }
            }
        }
    }

    private fun logOut() {


        if (!Utils.isNetworkConnected(mContext)) {
            toast(com.gold.footimpression.R.string.net_error)
        } else {
            (this.activity as BaseActivity).showProgressDialog { }
            mOrderPresenter!!.logOut<Unit>() { code, msg, result ->
                (this.activity as BaseActivity).closeProgressDialog()
                toast(msg!!)
                if (CodeUtils.isSuccess(code)) {
                    Utils.clearUserInfo()
                    val intent = Intent(GoldFootApplication.getInstance(), LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    GoldFootApplication.getInstance().startActivity(intent)
                } else {
                    toast(msg)
                }
            }
        }


    }
}
