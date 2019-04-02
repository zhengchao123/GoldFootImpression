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

class OrderPresenter(activity: Activity?) {
    var activity: Activity? = null

    init {
        this.activity = activity
    }

    companion object {
        val TAG = this@Companion::class.java.simpleName!!
        var mInstance: OrderPresenter? = null
        fun getInstance(activity: Activity?): OrderPresenter {
            if (null == mInstance) {
                mInstance = OrderPresenter(activity)
            }
            return mInstance!!
        }

    }


    /**
     * 获取订单
     */
    fun <T> getOrders(
        start: String,
        limit: String,
        callBack: (code: Int, msg: String?, result: T?) -> Unit
    ) {

        val params = mutableMapOf<String, String>()
        if (!TextUtils.isEmpty(start)) {
            params["start"] = start
        }
        if (!TextUtils.isEmpty(limit)) {
            params["limit"] = limit
        }
        Client2Server.doPostAsyn(params, object : HttpCallBack() {
            override fun onFailed(code: Int, exceptionMsg: String?, call: Call?) {
                super.onFailed(code, exceptionMsg, call)
                if (null == activity) {
                    return
                }
                runCallBack {
                    callBack(
                        code,
                        if (TextUtils.isEmpty(CodeUtils.getMsg(code))) exceptionMsg!! else CodeUtils.getMsg(code),
                        null
                    )
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
                                Gson().fromJson(data, object : TypeToken<MutableList<OrderDetailModule>>() {}.type)
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
        }.type, "getOrders", Constants.URL_PREVIEW_ORDER)
    }

    //登录
    fun <T> getRoom(callBack: (code: Int, msg: String?, result: T?) -> Unit) {
        val params = mutableMapOf<String, String>()
        Client2Server.doGetAsyn(params, object : HttpCallBack() {
            override fun onFailed(code: Int, exceptionMsg: String?, call: Call?) {
                super.onFailed(code, exceptionMsg, call)
                if (null == activity) {
                    return
                }
                runCallBack {
                    callBack(
                        code,
                        if (TextUtils.isEmpty(CodeUtils.getMsg(code))) exceptionMsg!! else CodeUtils.getMsg(code),
                        null
                    )
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
                        LogUtils.i(TAG, data)
                        callBack(
                            HttpCallBack.SUCCESS_CODE,
                            t.msg, Gson().fromJson(
                                data, object : TypeToken<RoomAndCardModule>() {}.type
                            )
                        )
                    } else {
                        var message = activity!!.getString(R.string.request_faild)
                        callBack(
                            HttpCallBack.FAILE_CODE,
                            message, null
                        )
                    }
                }
            }
        }, object : TypeToken<BaseNetObjectModule>() {
        }.type, "getRoom", Constants.URL_GET_ROOM)
    }


    public fun cancelRequest(flag: String) {
        HttpManager.getmInstance().cancleCallByKey(flag)
    }

    fun release() {
        if (null != mInstance) {
            cancelRequest("getOrders")

            mInstance = null
        }
    }

    fun runCallBack(run: () -> Unit) {
        activity!!.runOnUiThread { run() }
    }


}