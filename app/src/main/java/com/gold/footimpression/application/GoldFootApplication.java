package com.gold.footimpression.application;

import android.app.Application;
import android.content.Context;
import com.gold.footimpression.Service.MyIntentService;
import com.gold.footimpression.Service.MyPushService;
import com.igexin.sdk.PushManager;
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
// com.getui.demo.DemoPushService 为第三方自定义推送服务
        PushManager.getInstance().initialize(getApplicationContext(), MyPushService.class);
        PushManager.getInstance().registerPushIntentService(getApplicationContext(), MyIntentService.class);
    }

    private void initHandler() {
        OwnCrashHandler handler = OwnCrashHandler.Companion.getInstance();
        handler.init(mContext);
        Thread.setDefaultUncaughtExceptionHandler(handler);
    }

}
