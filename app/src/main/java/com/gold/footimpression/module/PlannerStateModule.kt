package com.gold.footimpression.module

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.gold.footimpression.BR

/**
 * 技师状态
 */
class PlannerStateModule() : BaseObservable() {


//    gonghao	string	工号
//    name	string	姓名
//    jishiZhuangtai	string	空闲状态->说明：1=休息，2=忙碌
//    daojishi	BigDecimal	倒计时（单位：分）
//    zhongfangBianma	String	服务房间



    var gonghao: String? = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.gonghao)
        }


    var name: String? = ""
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