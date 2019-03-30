package com.gold.footimpression.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.text.TextUtils
import android.transition.ChangeTransform
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.gold.footimpression.application.RcbApplication
import com.gold.footimpression.net.Constants
import com.gold.footimpression.net.utils.SharedPreferencesUtils


object Utils {

    val TAG = this.javaClass.simpleName!!


    fun mToast(context: Context, msg: String) {
        if (TextUtils.isEmpty(msg)) {
            return
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }


    fun isNetworkConnected(context: Context?): Boolean {
        if (context != null) {
            val mConnectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mNetworkInfo = mConnectivityManager.activeNetworkInfo
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable
            }
        }
        return false
    }

    fun getUserToken(): String? {
        return SharedPreferencesUtils.init(RcbApplication.getInstance()).getString(Constants.LOGIN_TOKEN_INFO)
    }

    fun saveUserToken(token: String) {
        SharedPreferencesUtils.init(RcbApplication.getInstance()).putString(Constants.LOGIN_TOKEN_INFO, token)
    }

    fun getUserBumenCode(): String? {
        return SharedPreferencesUtils.init(RcbApplication.getInstance()).getString(Constants.USER_BUMEN_CODE)
    }

    fun saveUserBumenCode(token: String) {
        SharedPreferencesUtils.init(RcbApplication.getInstance()).putString(Constants.USER_BUMEN_CODE, token)
    }

    fun getUserBumenName(): String? {
        return SharedPreferencesUtils.init(RcbApplication.getInstance()).getString(Constants.USER_BUMEN_NAME)
    }

    fun saveUserBumenName(token: String) {
        SharedPreferencesUtils.init(RcbApplication.getInstance()).putString(Constants.USER_BUMEN_NAME, token)
    }

    fun clearUserInfo() {
        SharedPreferencesUtils.init(RcbApplication.getInstance()).putString(Constants.LOGIN_TOKEN_INFO, "")
        SharedPreferencesUtils.init(RcbApplication.getInstance()).putString(Constants.USER_BUMEN_CODE, "")
        SharedPreferencesUtils.init(RcbApplication.getInstance()).putString(Constants.USER_BUMEN_NAME, "")
    }


    fun closeSoftKeyBord(context: Context, activity: Activity) {
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            activity.currentFocus.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    fun showSoftKeyBord(context: Context,  view: View) {
        var inputMethodManager = (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        view.requestFocus();
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);

    }
}