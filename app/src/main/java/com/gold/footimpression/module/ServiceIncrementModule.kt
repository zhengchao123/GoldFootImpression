package com.gold.footimpression.module

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.gold.footimpression.BR

/**
 * 增值服务
 */
class ServiceIncrementModule() : BaseObservable() {


//    zengzhiFuwuTypeName	string	增值服务项目类别
//    例：茶水、小吃、增值服务、留宿
//    zengzhiFuwuBianma	Integer	增值服务项目编码
//    zengzhiFuwuMingcheng	string	增值服务项目名称
//    price	BigDecimal	挂牌价
//    priceMember	BigDecimal	会员价（**所选订单是会员订单时使用该价格**）


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

    var price: String = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.price)
        }

    var priceMember: String = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.priceMember)
        }


}