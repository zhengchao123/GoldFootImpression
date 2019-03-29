package com.gold.footimpression.net;

import com.gold.footimpression.net.utils.LogUtils;
import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;

public class HttpCallBack implements Callback {
    protected OkHttpRequest mOkhttpRequest;


    public static int SUCCESS_CODE_NO_DATA = 1002;
    public static int SUCCESS_CODE = 1001;
    public static int FAILE_CODE = 1100;
    public static int FAILE_CODE_TIME_OUT = 1101;
    public static int FAILE_CODE_TOKEN_DATED = 401;//token过期
    public static int FAILE_PICTRUE_GET = 402;//获取图片错误


    public HttpCallBack() {
    }

    public HttpCallBack(OkHttpRequest request) {
        this.mOkhttpRequest = request;
    }

    private final String TAG = "OKHTTP " + this.getClass().getSimpleName();

    public void setRequest(OkHttpRequest okHttpRequest) {
        this.mOkhttpRequest = okHttpRequest;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        String errorMsg = call.request().url().toString();
        errorMsg += " request  faild: " + e.getMessage();
        if (null != mOkhttpRequest) {
            Map<String, String> params = mOkhttpRequest.getmParams();
            Gson gson = new Gson();
            if (null != params) {
                errorMsg += " \n with params :" + gson.toJson(params);
            }
        }
        LogUtils.INSTANCE.e(TAG, errorMsg);
        HttpManager.getmInstance().cancleCallByKey(mOkhttpRequest.getmRequestFlag());

        if (e.getCause() instanceof SocketTimeoutException) {
            onFailed(FAILE_CODE_TIME_OUT, e.getMessage(), call);
        } else {
            onFailed(FAILE_CODE, e.getMessage(), call);
        }

    }
    public void onFailed(int code, String exceptionMsg, Call call) {
//    e.getCause().equals(SocketTimeoutException.class)
    }
    @Override
    public void onResponse(Call call, Response response) throws IOException {
        Gson gson = new Gson();
        String responseBodyStr = response.body().string();
        LogUtils.INSTANCE.e(TAG, call.request().url()
                + " \n method: " + call.request().method()
                + " \n params: " + (call.request().method().equals("POST") ? gson.toJson(mOkhttpRequest.getmParams()) : "")
                + " \n response code: " + response.code()
                + " \n response body: " + (null == mOkhttpRequest.getmDownloadListener() ? responseBodyStr : ""));
        HttpManager.getmInstance().cancleCallByKey(mOkhttpRequest.getmRequestFlag());
        onResponse(call, response, (null != mOkhttpRequest.getmClassType()) ?
                gson.fromJson(responseBodyStr, mOkhttpRequest.getmClassType()) :
                null);

    }

    public <T> void onResponse(Call call, Response response, T t) {
    }
}
