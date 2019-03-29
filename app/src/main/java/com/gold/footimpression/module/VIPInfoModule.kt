package com.gold.footimpression.module

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.gold.footimpression.BR

/**
 * 會員信息
 */
class VIPInfoModule() : BaseObservable() {


    //    "huiyuanZhanghao": "1105027477194932224",
//    "huiyuanTel": "13981960806",
//    "huiyuanName": "李",
//    "cardno": "",
//    "cardTypeName": "普卡",
//    "xianjin": 904
//
    var huiyuanZhanghao: String? = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.huiyuanZhanghao)
        }


    var huiyuanTel: String? = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.huiyuanTel)
        }

    var huiyuanName: String = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.huiyuanName)
        }


    var cardno: String = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.cardno)
        }

    var cardTypeName: String = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.cardTypeName)
        }

    var xianjin: String = ""
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.xianjin)
        }


}