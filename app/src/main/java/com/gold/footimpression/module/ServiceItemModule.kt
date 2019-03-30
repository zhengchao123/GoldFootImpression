package com.gold.footimpression.module

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.gold.footimpression.BR

/**
 * 服务项目[]
 */
class ServiceItemModule() : BaseObservable() {


//    "fuwuXiangmuBianma": 1,编码
//    "fuwuXiangmuMingcheng": 名称
//    "fuwuShichang": 80, 时长
//    "price": 129, 价格
//    "priceMember": 89 会员价
    var fuwuXiangmuBianma: String? = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.fuwuXiangmuBianma)
        }


    var fuwuXiangmuMingcheng: String? = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.fuwuXiangmuMingcheng)
        }

    var fuwuShichang: String = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.fuwuShichang)
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
    var selected=false
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.selected)
        }
    var planners: MutableList<PlannerModule> = mutableListOf()
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.planners)
        }
}