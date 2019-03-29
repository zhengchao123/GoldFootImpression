package com.gold.footimpression.ui.base


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.gold.footimpression.R
import com.gold.footimpression.net.utils.LogUtils
import com.gold.footimpression.ui.event.EventHandler
import com.gold.footimpression.widget.TitleBar
import com.qmuiteam.qmui.util.QMUIStatusBarHelper

open class BaseActivity : AppCompatActivity() {
    val TAG = this.javaClass.simpleName
    lateinit var binding: ViewDataBinding

    var mProgress: ProgressBar? = null
    var mContentFrame: FrameLayout? = null
    var mViewTitle: TitleBar? = null

    var mContext: Context? = null
    override fun onCreate(savedInstanceState: Bundle?) :Unit{
        super.onCreate(savedInstanceState)
        mContext = this.baseContext
        LogUtils.d(TAG, javaClass.simpleName + " onCreate")
        val inflater = LayoutInflater.from(this.applicationContext)
        val viewGroup = inflater.inflate(R.layout.base_activity, null, false);
        mViewTitle = viewGroup.findViewById(R.id.title_bar)
        mProgress = viewGroup.findViewById(R.id.progress)
        mContentFrame = viewGroup.findViewById(R.id.frame_content)
        binding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(this.applicationContext), getContentViewId(), mContentFrame, true)
        showPage(configLoadingPage())
        setContentView(viewGroup)

        initData()
        initBinding()
        initView()
        initListener()
        when (configLoadingPage()) {
            true -> viewGroup.postDelayed(Runnable {
                showPage(false)
            }, 3000)
        }

    }

    protected open fun configLoadingPage() = true

    fun showPage(loadingPage: Boolean) {
        mProgress?.visibility = if (loadingPage) View.VISIBLE else View.GONE
        mContentFrame?.visibility = if (loadingPage) View.GONE else View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        QMUIStatusBarHelper.translucent(this, Color.TRANSPARENT)
        QMUIStatusBarHelper.setStatusBarLightMode(this)
        LogUtils.d(TAG, javaClass.simpleName + " onResume")
    }

    override fun onStart() {
        super.onStart()
        LogUtils.d(TAG, javaClass.simpleName + " onStart")
    }

    override fun onPause() {
        super.onPause()
        LogUtils.d(TAG, javaClass.simpleName + " onPause")
    }

    override fun onStop() {
        super.onStop()
        LogUtils.d(TAG, javaClass.simpleName + " onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtils.d(TAG, javaClass.simpleName + " onDestroy")
    }

    protected open fun initBinding(): Unit {

    }

    protected open fun initView(): Unit {
        mViewTitle!!.immersiveTitle(true)
    }

    protected fun setTitle(title: String) {
        mViewTitle!!.setTitle(title)
    }

    protected open fun onBackClick() {
        onBackPressed()
    }

    protected open fun initData(): Unit {}

    protected open fun initListener() {
        mViewTitle!!.initListener(object : EventHandler(this.baseContext, false) {
            override fun onClickView(view: View?) {
                super.onClickView(view)
                onBackClick()
            }
        })
    }

    protected open fun getContentViewId(): Int = 0;

    fun closeTitleBar(close: Boolean) {
        mViewTitle!!.visibility = if (close) View.GONE else View.VISIBLE
    }

}
