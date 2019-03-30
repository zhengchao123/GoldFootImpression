package com.gold.footimpression.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.databinding.ObservableField
import com.gold.footimpression.R
import com.gold.footimpression.databinding.ActivityLoginBinding
import com.gold.footimpression.module.AcountModule
import com.gold.footimpression.net.CodeUtils
import com.gold.footimpression.presenter.UserAcountPresenter
import com.gold.footimpression.ui.base.BaseActivity
import com.gold.footimpression.ui.event.EventHandler
import com.gold.footimpression.utils.Utils

class LoginActivity : BaseActivity() {

    private var mUserName = ObservableField<String>("")
    private var mPassword = ObservableField<String>("")
    private var mBinding: ActivityLoginBinding? = null
    private var mLoginPresenter: UserAcountPresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!TextUtils.isEmpty(Utils.getUserToken())) {
            gotoMain()
        }
    }

    override fun getContentViewId() = R.layout.activity_login

    override fun initBinding() {
        super.initBinding()
        mBinding = binding as ActivityLoginBinding
        mLoginPresenter = UserAcountPresenter(this)
    }

    override fun onStop() {
        super.onStop()
        mLoginPresenter!!.release()
    }

    override fun initView() {
        super.initView()
        closeTitleBar(true)
        mBinding!!.userName = mUserName
        mBinding!!.pwd = mPassword
    }

    override fun initListener() {
        super.initListener()

        val click = object : EventHandler(mContext, false) {
            override fun onClickView(view: View?) {
                super.onClickView(view)
                login()
            }
        }
        mBinding!!.click = click
    }

    override fun configLoadingPage(): Boolean = false

    fun login() {

        if (!Utils.isNetworkConnected(mContext)) {
            toast(R.string.net_error)
        } else {
            showProgressDialog { }
            mLoginPresenter!!.login<AcountModule>(
                mUserName.get()!!,
                mPassword.get()!!
            ) { code, msg, result ->
                closeProgressDialog()
                toast(msg!!)
                if (CodeUtils.isSuccess(code)) {
                    val token = (result as AcountModule).token
                    val code = result.bumenCode
                    val bumenName = result.bumenName
                    Utils.saveUserToken(token!!)
                    Utils.saveUserBumenCode(code)
                    Utils.saveUserBumenName(bumenName)
                    gotoMain()
                }
            }
        }


    }

    private fun gotoMain() {
        startActivity(Intent(mContext, MainActivity::class.java))
        this.finish()
    }

    override fun onBackClick() {
        this.finish()
    }
}
