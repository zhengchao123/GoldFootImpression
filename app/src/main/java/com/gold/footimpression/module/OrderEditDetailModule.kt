package com.gold.footimpression.module

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.gold.footimpression.BR
import com.google.gson.Gson

/**
 * 订单预览
 */
class OrderEditDetailModule() : BaseObservable() ,Cloneable{


    public override fun clone(): OrderEditDetailModule {
        var person: OrderEditDetailModule? = null
        try {
            person = super.clone() as OrderEditDetailModule
        } catch (e: CloneNotSupportedException) {
            e.printStackTrace()
        }

        return person!!
    }


    var dingdanUid = ""
    var zhongfangBianma = ""
    var shoupaihao = ""
    var jiedaiGonghao = ""
    var jiedaiName = ""
    //接待
    var reicever = ReiceverModule()
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.reicever)
        }
}