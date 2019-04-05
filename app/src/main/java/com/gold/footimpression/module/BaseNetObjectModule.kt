package com.gold.footimpression.module

import com.google.gson.JsonObject


class BaseNetObjectModule {
    var success:  Boolean = false
    var root: JsonObject? = null
    var msg: String? = ""
    var code:Int = 0
}