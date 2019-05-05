package com.gold.footimpression.module

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.gold.footimpression.BR

class EditServiceModule() : BaseObservable(),Cloneable {

    public override fun clone(): EditServiceModule {
        var person: EditServiceModule? = null
        try {
            person = super.clone() as EditServiceModule
        } catch (e: CloneNotSupportedException) {
            e.printStackTrace()
        }

        return person!!
    }


    var dingdanUid = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.dingdanUid)
        }
    var zhongfangBianma = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.zhongfangBianma)
        }
    var shoupaihao = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.shoupaihao)
        }
    var jiedaiGonghao = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.jiedaiGonghao)
        }
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
    //钟房名称
    var roomName = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.roomName)
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
    //宣传员姓名
    var xcrenName = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.xcrenName)
        }
    //宣传员编码
    var xcrenCode = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.xcrenCode)
        }

    //优惠金额
    var youhuijia = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.youhuijia)
        }
    //补差价
    var buchajia = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.buchajia)
        }

    //补差价
    var buchajiaValue = "否"
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.buchajiaValue)
        }

}