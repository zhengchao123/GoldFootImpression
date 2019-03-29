package com.gold.footimpression.net.utils

import android.util.Log
import com.gold.footimpression.BuildConfig


object LogUtils {

    private val HEAD_TAG = "decoration: "

    fun e(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.e(HEAD_TAG + tag, msg)
        }
    }

    fun i(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.i(HEAD_TAG + tag, msg)
        }
    }

    fun d(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.d(HEAD_TAG + tag, msg)
        }
    }
}
