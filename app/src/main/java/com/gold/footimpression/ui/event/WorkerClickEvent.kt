package com.gold.footimpression.ui.event

import android.content.Context

open class WorkerClickEvent<T>(context: Context) : EventHandler(context) {

     var mClickFlag: String? = null

     var instance: T? = null

     constructor(context: Context, clickFlag: String?) : this(context) {
        this.mClickFlag = clickFlag
    }

     constructor(context: Context, any: T) : this(context) {
        this.instance = any
    }
}