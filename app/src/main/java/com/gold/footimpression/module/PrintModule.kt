package com.gold.footimpression.module

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.gold.footimpression.BR
import com.google.gson.Gson

/**
 * 订单预览
 */
class PrintModule() : BaseObservable() {

    var dingdanhao = ""
    var dingdanUid = ""
    var jiedaiGongName = ""
    var total = ""

    var rooms = mutableListOf<Room>()

    class Room {
        var fanjianhao = ""
        var slicks = mutableListOf<Service>()

        class Service {

            var shoupaihao = ""
            var zengzhiFuwuMingcheng = ""
            var amount = ""
            var price = ""
            var dingdanJine = ""


        }
    }


}