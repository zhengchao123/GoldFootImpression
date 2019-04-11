package com.gold.footimpression.ui.activity

import android.os.Bundle
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.gold.footimpression.R
import com.gold.footimpression.databinding.ActivityMainBinding
import com.gold.footimpression.databinding.ActivityServiceEditFragmentBinding
import com.gold.footimpression.module.OrderModule
import com.gold.footimpression.module.ServiceItemModule
import com.gold.footimpression.ui.base.BaseActivity
import com.gold.footimpression.ui.fragment.*

class EditOrderActivity : BaseActivity() {

    override fun getContentViewId() = R.layout.activity_service_edit_fragment


    private var mCurrentFragment: Fragment? = null;
    private lateinit var mMainFrameLayout: FrameLayout;
    private lateinit var mFragmentManager: FragmentManager;

    private var dingdanUid = ""
    private lateinit var mMainActivityBinding: ActivityServiceEditFragmentBinding
    var services = mutableListOf<ServiceItemModule>()

    var mOrderModule = OrderModule()


    override fun onCreate(savedInstanceState: Bundle?) {
        setAdjust()
        super.onCreate(savedInstanceState)
        showFragment(Bundle().let {
            it.putString("dingdanUid",dingdanUid)
            it
        });
    }


    override fun initBinding() {
        mMainActivityBinding = binding as ActivityServiceEditFragmentBinding;

    }

    fun setAdjust() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    override fun initView() {
        super.initView()
        closeTitleBar(true)
        mMainFrameLayout = mMainActivityBinding.mainFragment;
    }

    override fun initData() {
        super.initData()
        mFragmentManager = supportFragmentManager;

        if (null != intent && intent.hasExtra("dingdanUid")) {
            dingdanUid = intent.getStringExtra("dingdanUid")
        }
    }


    fun showFragment(data: Bundle = Bundle()): Unit {

        mCurrentFragment = ServiceAllItemsEditFragment()
        if (data.keySet().size > 0) {
            (mCurrentFragment as ServiceAllItemsEditFragment).arguments = data
        }
        mFragmentManager.beginTransaction().add(mMainFrameLayout.id, this.mCurrentFragment!!, "fragment").commit();
//
        mFragmentManager.beginTransaction().show(mCurrentFragment!!).commit();
    }

    override fun configLoadingPage(): Boolean = false

}
