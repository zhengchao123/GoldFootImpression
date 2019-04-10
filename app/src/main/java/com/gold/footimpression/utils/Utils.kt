package com.gold.footimpression.utils

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.print.PrintHelper
import com.gold.footimpression.application.GoldFootApplication
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
        return SharedPreferencesUtils.init(GoldFootApplication.getInstance()).getString(Constants.LOGIN_TOKEN_INFO)
    }

    fun saveUserToken(token: String) {
        SharedPreferencesUtils.init(GoldFootApplication.getInstance()).putString(Constants.LOGIN_TOKEN_INFO, token)
    }

    fun getUserBumenCode(): String {
        return SharedPreferencesUtils.init(GoldFootApplication.getInstance()).getString(Constants.USER_BUMEN_CODE)
    }

    fun saveUserBumenCode(token: String) {
        SharedPreferencesUtils.init(GoldFootApplication.getInstance()).putString(Constants.USER_BUMEN_CODE, token)
    }

    fun getUserBumenName(): String? {
        return SharedPreferencesUtils.init(GoldFootApplication.getInstance()).getString(Constants.USER_BUMEN_NAME)
    }

    fun saveUserBumenName(token: String) {
        SharedPreferencesUtils.init(GoldFootApplication.getInstance()).putString(Constants.USER_BUMEN_NAME, token)
    }

    fun getDisplayName(): String? {
        return SharedPreferencesUtils.init(GoldFootApplication.getInstance()).getString(Constants.USER_DISPLAY_NAME)
    }

    fun saveCommandStr(command: String) {
        SharedPreferencesUtils.init(GoldFootApplication.getInstance()).putString(Constants.COMMAND_KEY, command)
    }

    fun getCommandStr(): String {
        return SharedPreferencesUtils.init(GoldFootApplication.getInstance()).getString(Constants.COMMAND_KEY, "")
    }

    fun saveDisplayName(token: String) {
        SharedPreferencesUtils.init(GoldFootApplication.getInstance()).putString(Constants.USER_DISPLAY_NAME, token)
    }

    fun getGonghao(): String? {
        return SharedPreferencesUtils.init(GoldFootApplication.getInstance()).getString(Constants.USER_GONGHAO_NAME)
    }

    fun saveGonghao(token: String) {
        SharedPreferencesUtils.init(GoldFootApplication.getInstance()).putString(Constants.USER_GONGHAO_NAME, token)
    }

    fun clearUserInfo() {
        SharedPreferencesUtils.init(GoldFootApplication.getInstance()).putString(Constants.LOGIN_TOKEN_INFO, "")
        SharedPreferencesUtils.init(GoldFootApplication.getInstance()).putString(Constants.USER_BUMEN_CODE, "")
        SharedPreferencesUtils.init(GoldFootApplication.getInstance()).putString(Constants.USER_BUMEN_NAME, "")
        SharedPreferencesUtils.init(GoldFootApplication.getInstance()).putString(Constants.USER_DISPLAY_NAME, "")
        SharedPreferencesUtils.init(GoldFootApplication.getInstance()).putString(Constants.USER_GONGHAO_NAME, "")
    }


    fun closeSoftKeyBord(context: Context, activity: Activity) {
        try {
            (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                activity.currentFocus.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun showSoftKeyBord(context: Context, view: View) {
        var inputMethodManager = (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        view.requestFocus();
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);

    }

    fun getCommand(): Boolean {
        return getCommandStr().equals(Constants.COMMAND_STR)
    }

     fun doPhotoPrint(activity: Activity,drawableId:Int) {
        val photoPrinter = PrintHelper(activity)
        photoPrinter.scaleMode = PrintHelper.SCALE_MODE_FIT
        val bitmap = BitmapFactory.decodeResource(
            activity.getResources(),
            drawableId
        )
        photoPrinter.printBitmap("droids.jpg - test print", bitmap)
    }
}