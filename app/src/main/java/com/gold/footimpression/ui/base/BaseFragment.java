package com.gold.footimpression.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import com.gold.footimpression.net.utils.LogUtils;

public class BaseFragment extends Fragment {

    protected final String TAG = this.getClass().getSimpleName();
    protected Context mContext;

    protected ViewDataBinding dataBinding;

    @Override
    public void onAttach(Context context) {
        LogUtils.INSTANCE.i(TAG, getClassName() + " onAttach");
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        LogUtils.INSTANCE.i(TAG, getClassName() + " onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        LogUtils.INSTANCE.i(TAG, getClassName() + " onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        LogUtils.INSTANCE.i(TAG, getClassName() + " onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        LogUtils.INSTANCE.i(TAG, getClassName() + " onStop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        LogUtils.INSTANCE.i(TAG, getClassName() + " onDestroy");
        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        LogUtils.INSTANCE.i(TAG, getClassName() + "hidden= "+hidden);
        super.onHiddenChanged(hidden);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtils.INSTANCE.i(TAG, TAG + " onCreateView");
        dataBinding = DataBindingUtil.inflate(inflater, getContentview(), container, false);
        initBinding();
        initData();
        initView();
        initListener();
        return dataBinding.getRoot();
    }

    public void initListener() {
    }

    public int getContentview() {
        return 0;
    }

    public void initBinding() {
    }

    public void initData() {
    }

    public void initView() {

    }

    protected void toast(String content) {
        Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
    }

    protected void toast(Integer content) {
        Toast.makeText(mContext, getString(content), Toast.LENGTH_SHORT).show();
    }

    public String getClassName() {
        return this.getClass().getSimpleName();
    }
}
