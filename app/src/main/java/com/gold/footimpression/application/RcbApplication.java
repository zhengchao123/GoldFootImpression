package com.gold.footimpression.application;

import android.app.Application;
import android.content.Context;
import decoration.scsowing.com.decoration.ui.event.OwnCrashHandler;


public class RcbApplication extends Application {

    public Context mContext;

    public static com.gold.footimpression.application.RcbApplication mInstance;

    public static com.gold.footimpression.application.RcbApplication getInstance() {
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
