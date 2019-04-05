package com.gold.footimpression.module

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.gold.footimpression.BR
import com.gold.footimpression.R
import com.gold.footimpression.application.RcbApplication

/**
 * 技师状态
 */
class PlannerStateModule() : BaseObservable() {


//    gonghao	string	工号
//    name	string	姓名
//    jishiZhuangtai	string	空闲状态->说明：1=休息，2=忙碌
//    daojishi	BigDecimal	倒计时（单位：分）
//    zhongfangBianma	String	服务房间


    fun plannerStateStr(): String {
        var result = "空闲"
        when (jishiZhuangtai) {
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
        var color = R.color.colorPrimary
        when (jishiZhuangtai) {
            "1" -> color = R.color.colorPrimary
            "2" -> {
                color = R.color.red_state_color
            }
            "3" -> {
                color = R.color.red_state_color
            }
        }
        return color

    }

    fun stateDrawable(): Drawable {
        var color = ContextCompat.getDrawable(RcbApplication.mInstance.mContext, R.mipmap.icon_state_free)
        when (jishiZhuangtai) {
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

    var gonghao: String = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.gonghao)
        }


    var name: String = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }

    var jishiZhuangtai: String = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.jishiZhuangtai)
        }

    var daojishi: String = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.daojishi)
        }

    var zhongfangBianma: String = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.zhongfangBianma)
        }

}