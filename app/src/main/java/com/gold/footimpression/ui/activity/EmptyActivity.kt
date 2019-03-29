package com.gold.footimpression.ui.activity

import android.content.Context
import android.content.Intent
import com.gold.footimpression.R
import com.gold.footimpression.ui.base.BaseActivity

class EmptyActivity : BaseActivity() {
    override fun getContentViewId(): Int {
        return R.layout.empty_activity;
    }


    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, EmptyActivity::class.java))
        }
    }

    override fun configLoadingPage() = false

}