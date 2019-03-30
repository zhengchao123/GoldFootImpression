package com.gold.footimpression.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.NumberPicker
import android.widget.PopupWindow
import com.gold.footimpression.net.utils.DensyUtils


object ViewUtils {

    fun fitPopupWindowOverStatusBar(mPopupWindow: PopupWindow, needFullScreen: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                val mLayoutInScreen = PopupWindow::class.java.getDeclaredField("mLayoutInScreen")
                mLayoutInScreen.isAccessible = needFullScreen
                mLayoutInScreen.set(mPopupWindow, needFullScreen)
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }

        }
    }

    fun setNumberPickerDividerColor(context: Context, numberPicker: NumberPicker) {
        val pickerFields = NumberPicker::class.java.declaredFields
        var heightHasSet = false
        var colorHasSet = false
        for (pf in pickerFields) {

            if (pf.name.equals("mSelectionDividerHeight")) {
                pf.isAccessible = true
                pf.set(numberPicker, DensyUtils.dp2px(context, 0.5f));
                heightHasSet = true
                if (colorHasSet) {
                    break
                }
                continue
            }

            if (pf.name == "mSelectionDivider") {
                pf.isAccessible = true
                try {
                    //设置分割线的颜色值
                    pf.set(numberPicker, ColorDrawable(Color.parseColor("#D9D9D9")))
                    pf.set(numberPicker, 50)
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                } catch (e: Resources.NotFoundException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }

                colorHasSet = true
                if (heightHasSet) {
                    break
                }
                continue
            }
        }
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    @SuppressLint("ObsoleteSdkInt")
    fun hideBottomUIMenu(activity: Activity) {
        //隐藏虚拟按键，并且全屏
        val window = activity.window
        if (Build.VERSION.SDK_INT in 12..18) { // lower api
            val v = window.decorView
            v.systemUiVisibility = View.GONE
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            val decorView = window.decorView
            val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN)
            decorView.systemUiVisibility = uiOptions
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        );
        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        );

    }
}
