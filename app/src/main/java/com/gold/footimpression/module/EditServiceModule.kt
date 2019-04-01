package com.gold.footimpression.module

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.gold.footimpression.BR

class EditServiceModule() : BaseObservable() {


    //折扣价
    var discountPrice = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.discountPrice)
        }
    //团购价
     var groupPrice = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.groupPrice)
        }
    //钟房编码
    var roomNum = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.roomNum)
        }

    //手牌号
    var shoupaiNumValue = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.shoupaiNumValue)
        }

    //接待
    var reicever = ReiceverModule()
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.reicever)
        }


}