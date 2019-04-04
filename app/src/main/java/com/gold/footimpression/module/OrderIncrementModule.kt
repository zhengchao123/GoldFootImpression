package com.gold.footimpression.module

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.gold.footimpression.BR
import com.gold.footimpression.R
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

    var iconId = R.mipmap.icon_service
    var typeHead = false
    var typeHeadName = ""

    fun _iconId(): Int {
        var id = iconId
        when (typeHeadName) {
            "增值服务" -> {
                id = R.mipmap.icon_service
            }
            "茶水" -> {
                id = R.mipmap.icon_tea
            }
            "小吃" -> {
                id = R.mipmap.icon_food
            }
            "留宿" -> {
                id = R.mipmap.icon_zhusu
            }
        }
        return id
    }

    var zengzhiFuwuTypeName: String? = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.zengzhiFuwuTypeName)
        }

    //
//    var zengzhiFuwuBianma: String? = ""
//        @Bindable
//        get() = field
//        set(value) {
//            field = value
//            notifyPropertyChanged(BR.zengzhiFuwuBianma)
//        }
    var dingdanJine: String? = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.dingdanJine)
        }


    fun allAmount(): String {
        try {
            LogUtils.i("Zc", "== price ${price.toDouble()} amount = ${amount.toInt()}")
            return "" + (price.toDouble() * amount.toInt())
        } catch (e: Exception) {
            return ""
        }

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


    var gonghao: String = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.gonghao)
        }
    var jishiGonghao: String = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.jishiGonghao)
        }


    var dingdanUid = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.dingdanUid)
        }


    var tcrenGonghao = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.tcrenGonghao)
        }
    var tcrenName = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.tcrenName)
        }


    var name: String = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }


//    var waitingTime: String = ""
//        @Bindable
//        get() = field
//        set(value) {
//            field = value
//            notifyPropertyChanged(BR.waitingTime)
//        }

    override fun equals(other: Any?): Boolean {
        if (other is OrderIncrementModule) {
            return this.zengzhiFuwuTypeName.equals(other.zengzhiFuwuTypeName)
        }
        return super.equals(other)
    }
        var zengzhiFuwuBianma: String? = ""
            @Bindable
            get() = field
            set(value) {
                field = value
                notifyPropertyChanged(BR.zengzhiFuwuBianma)
            }
    override fun hashCode(): Int {
        return this.zengzhiFuwuTypeName.hashCode()
    }

}