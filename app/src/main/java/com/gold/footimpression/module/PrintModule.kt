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
    var jiedaiName = ""
    var total = ""

    var rooms = mutableListOf<Room>()

    class Room {
        var fangjianhao = ""
        var services = mutableListOf<Service>()

        class Service {

            var shoupaihao = ""
            var fuwuMingcheng = ""
            var amount = ""
            var price = ""
            var dingdanJine = ""


        }
    }


}