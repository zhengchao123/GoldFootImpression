package com.gold.footimpression.module

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.gold.footimpression.BR

class AcountModule() : BaseObservable() {

    var token: String? = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.token)
        }


    var userName: String? = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.userName)
        }
    var displayName: String = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.displayName)
        }
    //门店编码
    var bumenCode: String = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.bumenCode)
        }
    //门店名称
    var bumenName: String = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.bumenName)
        }

    var gonghao = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.gonghao)
        }
    var printDomain = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.printDomain)
        }


}