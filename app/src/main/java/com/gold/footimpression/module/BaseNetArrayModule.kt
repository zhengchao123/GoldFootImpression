package com.rcb.financialservice.model

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.json.JSONObject


class BaseNetArrayModule {
    var success: Boolean = false
    var root: JsonArray? = JsonArray()
    var msg: String? = ""
}