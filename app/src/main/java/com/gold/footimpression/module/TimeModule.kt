package com.gold.footimpression.module

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.gold.footimpression.BR
import com.gold.footimpression.net.utils.DateUtils
import com.gold.footimpression.net.utils.LogUtils
import java.io.Serializable

class TimeModule() : BaseObservable(), Serializable {
    constructor(currentTime: Long) : this() {
        fullTime = DateUtils.transferLongToDate("yyyy-MM-dd HH:mm:ss", currentTime)
        shortTime = DateUtils.transferLongToDate("HH:mm", currentTime)
        times = "" + currentTime
    }

    var fullTime: String = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.fullTime)
        }


    var shortTime: String? = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.shortTime)
        }

    var times: String? = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.times)
        }

    override fun equals(other: Any?): Boolean {
        if (null != other) {
            return (other as TimeModule).shortTime.equals(this.shortTime)
        }
        return super.equals(other)
    }
}