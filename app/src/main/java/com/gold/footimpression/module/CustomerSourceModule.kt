package com.gold.footimpression.module

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.gold.footimpression.BR

/**
 * 刻画来源
 */
class CustomerSourceModule() : BaseObservable() {
    //编码
    var ccode: String? = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.ccode)
        }

    //名称
    var cname: String? = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.cname)
        }

}