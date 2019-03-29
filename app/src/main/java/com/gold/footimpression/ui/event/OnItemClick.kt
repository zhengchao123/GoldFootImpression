package decoration.scsowing.com.decoration.ui.event

import android.view.View

 interface OnItemClick {
    fun onItemClick(itemView: View, position: Int)
    fun onItemClick(itemView: View, position: Int, instance: Any)
}
