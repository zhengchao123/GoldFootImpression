package com.gold.footimpression.module

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.gold.footimpression.BR
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