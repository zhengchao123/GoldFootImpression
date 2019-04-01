package com.gold.footimpression.module

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.Observable
import com.gold.footimpression.BR

/**
 *房间和手牌
 */
class RoomAndCardModule() : BaseObservable() {


//    gonghao 工号
//    name 姓名


    var zhongfang: MutableList<Room> = mutableListOf()
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.zhongfang)
        }


    var shoupai = mutableListOf<Card>()
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.shoupai)
        }


    inner class Room() : BaseObservable() {
        //zhongfangBianma 钟房编码（房间号）
        //zhongfangMingcheng  钟房名称
        // kongxianshu  空闲床位

        var zhongfangBianma: String? = ""
            @Bindable
            get() = field
            set(value) {
                field = value
                notifyPropertyChanged(BR.zhongfangBianma)
            }

        var zhongfangMingcheng: String? = ""
            @Bindable
            get() = field
            set(value) {
                field = value
                notifyPropertyChanged(BR.zhongfangMingcheng)
            }

        var kongxianshu: String? = ""
            @Bindable
            get() = field
            set(value) {
                field = value
                notifyPropertyChanged(BR.kongxianshu)
            }

    }

    inner class Card() : BaseObservable() {
//        shoupaihao  手牌号
//        shoupaiLeixing  手牌类型

        var shoupaihao: String? = ""
            @Bindable
            get() = field
            set(value) {
                field = value
                notifyPropertyChanged(BR.zhongfangMingcheng)
            }

        var shoupaiLeixing: String? = ""
            @Bindable
            get() = field
            set(value) {
                field = value
                notifyPropertyChanged(BR.kongxianshu)
            }
    }
}