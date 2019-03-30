package com.gold.footimpression.presenter

import android.app.Activity
import android.text.TextUtils
import com.gold.footimpression.R
import com.gold.footimpression.module.*
import com.gold.footimpression.net.*
import com.gold.footimpression.net.utils.LogUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rcb.financialservice.model.BaseNetArrayModule
import com.rcb.financialservice.model.BaseNetObjectModule
import com.rcb.financialservice.model.BaseNetStringModule
import okhttp3.Call
import okhttp3.Response

class UserAcountPresenter(activity: Activity?) {
    var activity: Activity? = null

    init {
        this.activity = activity
    }

    companion object {
        val TAG = this@Companion::class.java.simpleName!!
        var mInstance: UserAcountPresenter? = null
        fun getInstance(activity: Activity?): UserAcountPresenter {
            if (null == mInstance) {
                mInstance = UserAcountPresenter(activity)
            }
            return mInstance!!
        }

    }

    //登录
    fun <T> login(
        name: String,
        password: String,
        callBack: (code: Int, msg: String?, result: T?) -> Unit
    ) {
        val params = mutableMapOf<String, String>()
        params["name"] = name
        params["password"] = password

        Client2Server.doPostAsyn(params, object : HttpCallBack() {
            override fun onFailed(code: Int, exceptionMsg: String?, call: Call?) {
                super.onFailed(code, exceptionMsg, call)
                if (null == activity) {
                    return
                }
                runCallBack {
                    callBack(code, if(TextUtils.isEmpty(CodeUtils.getMsg(code)))exceptionMsg!! else CodeUtils.getMsg(code), null)
                }
            }

            override fun <T : Any?> onResponse(call: Call?, response: Response?, t: T) {
                super.onResponse(call, response, t)
                var data = ""
                try {
                    data = (t as BaseNetObjectModule).root.toString()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                if (null == activity) {
                    return
                }
                runCallBack {
                    if (!TextUtils.isEmpty(data) && (t as BaseNetObjectModule).success) {
                        LogUtils.e(TAG, data)
                        callBack(
                            HttpCallBack.SUCCESS_CODE,
                            activity!!.getString(R.string.login_success), Gson().fromJson(
                                data, object : TypeToken<AcountModule>() {}.type
                            )
                        )
                    } else {
                        var message = ""
                        if (TextUtils.isEmpty((t as BaseNetObjectModule).msg)) {
                            message = activity!!.getString(R.string.login_fail)
                        } else {
                            message = (t as BaseNetObjectModule).msg!!
                        }
                        callBack(
                            HttpCallBack.FAILE_CODE,
                            message, null
                        )
                    }
                }
            }
        }, object : TypeToken<BaseNetObjectModule>() {
        }.type, "login", Constants.URL_LOGIN)
    }


    //登录
    fun <T> getVipInfo(
        huiyuanTel: String,
        callBack: (code: Int, msg: String?, result: T?) -> Unit
    ) {
        val params = mutableMapOf<String, String>()
        params["huiyuanTel"] = huiyuanTel

        Client2Server.doPostAsyn(params, object : HttpCallBack() {
            override fun onFailed(code: Int, exceptionMsg: String?, call: Call?) {
                super.onFailed(code, exceptionMsg, call)
                if (null == activity) {
                    return
                }
                runCallBack {
                    callBack(code, if(TextUtils.isEmpty(CodeUtils.getMsg(code)))exceptionMsg!! else CodeUtils.getMsg(code), null)
                }
            }

            override fun <T : Any?> onResponse(call: Call?, response: Response?, t: T) {
                super.onResponse(call, response, t)
                var data = ""
                try {
                    data = (t as BaseNetObjectModule).root.toString()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                if (null == activity) {
                    return
                }
                runCallBack {
                    if (!TextUtils.isEmpty(data) && (t as BaseNetObjectModule).success) {
                        LogUtils.e(TAG, data)
                        callBack(
                            HttpCallBack.SUCCESS_CODE,
                            t.msg, Gson().fromJson(
                                data, object : TypeToken<VIPInfoModule>() {}.type
                            )
                        )
                    } else {
                        var message = ""
//                        if (TextUtils.isEmpty((t as BaseNetObjectModule).msg)) {
//                            message = activity!!.getString(R.string.login_fail)
//                        } else {
                        message = activity!!.getString(R.string.no_data)
//                        }
                        callBack(
                            HttpCallBack.FAILE_CODE,
                            message, null
                        )
                    }
                }
            }
        }, object : TypeToken<BaseNetObjectModule>() {
        }.type, "getVipInfo", Constants.URL_VIP_INFO)
    }


    /**
     * 下拉列表
     */
    fun <T> getDicts(
        key: String,
        callBack: (code: Int, msg: String?, result: T?) -> Unit
    ) {

        val params = mutableMapOf<String, String>()

        params["key"] = key

        Client2Server.doPostAsyn(params, object : HttpCallBack() {
            override fun onFailed(code: Int, exceptionMsg: String?, call: Call?) {
                super.onFailed(code, exceptionMsg, call)
                if (null == activity) {
                    return
                }
                runCallBack {
                    callBack(code, if(TextUtils.isEmpty(CodeUtils.getMsg(code)))exceptionMsg!! else CodeUtils.getMsg(code), null)
                }
            }

            override fun <K : Any?> onResponse(call: Call?, response: Response?, t: K) {
                super.onResponse(call, response, t)
                if (null == activity) {
                    return
                }

                val dataCount = (t as BaseNetArrayModule).root!!.size()
                val data = (t as BaseNetArrayModule).root!!.toString()

                runCallBack {
                    if ((t as BaseNetArrayModule).success) {
                        if (dataCount == 0) {
                            callBack(
                                HttpCallBack.SUCCESS_CODE_NO_DATA,
                                CodeUtils.getMsg(HttpCallBack.SUCCESS_CODE_NO_DATA), null
                            )
                        } else {
                            callBack(
                                HttpCallBack.SUCCESS_CODE,
                                CodeUtils.getMsg(HttpCallBack.SUCCESS_CODE),
                                Gson().fromJson(data, object : TypeToken<MutableList<CustomerSourceModule>>() {}.type)
                            )
                        }
                    } else {
                        callBack(
                            HttpCallBack.FAILE_CODE,
                            t.msg,
                            null
                        )
                    }


                }

            }
        }, object : TypeToken<BaseNetArrayModule>() {
        }.type, "getDicts", Constants.URL_DICTS)
    }
    /**
     * 服务项目
     */
    fun <T> getServiceItems(
        callBack: (code: Int, msg: String?, result: T?) -> Unit
    ) {

        val params = mutableMapOf<String, String>()
        Client2Server.doGetAsyn(params, object : HttpCallBack() {
            override fun onFailed(code: Int, exceptionMsg: String?, call: Call?) {
                super.onFailed(code, exceptionMsg, call)
                if (null == activity) {
                    return
                }
                runCallBack {
                    callBack(code, if(TextUtils.isEmpty(CodeUtils.getMsg(code)))exceptionMsg!! else CodeUtils.getMsg(code), null)
                }
            }

            override fun <K : Any?> onResponse(call: Call?, response: Response?, t: K) {
                super.onResponse(call, response, t)
                if (null == activity) {
                    return
                }

                val dataCount = (t as BaseNetArrayModule).root!!.size()
                val data = (t as BaseNetArrayModule).root!!.toString()

                runCallBack {
                    if ((t as BaseNetArrayModule).success) {
                        if (dataCount == 0) {
                            callBack(
                                HttpCallBack.SUCCESS_CODE_NO_DATA,
                                CodeUtils.getMsg(HttpCallBack.SUCCESS_CODE_NO_DATA), null
                            )
                        } else {
                            callBack(
                                HttpCallBack.SUCCESS_CODE,
                                CodeUtils.getMsg(HttpCallBack.SUCCESS_CODE),
                                Gson().fromJson(data, object : TypeToken<MutableList<ServiceItemModule>>() {}.type)
                            )
                        }
                    } else {
                        callBack(
                            HttpCallBack.FAILE_CODE,
                            t.msg,
                            null
                        )
                    }


                }

            }
        }, object : TypeToken<BaseNetArrayModule>() {
        }.type, "serviceitems", Constants.URL_SERVICE_ITEMS)
    }
    /**
     * 获取技师
     * mendianBianma
    daodianTime
    daodianHMStr
    fuwuxiangmuBianma
    isCurrentTime

     */
    fun <T> getPlanners(
        mendianBianma:String,
        daodianTime:String,
        daodianHMStr:String,
        fuwuxiangmuBianma:String,
        isCurrentTime:String,
        callBack: (code: Int, msg: String?, result: T?) -> Unit
    ) {

        val params = mutableMapOf<String, String>()
        if(!TextUtils.isEmpty(mendianBianma)){
            params["mendianBianma"]=mendianBianma
        }
        if(!TextUtils.isEmpty(daodianTime)){
            params["daodianTime"]=daodianTime
        }
        if(!TextUtils.isEmpty(daodianHMStr)){
            params["daodianHMStr"]=daodianHMStr
        }
        if(!TextUtils.isEmpty(fuwuxiangmuBianma)){
            params["fuwuxiangmuBianma"]=fuwuxiangmuBianma
        }
        if(!TextUtils.isEmpty(isCurrentTime)){
            params["isCurrentTime"]=isCurrentTime
        }
        Client2Server.doPostAsyn(params, object : HttpCallBack() {
            override fun onFailed(code: Int, exceptionMsg: String?, call: Call?) {
                super.onFailed(code, exceptionMsg, call)
                if (null == activity) {
                    return
                }
                runCallBack {
                    callBack(code, if(TextUtils.isEmpty(CodeUtils.getMsg(code)))exceptionMsg!! else CodeUtils.getMsg(code), null)
                }
            }

            override fun <K : Any?> onResponse(call: Call?, response: Response?, t: K) {
                super.onResponse(call, response, t)
                if (null == activity) {
                    return
                }

                val dataCount = (t as BaseNetArrayModule).root!!.size()
                val data = (t as BaseNetArrayModule).root!!.toString()

                runCallBack {
                    if ((t as BaseNetArrayModule).success) {
                        if (dataCount == 0) {
                            callBack(
                                HttpCallBack.SUCCESS_CODE_NO_DATA,
                                CodeUtils.getMsg(HttpCallBack.SUCCESS_CODE_NO_DATA), null
                            )
                        } else {
                            callBack(
                                HttpCallBack.SUCCESS_CODE,
                                CodeUtils.getMsg(HttpCallBack.SUCCESS_CODE),
                                Gson().fromJson(data, object : TypeToken<MutableList<PlannerModule>>() {}.type)
                            )
                        }
                    } else {
                        callBack(
                            HttpCallBack.FAILE_CODE,
                            t.msg,
                            null
                        )
                    }


                }

            }
        }, object : TypeToken<BaseNetArrayModule>() {
        }.type, "getPlanners", Constants.URL_PLANNER)
    }

    public fun cancelRequest(flag: String) {
        HttpManager.getmInstance().cancleCallByKey(flag)
    }

    fun release() {
        if (null != mInstance) {
            cancelRequest("login")
            cancelRequest("getVipInfo")
            cancelRequest("getDicts")
            cancelRequest("serviceitems")
            cancelRequest("getPlanners")
            mInstance = null
        }
    }

    fun runCallBack(run: () -> Unit) {
        activity!!.runOnUiThread { run() }
    }


}