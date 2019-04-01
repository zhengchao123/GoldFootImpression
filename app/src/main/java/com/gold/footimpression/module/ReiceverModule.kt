package com.gold.footimpression.module

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.gold.footimpression.BR

/**
 * 接待员[]
 */
class ReiceverModule() : BaseObservable() {


//    gonghao 工号
//    name 姓名


    var gonghao: String? = ""
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

    constructor(name: String, gonghao: String) : this() {
        this.name = name
        this.gonghao = gonghao
    }

}