package com.gold.footimpression.presenter

import android.app.Activity
import android.text.TextUtils
import com.gold.footimpression.module.*
import com.gold.footimpression.net.*
import com.gold.footimpression.utils.Utils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

                val dataCount = (t as BaseNetArrayModule).root.size()
                val data = (t as BaseNetArrayModule).root.toString()

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
        huiyuanZhanghao: String?,
        paramFuwuStr: String?,
        callBack: (code: Int, msg: String?, result: T?) -> Unit
    ) {

        val params = mutableMapOf<String, String>()
        if (!TextUtils.isEmpty(huiyuanZhanghao)) {
            params["huiyuanZhanghao"] = huiyuanZhanghao!!
        }
        if (!TextUtils.isEmpty(paramFuwuStr)) {
            params["paramFuwuStr"] = paramFuwuStr!!
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
//                        if (dataCount == 0) {
//                            callBack(
//                                HttpCallBack.SUCCESS_CODE,
//                                CodeUtils.getMsg(HttpCallBack.SUCCESS_CODE), null
//                            )
//                        } else {
                        callBack(
                            HttpCallBack.SUCCESS_CODE,
                            CodeUtils.getMsg(HttpCallBack.SUCCESS_CODE),
                            Gson().fromJson(
                                data,
                                object : TypeToken<MutableList<OrderIncrementModule>>() {}.type
                            )
                        )
//                        }
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


    /**

    打印

     */
    fun <T> submitPrint(
        printJson: String?,
        callBack: (code: Int, msg: String?, result: T?) -> Unit
    ) {

        val params = mutableMapOf<String, String>()
        if (!TextUtils.isEmpty(printJson)) {
            params["printJson"] = printJson!!
        }
        Client2Server.doPostAsyn(params, object : HttpCallBack() {
            override fun onFailed(code: Int, exceptionMsg: String?, call: Call?) {
                super.onFailed(code, exceptionMsg, call)
                if (null == activity) {
                    return
                }
            }

            override fun <K : Any?> onResponse(call: Call?, response: Response?, t: K) {
                super.onResponse(call, response, t)
                if (null == activity) {
                    return
                }

            }
        }, object : TypeToken<BaseNetArrayModule>() {
        }.type, "print", Utils.getPrintAddress() + "/print")
    }


    /**
     * 获取订单
     */
    fun <T> getZengzhiDetails(
        dingdanUid: String,
        callBack: (code: Int, msg: String?, result: T?) -> Unit
    ) {

        val params = mutableMapOf<String, String>()
        if (!TextUtils.isEmpty(dingdanUid)) {
            params["dingdanUid"] = dingdanUid
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
        }.type, "getZengzhiDetails", Constants.URL_GET_ZENGZHI_DETAIL)
    }


    /**
     * 获取订单
     */
    fun <T> getRoomStae(
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
                                Gson().fromJson(data, object : TypeToken<MutableList<RoomStateModule>>() {}.type)
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
        }.type, "getRoomState", Constants.URL_GET_ROMM_STATE)
    }

    /**
     * 获取订单
     */
    fun <T> getPlannerState(
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
                                Gson().fromJson(data, object : TypeToken<MutableList<PlannerStateModule>>() {}.type)
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
        }.type, "getPlannerState", Constants.URL_GET_PLANNER_STATE)
    }

    /**
     * 获取订单
     */
    fun <T> updatePwd(
        oldPass: String?,
        newPass: String?,
        callBack: (code: Int, msg: String?, result: T?) -> Unit
    ) {

        val params = mutableMapOf<String, String>()
        if (!TextUtils.isEmpty(oldPass)) {
            params["oldPass"] = oldPass!!
        }
        if (!TextUtils.isEmpty(newPass)) {
            params["newPass"] = newPass!!
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

                runCallBack {
                    if ((t as BaseNetArrayModule).success) {
                        callBack(
                            HttpCallBack.SUCCESS_CODE,
                            CodeUtils.getMsg(HttpCallBack.SUCCESS_CODE), null
                        )
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
        }.type, "updatePwd", Constants.URL_UPDATE_PWD)
    }

    /**
     * 获取订单
     */
    fun <T> logOut(
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

                runCallBack {
                    if ((t as BaseNetPwdModule).success) {
                        callBack(
                            HttpCallBack.SUCCESS_CODE,
                            CodeUtils.getMsg(HttpCallBack.SUCCESS_CODE), null
                        )
                    } else {
                        callBack(
                            HttpCallBack.FAILE_CODE,
                            t.msg,
                            null
                        )
                    }


                }

            }
        }, object : TypeToken<BaseNetPwdModule>() {
        }.type, "logOut", Constants.URL_LOG_OUT)
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
            cancelRequest("getZengzhiDetails")
            cancelRequest("getRoomState")
            cancelRequest("getPlannerState")
            cancelRequest("updatePwd")
            cancelRequest("logOut")
            cancelRequest("print")

            mInstance = null
        }
    }

    fun runCallBack(run: () -> Unit) {
        activity!!.runOnUiThread { run() }
    }


}