package com.gold.footimpression.module

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.gold.footimpression.BR
import com.gold.footimpression.net.utils.LogUtils
import java.lang.Exception

/**
 * 增值订单
 */
class OrderIncrementModule() : BaseObservable(), Cloneable {

    public override fun clone(): OrderIncrementModule {
        var person: OrderIncrementModule? = null
        try {
            person = super.clone() as OrderIncrementModule
        } catch (e: CloneNotSupportedException) {
            e.printStackTrace()
        }

        return person!!
    }
//    zengzhiFuwuTypeName	string	增值服务项目类别
//    例：茶水、小吃、增值服务、留宿
//    zengzhiFuwuBianma	Integer	增值服务项目编码
//    zengzhiFuwuMingcheng	string	增值服务项目名称
//    amount	BigDecimal	数量

    var typeHead = false
    var typeHeadName = ""

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


    fun allAmount(): String {
        try {
            LogUtils.i("Zc", "== price ${price.toDouble()} amount = ${amount.toInt()}")
            return "" + (price.toDouble() * amount.toInt())
        } catch (e: Exception) {
            return ""
        }

    }

    var jinge: String = ""
        @Bindable
        get() = "" + (price.toDouble() * amount.toInt())
        set(value) {
            field = value
            notifyPropertyChanged(BR.jinge)
        }
    var priceMember: String = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.priceMember)
        }
    var price: String = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.price)
        }
    var zengzhiFuwuMingcheng: String = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.zengzhiFuwuMingcheng)
        }

    var amount: String = "0"
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.amount)
        }


    var dingdanUid: String = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.dingdanUid)
        }

    var gonghao: String = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.gonghao)
        }

    var tcrenGonghao = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.tcrenGonghao)
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

    override fun equals(other: Any?): Boolean {
        if (other is OrderIncrementModule) {
            return this.zengzhiFuwuTypeName.equals(other.zengzhiFuwuTypeName)
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return this.zengzhiFuwuTypeName.hashCode()
    }

}