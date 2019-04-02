package com.gold.footimpression.ui.event
import android.view.View

 interface OnItemClick {
    fun onItemClick(itemView: View, position: Int)
    fun onItemClick(itemView: View, position: Int, instance: Any,viewId:Int=0)
}
