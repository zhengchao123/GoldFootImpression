package com.gold.footimpression.module

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.gold.footimpression.BR

/**
 * 服务项目[]
 */
class ServiceItemModule() : BaseObservable(),Cloneable  {


    public override fun clone(): ServiceItemModule {
        var person: ServiceItemModule? = null
        try {
            person = super.clone() as ServiceItemModule
        } catch (e: CloneNotSupportedException) {
            e.printStackTrace()
        }

        return person!!
    }


    //    "fuwuXiangmuBianma": 1,编码
//    "fuwuXiangmuMingcheng": 名称
//    "fuwuShichang": 80, 时长
//    "price": 129, 价格
//    "priceMember": 89 会员价
    var dingdanUid: String? = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.dingdanUid)
        }

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
    var selected = false
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.selected)
        }

    var clicked = false
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.clicked)
        }

    var planners: MutableList<PlannerModule> = mutableListOf()
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.planners)
        }
    var filterPlanners: MutableList<PlannerModule> = mutableListOf()
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.filterPlanners)
        }

    fun loadPlannerName(): String {
        planners.forEach {
            if (it.selected) {
                return it.name!!
            }
        }
        return ""
    }

    //所选技师
    var plannerName = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.plannerName)
        }
    //所选技师
    var plannerGonghao = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.plannerGonghao)
        }
    var avalibleData = false
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.avalibleData)
        }

    //是否此项目选中了钟点
    var selectHourService = false
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.avalibleData)
        }
    //服务编辑信息
    var mServiceEditModule = EditServiceModule()
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.mServiceEditModule)
        }



}