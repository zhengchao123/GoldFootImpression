package com.gold.footimpression.module

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.gold.footimpression.BR
import com.gold.footimpression.R
import com.gold.footimpression.application.RcbApplication
import com.google.gson.Gson

/**
 * 房间状态
 */
class RoomStateModule() : BaseObservable() {

//
//    zhongfangBianma	string	钟房名称（编码）
//    chuangweishu	Integer	总床位数
//    kongxianshu	Integer	空床位数
//    fangjianZhuangtai	string	房间状态->说明：1=空闲，2=服务，3=休息
//    floor	Integer	楼层
//    daojishi	BigDecimal	倒计时（单位：分）

    fun roomStateStr(): String {
        var result = "空闲"
        when (fangjianZhuangtai) {
            "1" -> {
                result = "空闲"
            }
            "2" -> {
                result = "服务"
            }
            "3" -> {
                result = "休息"
            }
        }
        return result
    }

    fun stateColor(): Int {
        var color = ContextCompat.getColor(RcbApplication.mInstance.mContext, R.color.colorPrimary)
        when (fangjianZhuangtai) {
            "1" -> color = ContextCompat.getColor(RcbApplication.mInstance.mContext, R.color.colorPrimary)
            "2" -> {
                color = ContextCompat.getColor(RcbApplication.mInstance.mContext, R.color.red_state_color)
            }
            "3" -> {
                color = ContextCompat.getColor(RcbApplication.mInstance.mContext, R.color.red_state_color)
            }
        }
        return color

    }

    fun stateDrawable(): Drawable {
        var color = ContextCompat.getDrawable(RcbApplication.mInstance.mContext, R.mipmap.icon_state_free)
        when (fangjianZhuangtai) {
            "1" -> color = ContextCompat.getDrawable(RcbApplication.mInstance.mContext, R.mipmap.icon_state_free)
            "2" -> {
                color = ContextCompat.getDrawable(RcbApplication.mInstance.mContext, R.mipmap.icon_state_in_service)
            }
            "3" -> {
                color = ContextCompat.getDrawable(RcbApplication.mInstance.mContext, R.mipmap.icon_state_rest)
            }
        }
        return color!!

    }

    var zhongfangBianma = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.zhongfangBianma)
        }
    var chuangweishu = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.chuangweishu)
        }
    var kongxianshu = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.kongxianshu)
        }
    var fangjianZhuangtai = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.fangjianZhuangtai)
        }

    var floor = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.floor)
        }


    var daojishi = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.daojishi)
        }


}