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
    fun <T> getHistory(
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
        }.type, "getHistory", Constants.URL_HISTORY_ORDER)
    }

    /**
     * 获取订单
     */
    fun <T> getZengzhi(
        callBack: (code: Int, msg: String?, result: T?) -> Unit
    ) {

        val params = mutableMapOf<String, String>()
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
                                Gson().fromJson(data, object : TypeToken<MutableList<OrderIncrementModule>>() {}.type)
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
        }.type, "getZengzhi", Constants.URL_GET_ZENGZHI)
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


    /**
     * 获取提成人
     *
     */
    fun <T> getJishiAuth(
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
                                Gson().fromJson(data, object : TypeToken<MutableList<ReiceverModule>>() {}.type)
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
        }.type, "getJishiAuth", Constants.URL_GET_AUTH)
    }


    /**
     * 添加增值服务
     * huiyuanZhanghao
    paramFuwuStr

     */
    fun <T> submitCreateService(
        huiyuanZhanghao: String,
        paramFuwuStr: String,
        callBack: (code: Int, msg: String?, result: T?) -> Unit
    ) {

        val params = mutableMapOf<String, String>()
        if (!TextUtils.isEmpty(huiyuanZhanghao)) {
            params["huiyuanZhanghao"] = huiyuanZhanghao
        }
        if (!TextUtils.isEmpty(paramFuwuStr)) {
            params["paramFuwuStr"] = paramFuwuStr
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
                                Gson().fromJson(
                                    data,
                                    object : TypeToken<MutableList<OrderIncrementModule>>() {}.type
                                )
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
        }.type, "submitCreateService", Constants.URL_ADD_SERVICES)
    }

    public fun cancelRequest(flag: String) {
        HttpManager.getmInstance().cancleCallByKey(flag)
    }

    fun release() {
        if (null != mInstance) {
            cancelRequest("getOrders")
            cancelRequest("getHistory")
            cancelRequest("getZengzhi")
            cancelRequest("getJishiAuth")
            cancelRequest("submitCreateService")
            mInstance = null
        }
    }

    fun runCallBack(run: () -> Unit) {
        activity!!.runOnUiThread { run() }
    }


}