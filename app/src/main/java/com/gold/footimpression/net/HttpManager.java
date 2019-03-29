package com.gold.footimpression.net;

import com.gold.footimpression.net.utils.LogUtils;
import okhttp3.Call;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpManager {

    private final String TAG = this.getClass().getSimpleName();
    private static HttpManager mInstance;
    private HashMap<String, Call> mCallHasMap = new HashMap<>();

    public static HttpManager getmInstance() {
        if (null == mInstance) {
            mInstance = new HttpManager();
        }
        return mInstance;
    }

    /**
     * 异步http请求
     *
     * @param request
     * @param okHttpClient
     */
    public void asynchronousHttp(OkHttpRequest request, OkHttpClient okHttpClient) throws Exception{
        if( null == request ){
            throw new Exception("request can not be null");
        }
        Call call = okHttpClient.newCall(request.getmRequest());
        if (null != mCallHasMap && mCallHasMap.containsKey(request.getmRequestFlag())) {
            LogUtils.INSTANCE.e(TAG, "requests contains a same flag request,please rename a flag ");
            throw new Exception("requests contains a same flag request,please rename a flag ");
        } else {
            mCallHasMap.put(request.getmRequestFlag(), call);
        }
        call.enqueue(request.getmCallBack());
    }

    /**
     * 同步http请求
     *
     * @param request
     * @param okHttpClient
     */
    public void synchronousHttp(OkHttpRequest request, OkHttpClient okHttpClient) throws Exception {
        Call call = okHttpClient.newCall(request.getmRequest());
        if (null != mCallHasMap && mCallHasMap.containsKey(request.getmRequestFlag())) {
            LogUtils.INSTANCE.e(TAG, "requests contains a same flag request,please remane a flag ");
            throw new Exception("requests contains a same flag request,please rename a flag ");
        } else {
            mCallHasMap.put(request.getmRequestFlag(), call);
        }
        try {
            call.execute();
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.INSTANCE.e(TAG, e.getMessage());
        }
    }

//    /**
//     * 下载
//     * @param request
//     * @param okHttpClient
//     */
//    public void download(OkHttpRequest request,OkHttpClient okHttpClient){
//
//    }

    /**
     * cancle a http request by key
     * @param key
     */
    public void cancleCallByKey(String key) {
        if (mCallHasMap.containsKey(key)) {
            Call call = mCallHasMap.get(key);
            if (call.isExecuted() && !call.isCanceled()) {
                call.cancel();
            }
            mCallHasMap.remove(key);
        }
    }

    /**
     * cancle all http request
     */
    public void cancleAllCall() {
        for (Map.Entry<String, Call> entry : mCallHasMap.entrySet()) {
            cancleCallByKey(entry.getKey());
        }

    }
}
