package com.gold.footimpression.module

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.gold.footimpression.BR

/**
 * 增值订单
 */
class OrderIncrementModule() : BaseObservable() {


//    zengzhiFuwuTypeName	string	增值服务项目类别
//    例：茶水、小吃、增值服务、留宿
//    zengzhiFuwuBianma	Integer	增值服务项目编码
//    zengzhiFuwuMingcheng	string	增值服务项目名称
//    amount	BigDecimal	数量



    var zengzhiFuwuTypeName: String? = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.zengzhiFuwuTypeName)
        }


    var zengzhiFuwuBianma: String? = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.zengzhiFuwuBianma)
        }

    var zengzhiFuwuMingcheng: String = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.zengzhiFuwuMingcheng)
        }

    var amount: String = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.amount)
        }


//    gonghao	string	工号
//    name	string	姓名
//    waitingTime	Integer	等待时间


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


    var waitingTime: String = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.waitingTime)
        }

}