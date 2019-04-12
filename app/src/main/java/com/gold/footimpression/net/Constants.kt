package com.gold.footimpression.net

import com.gold.footimpression.BuildConfig
import com.gold.footimpression.utils.Utils

object Constants {
    const val HOST = BuildConfig.URL
    const val HOST_NO_VERIFY = BuildConfig.URL_NO_VERIFY
    //登录
    const val URL_LOGIN = HOST_NO_VERIFY + "login"
    //vip信息
    const val URL_VIP_INFO = HOST + "searchHuiyuanByTelNo"
    //下拉列表
    const val URL_DICTS = HOST + "getDicts"
    //服务项目
    const val URL_SERVICE_ITEMS = HOST + "getFuwuXiangmu"
    //技师
    const val URL_PLANNER = HOST + "searchFreeJishi"
    //获取房间和手牌
    const val URL_GET_ROOM = HOST + "getOrderInitInfoForIpad"
    //获取接待
    const val URL_GET_RECIVER = HOST + "getJiedaiAuth"
    //下单
    const val URL_SUBMIT_ORDER = HOST + "addDingdanForIpad"

    //订单预览
    const val URL_PREVIEW_ORDER = HOST + "searchAllDingdanList"

    //历史订单订单预览
    const val URL_HISTORY_ORDER = HOST + "searchAllDingdanByUser"

    //获取提成人
    const val URL_GET_AUTH = HOST + "getJishiAuth"
    //获取增值服务
    const val URL_GET_ZENGZHI = HOST + "searchZengzhifuwu"

    //获取增值服务详情
    const val URL_GET_ZENGZHI_DETAIL = HOST + "searchAllDingdanZzList"
    //提交增值服务
    const val URL_ADD_SERVICES = HOST + "addDingdanZzForIpad"
    //获取房间状态
    const val URL_GET_ROMM_STATE = HOST + "searchMendianZhongfangStatus"
    //获取技师状态
    const val URL_GET_PLANNER_STATE = HOST + "searchMendianJishiStatus"
    //修改密码
    const val URL_UPDATE_PWD = HOST + "updatePwd"
    //注销
    const val URL_LOG_OUT = HOST + "logout"
    //订单详情
    const val URL_GET_DINGDAN_DETAIL = HOST + "searchAllDingdanDetail"

    //修改订单
    const val URL_UPDATE_DIANGDAN = HOST + "updateDingdanForIpad"

    val downloaadUrl = "http://pic.58pic.com/58pic/15/63/07/42Q58PIC42U_1024.jpg"


    val LOGIN_TOKEN_INFO = "user_token"
    val USER_BUMEN_CODE = "user_bumen_code"
    val USER_BUMEN_NAME = "user_bumen_name"
    val USER_DISPLAY_NAME = "user_display_name"
    val USER_GONGHAO_NAME = "user_gonghao_name"
    val USER_PRINT_ADDRESS = "user_print_address"
    val COMMAND_KEY = "command_key"
    const val COMMAND_STR = "stop"
    const val COMMAND_RELEASE_STR = "release"
}
