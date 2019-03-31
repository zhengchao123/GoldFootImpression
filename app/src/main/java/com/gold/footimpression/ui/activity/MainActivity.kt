package com.gold.footimpression.ui.activity

import android.os.Bundle
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.gold.footimpression.R
import com.gold.footimpression.databinding.ActivityMainBinding
import com.gold.footimpression.module.ServiceItemModule
import com.gold.footimpression.ui.base.BaseActivity
import com.gold.footimpression.ui.fragment.*

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
    public var services = mutableListOf<ServiceItemModule>()


    //是团购
    public var group = ObservableField<Boolean>(false)
    //是优惠券
    public var coupon = ObservableField<Boolean>(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        setAdjust()
        super.onCreate(savedInstanceState)
        showFragment(TAG_ORDER_INPUT_FRAGMENT);
    }

    override fun initBinding() {
        mMainActivityBinding = binding as ActivityMainBinding;

    }

    fun setAdjust(){
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
                    showFragment(TAG_ORDER_INPUT_FRAGMENT);
                }
                R.id.rb_2 -> {
                    showFragment(TAG_ROOM_STATE_FRAGMENT);
                }
                R.id.rb_3 -> {
                    showFragment(TAG_ORDER_PREVIEW_FRAGMENT);
                }
                R.id.rb_4 -> {
                    showFragment(TAG_SETTING_FRAGMENT);
                }
            }
        }
    }


    public var mData: Bundle? = null

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
            }
        }
        mFragmentManager.beginTransaction().show(mCurrentFragment!!).commit();
    }

    override fun configLoadingPage(): Boolean = false

}
