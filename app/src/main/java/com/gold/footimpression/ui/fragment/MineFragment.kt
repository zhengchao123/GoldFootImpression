package com.gold.footimpression.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gold.footimpression.R
import com.gold.footimpression.ui.base.BaseFragment

class MineFragment : BaseFragment() {

    override fun getContentview() = R.layout.mine_fragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = super.onCreateView(inflater, container, savedInstanceState)

        return view
    }
}
