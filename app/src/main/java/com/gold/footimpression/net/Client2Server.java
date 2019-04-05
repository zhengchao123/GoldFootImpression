package com.gold.footimpression.net;

import android.text.TextUtils;
import com.gold.footimpression.application.GoldFootApplication;
import com.gold.footimpression.net.download.DownLoadCallBack;
import com.gold.footimpression.net.download.DownloadListener;
import com.gold.footimpression.utils.Utils;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class Client2Server {

    private static final String TAG = "Client2Server";

    public static void doGetAsyn(Map<String, String> params) throws Exception {
        doGetAsyn(params, null, "");
    }

    public static void doGetAsyn(Map<String, String> params, String url) throws Exception {
        doGetAsyn(params, null, url);
    }

    public static void doGetAsyn(Map<String, String> params, Callback callback, String url) throws Exception {
        doGetAsyn(params, callback, null, url);
    }

    public static void doGetAsyn(Map<String, String> params, Callback callback, Type type, String url) throws Exception {
        doGetAsyn(params, callback, type, null, url);
    }


    public static void doGetAsyn(Map<String, String> params, Callback callback, Type type, String flag, String url) throws Exception {
        OkHttpRequest okHttpRequest = new OkHttpRequest.Builder()
                .type(OkHttpRequest.TYPE_GET)
                .params(params)
                .headParams(getHeadStringMap())
                .callBack(callback)
                .classType(type)
                .requestFlag(flag)
                .url(url)
                .build();
        OkHttpClient okHttpClient = HttpClientManager.getInstance(GoldFootApplication.getInstance().mContext).getOkHttpInstance();
        HttpManager.getmInstance().asynchronousHttp(okHttpRequest, okHttpClient);
    }

    private static Map<String, String> getHeadStringMap() {
        Map<String, String> headParams = new HashMap<>();
        String token = Utils.INSTANCE.getUserToken();
        if (!TextUtils.isEmpty(token)) {
            headParams.put("token", token);
        }
        return headParams;
    }

    public static void doPostAsyn(Map<String, String> params) throws Exception {
        doPostAsyn(params, "");
    }

    public static void doPostAsyn(Map<String, String> params, String url) throws Exception {
        doPostAsyn(params, null, url);
    }


    public static void doPostAsyn(Map<String, String> params, Callback callback, String url) throws Exception {
        doPostAsyn(params, callback, null, url);
    }

    public static void doPostAsyn(Map<String, String> params, Callback callback, Type type, String url) throws Exception {
        doPostAsyn(params, callback, type, null, url);
    }

    public static void doPostAsyn(Map<String, String> params, Callback callback, Type type, String flag, String url) throws Exception {
        OkHttpRequest okHttpRequest = new OkHttpRequest.Builder()
                .type(OkHttpRequest.TYPE_POST)
                .headParams(getHeadStringMap())
                .params(params)
                .callBack(callback)
                .classType(type)
                .url(url)
                .requestFlag(flag)
                .build();
        OkHttpClient okHttpClient = HttpClientManager.getInstance(GoldFootApplication.getInstance().mContext).getOkHttpInstance();
        try {
            HttpManager.getmInstance().asynchronousHttp(okHttpRequest, okHttpClient);
        } catch (Exception e) {
        }

    }

    //上传图片专用
    public static void doPostAsyn(Map<String, String> params, String filePath, Callback callback, Type type, String flag, String url) throws Exception {
        OkHttpRequest okHttpRequest = new OkHttpRequest.Builder()
                .type(OkHttpRequest.TYPE_POST)
                .params(params)
                .callBack(callback)
                .classType(type)
                .url(url)
                .requestFlag(flag)
                .filePath(filePath)
                .build();
        OkHttpClient okHttpClient = HttpClientManager.getInstance(GoldFootApplication.getInstance().mContext).getOkHttpInstance();
        HttpManager.getmInstance().asynchronousHttp(okHttpRequest, okHttpClient);
    }

    public static <T extends DownLoadCallBack> void download(Map<String, String> params, T callback) throws Exception {
        download(params, callback, null);
    }

    public static <T extends DownLoadCallBack> void download(Map<String, String> params, T callback, DownloadListener listener) throws Exception {
        OkHttpRequest okHttpRequest = new OkHttpRequest.Builder()
                .params(params)
                .callBack(callback)
                .url(Constants.INSTANCE.getDownloaadUrl())
                .build();
        OkHttpClient okHttpClient = HttpClientManager.getInstance(GoldFootApplication.getInstance().mContext).getDownLoadOkHttpInstance(listener);
        HttpManager.getmInstance().asynchronousHttp(okHttpRequest, okHttpClient);
    }


}
