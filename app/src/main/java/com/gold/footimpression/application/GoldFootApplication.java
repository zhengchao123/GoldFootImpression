package com.gold.footimpression.application;

import android.app.Application;
import android.content.Context;
import decoration.scsowing.com.decoration.ui.event.OwnCrashHandler;


public class GoldFootApplication extends Application {

    public Context mContext;

    public static GoldFootApplication mInstance;

    public static GoldFootApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        this.mContext = this.getApplicationContext();
        initHandler();

    }

    private void initHandler() {
        OwnCrashHandler handler = OwnCrashHandler.Companion.getInstance();
        handler.init(mContext);
        Thread.setDefaultUncaughtExceptionHandler(handler);
    }

}
