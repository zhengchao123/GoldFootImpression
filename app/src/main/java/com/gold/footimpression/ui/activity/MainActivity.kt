package com.gold.footimpression.ui.activity

import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.gold.footimpression.R
import com.gold.footimpression.databinding.ActivityMainBinding
import com.gold.footimpression.ui.base.BaseActivity
import com.gold.footimpression.ui.fragment.OrderPreviewFragment
import com.gold.footimpression.ui.fragment.RoomStateFragment
import com.gold.footimpression.ui.fragment.OrderInputFragment
import com.gold.footimpression.ui.fragment.SettingFragment

class MainActivity : BaseActivity() {

    override fun getContentViewId() = R.layout.activity_main


    private var mCurrentFragment: Fragment? = null;
    private lateinit var mMainFrameLayout: FrameLayout;
    private lateinit var mFragmentManager: FragmentManager;

    private val TAG_ORDER_INPUT_FRAGMENT = "ORDER_INPUT_FRAGMENT";
    private val TAG_ORDER_PREVIEW_FRAGMENT = "ORDER_PREVIEW_FRAGMENT";
    private val TAG_ROOM_STATE_FRAGMENT = "ROOM_STATE_FRAGMENT";
    private val TAG_SETTING_FRAGMENT = "SETTING_FRAGMENT";


    private lateinit var mMainActivityBinding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showFragment(TAG_ORDER_INPUT_FRAGMENT);
    }

    override fun initBinding() {
        mMainActivityBinding = binding as ActivityMainBinding;

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
        mMainActivityBinding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_1 -> {
//                    Toast.makeText(mContext, " rb_1 checked", Toast.LENGTH_LONG).show()
                    showFragment(TAG_ORDER_INPUT_FRAGMENT);
                }
                R.id.rb_2 -> {
//                    Toast.makeText(mContext, " rb_2 checked", Toast.LENGTH_LONG).show()
                    showFragment(TAG_ROOM_STATE_FRAGMENT);
                }
                R.id.rb_3 -> {
//                    Toast.makeText(mContext, " rb_3 checked", Toast.LENGTH_LONG).show()
                    showFragment(TAG_ORDER_PREVIEW_FRAGMENT);
                }
                R.id.rb_4 -> {
//                    Toast.makeText(mContext, " rb_4 checked", Toast.LENGTH_LONG).show()
                    showFragment(TAG_SETTING_FRAGMENT);
                }
            }
        }
    }


    fun showFragment(tag: String?): Unit {
        if (mCurrentFragment != null) {
            getSupportFragmentManager().beginTransaction().hide(mCurrentFragment!!).commit();
        }
        mCurrentFragment = mFragmentManager.findFragmentByTag(tag);
        if (mCurrentFragment == null) {
            when (tag) {
                TAG_ORDER_INPUT_FRAGMENT ->
                    mCurrentFragment = OrderInputFragment();
                TAG_ROOM_STATE_FRAGMENT ->
                    mCurrentFragment = RoomStateFragment();
                TAG_ORDER_PREVIEW_FRAGMENT ->
                    mCurrentFragment = OrderPreviewFragment();
                TAG_SETTING_FRAGMENT ->
                    mCurrentFragment = SettingFragment();

            }
            mFragmentManager.beginTransaction().add(mMainFrameLayout.id, this.mCurrentFragment!!, tag).commit();
        } else {
            mFragmentManager.beginTransaction().show(mCurrentFragment!!).commit();
        }
    }

    override fun configLoadingPage(): Boolean = false

}
