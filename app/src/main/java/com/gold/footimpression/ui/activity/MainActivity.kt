package com.gold.footimpression.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.gold.footimpression.R
import com.gold.footimpression.databinding.ActivityMainBinding
import com.gold.footimpression.module.OrderModule
import com.gold.footimpression.module.ServiceItemModule
import com.gold.footimpression.net.utils.LogUtils
import com.gold.footimpression.ui.base.BaseActivity
import com.gold.footimpression.ui.fragment.*
import com.gold.footimpression.utils.Utils

class MainActivity : BaseActivity() {

    override fun getContentViewId() = R.layout.activity_main


    private var mCurrentFragment: Fragment? = null;
    private lateinit var mMainFrameLayout: FrameLayout;
    private lateinit var mFragmentManager: FragmentManager;

    private val TAG_ORDER_INPUT_FRAGMENT = "ORDER_INPUT_FRAGMENT";
    private val TAG_ORDER_PREVIEW_FRAGMENT = "ORDER_PREVIEW_FRAGMENT";
    private val TAG_ROOM_STATE_FRAGMENT = "ROOM_STATE_FRAGMENT";
    private val TAG_SETTING_FRAGMENT = "SETTING_FRAGMENT";
    private val TAG_SERVICE_ITEMS = "SERVICE_ITEMS_FRAGMENT";
    private val TAG_SERVICE_EDIT_ITEMS = "SERVICE_EDIT_ITEMS";


    private lateinit var mMainActivityBinding: ActivityMainBinding
    var services = mutableListOf<ServiceItemModule>()

    var mOrderModule = OrderModule()
    //是团购
    var group = ObservableField<Boolean>(false)
    //是优惠券
    var coupon = ObservableField<Boolean>(false)

    override fun onBackPressed() {

        if (mCurrentFragment is OrderInputFragment) {
            super.onBackPressed()
        } else if (mCurrentFragment is ServiceItemsFragment) {
            showFragment("ORDER_INPUT_FRAGMENT")
        } else if (mCurrentFragment is ServiceAllItemsEditFragment) {
            showFragment("SERVICE_ITEMS_FRAGMENT")
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setAdjust()
        super.onCreate(savedInstanceState)
        showFragment(TAG_ROOM_STATE_FRAGMENT);
    }

    override fun initBinding() {
        mMainActivityBinding = binding as ActivityMainBinding;

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
    }

    override fun initListener() {
        super.initListener()
        mMainActivityBinding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_1 -> {
                    Utils.closeSoftKeyBord(mContext!!, this)
                    showFragment(TAG_ORDER_INPUT_FRAGMENT);
                }
                R.id.rb_2 -> {
                    LogUtils.i(TAG, " rb2 selected")
                    Utils.closeSoftKeyBord(mContext!!, this)
                    var data = Bundle()
                    if (reiceveData != null) {
                        data = reiceveData!!
                    } else {
                        data.putString("fromKey", "mainActivity")
                    }
                    clearUnsuefulFragment()

                    showFragment(TAG_ORDER_PREVIEW_FRAGMENT, data);
                    reiceveData = null
                }
                R.id.rb_3 -> {
                    clearUnsuefulFragment()
                    Utils.closeSoftKeyBord(mContext!!, this)
                    showFragment(TAG_ROOM_STATE_FRAGMENT);
                }
                R.id.rb_4 -> {
                    clearUnsuefulFragment()
                    Utils.closeSoftKeyBord(mContext!!, this)
                    showFragment(TAG_SETTING_FRAGMENT);
                }
            }
        }
    }

    private fun clearUnsuefulFragment() {
        if (null != mFragmentManager.findFragmentByTag(TAG_ORDER_INPUT_FRAGMENT)) {
            mFragmentManager.beginTransaction()
                .remove(mFragmentManager.findFragmentByTag(TAG_ORDER_INPUT_FRAGMENT)!!).commit()
        }

        if (null != mFragmentManager.findFragmentByTag(TAG_SERVICE_ITEMS)) {
            mFragmentManager.beginTransaction()
                .remove(mFragmentManager.findFragmentByTag(TAG_SERVICE_ITEMS)!!).commit()
        }

        if (null != mFragmentManager.findFragmentByTag(TAG_SERVICE_EDIT_ITEMS)) {
            mFragmentManager.beginTransaction()
                .remove(mFragmentManager.findFragmentByTag(TAG_SERVICE_EDIT_ITEMS)!!).commit()
        }
    }


    var mData: Bundle? = null

    private var reiceveData: Bundle? = null

    fun rb2Select(data: Bundle = Bundle()) {
        reiceveData = data
        mMainActivityBinding!!.rb2.isChecked = true
    }

    fun showFragment(tag: String?, data: Bundle = Bundle()): Unit {

        mData = data
        if (mCurrentFragment != null) {
            getSupportFragmentManager().beginTransaction().hide(mCurrentFragment!!).commit();
        }
        mCurrentFragment = mFragmentManager.findFragmentByTag(tag);
        if (mCurrentFragment == null) {
            when (tag) {
                TAG_ORDER_INPUT_FRAGMENT -> {
                    mCurrentFragment = OrderInputFragment();
                }

                TAG_ROOM_STATE_FRAGMENT -> {
                    mCurrentFragment = RoomStateFragment();
                }

                TAG_ORDER_PREVIEW_FRAGMENT ->
                    mCurrentFragment = OrderPreviewFragment();
                TAG_SETTING_FRAGMENT ->
                    mCurrentFragment = SettingFragment();
                TAG_SERVICE_ITEMS -> {
                    mCurrentFragment = ServiceItemsFragment()
                }
                TAG_SERVICE_EDIT_ITEMS -> {
                    mCurrentFragment = ServiceItemsEditFragment()
                }
            }
            if (mCurrentFragment is ServiceItemsFragment) {
                if (data.keySet().size > 0) {
                    (mCurrentFragment as ServiceItemsFragment).arguments = data
                }
            } else if (mCurrentFragment is ServiceItemsEditFragment) {
                if (data.keySet().size > 0) {
                    (mCurrentFragment as ServiceItemsEditFragment).arguments = data

                }
            } else if (mCurrentFragment is OrderPreviewFragment) {
                if (data.keySet().size > 0) {
                    (mCurrentFragment as OrderPreviewFragment).arguments = data


                }

            }
            mFragmentManager.beginTransaction().add(mMainFrameLayout.id, this.mCurrentFragment!!, tag).commit();
        } else {
            if (mCurrentFragment is ServiceItemsFragment) {
                if (data.keySet().size > 0) {
                    (mCurrentFragment as ServiceItemsFragment).arguments = data
                }
            } else if (mCurrentFragment is ServiceItemsEditFragment) {
                if (data.keySet().size > 0) {
                    (mCurrentFragment as ServiceItemsEditFragment).arguments = data

                }
            } else if (mCurrentFragment is OrderPreviewFragment) {
                if (data.keySet().size > 0) {
                    (mCurrentFragment as OrderPreviewFragment).arguments = data

                }

            }
        }
        mFragmentManager.beginTransaction().show(mCurrentFragment!!).commit();
    }

    override fun configLoadingPage(): Boolean = false
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        LogUtils.i(TAG, "mainactivity on result $resultCode")
    }
}
