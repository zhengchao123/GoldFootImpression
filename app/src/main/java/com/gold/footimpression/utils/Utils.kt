package com.gold.footimpression.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.text.TextUtils
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.gold.footimpression.application.RcbApplication
import com.gold.footimpression.net.Constants
import com.gold.footimpression.net.utils.SharedPreferencesUtils


object Utils {

    val TAG = this.javaClass.simpleName
    /**
     * get App versionCode
     * @param context
     * @return
     */
    fun getVersionCode(context: Context): String {
        var packageManager: PackageManager = context.getPackageManager();
        var packageInfo: PackageInfo
        var versionCode: String = ""
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = "" + packageInfo.versionCode
        } catch (e: Exception) {
            e.printStackTrace();
        }
        return versionCode
    }

    fun mToast(context: Context, msg: String) {
        if (TextUtils.isEmpty(msg)) {
            return
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }



    //启动惠生活  startFlag打开惠生活类型，B2C跳转四川馆-资阳馆  O2O跳转生活服务
    fun startHsh(startFlag: String, context: Context) {
        val i = "com.scsxy.hsh"
        if (isPkgInstalled(context, i)) {
            val intent = context.packageManager.getLaunchIntentForPackage(i)
            intent!!.putExtra("START_FLAG", "ZYRCB")
            intent!!.putExtra("flag", startFlag)
            context.startActivity(intent)
        } else {
            goMarket(context, i)
        }
    }

    //判断某个app是否安装
    fun isPkgInstalled(context: Context, pkgName: String): Boolean {
        val packageInfo: PackageInfo? = try {
            context.packageManager.getPackageInfo(pkgName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
        return packageInfo != null
    }

    /**
     * 跳到应用市场下载
     */
    private fun goMarket(context: Context, packageName: String) {
        try {
            val uri = Uri.parse("market://details?id=$packageName")
            val intent = Intent(Intent.ACTION_VIEW, uri)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(context, intent, null)
        } catch (notFoundException: ActivityNotFoundException) {
            mToast(context, "没有安装应用市场")
            notFoundException.printStackTrace()
        }
    }

//    /**
//     * 用户信息
//     */
//    fun getUserInfo(): NxUserModel? {
//        val loginString = SharedPreferencesUtils.init(RcbApplication.getInstance()).getString(Constants.LOGIN_INFO)
//        if (!TextUtils.isEmpty(loginString)) {
//            return Gson().fromJson(loginString, NxUserModel::class.java)
//        }
//        return null
//    }


    fun getUserToken(): String? {
        return SharedPreferencesUtils.init(RcbApplication.getInstance()).getString(Constants.LOGIN_TOKEN_INFO)
    }

    fun saveUserToken(token: String) {
        SharedPreferencesUtils.init(RcbApplication.getInstance()).putString(Constants.LOGIN_TOKEN_INFO, token)
    }

    fun clearUserToken() {
        SharedPreferencesUtils.init(RcbApplication.getInstance()).putString(Constants.LOGIN_TOKEN_INFO, "")
    }


}