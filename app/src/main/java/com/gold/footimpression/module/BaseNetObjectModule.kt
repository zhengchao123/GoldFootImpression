package com.rcb.financialservice.model

import com.google.gson.JsonObject


class BaseNetObjectModule {
    var success:  Boolean = false
    var root: JsonObject? = null
    var msg: String? = ""
}