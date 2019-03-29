package com.gold.footimpression.net

class CodeUtils {

    companion object {
        var mapCode: MutableMap<Int, String> = mutableMapOf()

        init {
            mapCode.put(HttpCallBack.FAILE_CODE, "请求失败,请稍后重试")
            mapCode.put(HttpCallBack.SUCCESS_CODE, "请求成功")
            mapCode.put(HttpCallBack.SUCCESS_CODE_NO_DATA, "请求成功但是没有数据，请稍后重试")
            mapCode.put(HttpCallBack.FAILE_CODE_TIME_OUT, "网络超时,请稍后重试")
            mapCode.put(HttpCallBack.FAILE_CODE_TOKEN_DATED, "登录已失效")
            mapCode.put(HttpCallBack.FAILE_PICTRUE_GET, "获取图片失败，请选择其他图片")
        }

        fun isSuccess(code: Int) = code == HttpCallBack.SUCCESS_CODE
        fun isSuccessNodata(code: Int) = code == HttpCallBack.SUCCESS_CODE_NO_DATA
        fun isTokenFaild(code: Int) = code == HttpCallBack.FAILE_CODE_TOKEN_DATED
        fun getMsg(succesS_CODE: Int): String? {

            if (mapCode.containsKey(succesS_CODE)) {
                return mapCode[succesS_CODE]
            }
            return ""
        }
    }
}