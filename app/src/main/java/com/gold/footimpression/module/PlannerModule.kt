package com.gold.footimpression.module

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.gold.footimpression.BR

/**
 * 技师[]
 */
class PlannerModule() : BaseObservable() {


//    gonghao 工号
//    name 姓名
//    waitingTime 等待时间


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

    var waitingTime: String = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.waitingTime)
        }

    var hour: Boolean = false
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.hour)
        }

    var selected: Boolean = false
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.selected)
        }
    var fuwuxiangmuCode: String = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.fuwuxiangmuCode)
        }
    override fun equals(other: Any?): Boolean {
        if (other is PlannerModule) {
            return this.gonghao.equals(other.gonghao)
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return gonghao.hashCode()
    }

//    override fun equals(other: Any?): Boolean {
//        return (other as PlannerModule).gonghao.equals(gonghao)
//    }
//
//    override fun hashCode(): Int {
//        return super.hashCode()
//    }
}