package com.gold.footimpression.ui.activity

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.gold.footimpression.R
import com.gold.footimpression.databinding.ActivityMainBinding
import com.gold.footimpression.ui.base.BaseActivity
import com.gold.footimpression.ui.fragment.AssistantFragment
import com.gold.footimpression.ui.fragment.LifeFragment
import com.gold.footimpression.ui.fragment.MainFragment
import com.gold.footimpression.ui.fragment.MineFragment

class MainActivity : BaseActivity() {

    override fun getContentViewId() = R.layout.activity_main


    private var mCurrentFragment: Fragment? = null;
    private lateinit var mMainFrameLayout: FrameLayout;
    private lateinit var mFragmentManager: FragmentManager;

    private val TAG_MAIN_FRAGMENT = "MAIN_FRAGMENT";
    private val TAG_LIFE_FRAGMENT = "LIFE_FRAGMENT";
    private val TAG_ASSISTANT_FRAGMENT = "ASSISTANT_FRAGMENT";
    private val TAG_MINE_FRAGMENT = "MINE_FRAGMENT";


    private lateinit var mMainActivityBinding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showFragment(TAG_MAIN_FRAGMENT);
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
                    Toast.makeText(mContext, " rb_1 checked", Toast.LENGTH_LONG).show()
                    showFragment(TAG_MAIN_FRAGMENT);
                }
                R.id.rb_2 -> {
                    Toast.makeText(mContext, " rb_2 checked", Toast.LENGTH_LONG).show()
                    showFragment(TAG_ASSISTANT_FRAGMENT);
                }
                R.id.rb_3 -> {
                    Toast.makeText(mContext, " rb_3 checked", Toast.LENGTH_LONG).show()
                    showFragment(TAG_LIFE_FRAGMENT);
                }
                R.id.rb_4 -> {
                    Toast.makeText(mContext, " rb_4 checked", Toast.LENGTH_LONG).show()
                    showFragment(TAG_MINE_FRAGMENT);
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
                TAG_MAIN_FRAGMENT ->
                    mCurrentFragment = MainFragment();
                TAG_ASSISTANT_FRAGMENT ->
                    mCurrentFragment = LifeFragment();
                TAG_LIFE_FRAGMENT ->
                    mCurrentFragment = AssistantFragment();
                TAG_MINE_FRAGMENT ->
                    mCurrentFragment = MineFragment();

            }
            mFragmentManager.beginTransaction().add(mMainFrameLayout.id, this.mCurrentFragment!!, tag).commit();
        } else {
            mFragmentManager.beginTransaction().show(mCurrentFragment!!).commit();
        }
    }



}
