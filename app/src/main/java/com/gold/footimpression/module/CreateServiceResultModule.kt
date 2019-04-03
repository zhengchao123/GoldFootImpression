package com.gold.footimpression.module

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.gold.footimpression.BR

/**
 * 增加增值服务返回模型
 */
class CreateServiceResultModule() : BaseObservable(), Cloneable {


//    gonghao 工号
//    name 姓名

    public override fun clone(): CreateServiceResultModule {
        var person: CreateServiceResultModule? = null
        try {
            person = super.clone() as CreateServiceResultModule
        } catch (e: CloneNotSupportedException) {
            e.printStackTrace()
        }

        return person!!
    }

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