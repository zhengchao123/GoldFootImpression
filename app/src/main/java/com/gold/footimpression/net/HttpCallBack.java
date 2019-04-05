package com.gold.footimpression.net;

import android.content.Intent;
import com.gold.footimpression.application.GoldFootApplication;
import com.gold.footimpression.net.utils.LogUtils;
import com.gold.footimpression.ui.activity.LoginActivity;
import com.gold.footimpression.utils.Utils;
import com.google.gson.Gson;
import com.rcb.financialservice.model.BaseNetArrayModule;
import com.rcb.financialservice.model.BaseNetObjectModule;
import com.rcb.financialservice.model.BaseNetStringModule;
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
    public static int FAILE_CODE_TOKEN_DATED = 23;//token过期
    public static int FAILE_CODE_TOKEN_NULL = 22;//token为空
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
        errorMsg += " request  faild: " + "\n head: " + getHeadStr(call) + "\n errMsg: " + e.getMessage();

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
        LogUtils.INSTANCE.i(TAG, call.request().url()
                + " \n method: " + call.request().method()
                + " \n head: " + getHeadStr(call)
                + " \n params: " + (call.request().method().equals("POST") ? gson.toJson(mOkhttpRequest.getmParams()) : ""));
//        LogUtils.INSTANCE.i(TAG, " \n params: " + (call.request().method().equals("POST") ? gson.toJson(mOkhttpRequest.getmParams()) : ""));
        LogUtils.INSTANCE.i(TAG, " \n response code: " + response.code()
                + " \n response body: " + (null == mOkhttpRequest.getmDownloadListener() ? responseBodyStr : ""));
        HttpManager.getmInstance().cancleCallByKey(mOkhttpRequest.getmRequestFlag());
        try {
            onResponse(call, response, (null != mOkhttpRequest.getmClassType()) ?
                    gson.fromJson(responseBodyStr, mOkhttpRequest.getmClassType()) :
                    null);
        } catch (Exception e) {
            onFailure(call, new IOException(e.getMessage()));
        }


    }

    public <T> void onResponse(Call call, Response response, T t) {
        try {
            BaseNetArrayModule module = t instanceof BaseNetArrayModule ? ((BaseNetArrayModule) t) : null;
            BaseNetStringModule moduleS = t instanceof BaseNetStringModule ? ((BaseNetStringModule) t) : null;
            BaseNetObjectModule moduleO = t instanceof BaseNetObjectModule ? ((BaseNetObjectModule) t) : null;
            Integer code = 0;
            String msg = "";
            if (module != null) {
                code = module.getCode();
                msg = module.getMsg();
            }
            if (moduleS != null) {
                code = moduleS.getCode();
                msg = moduleS.getMsg();
            }
            if (moduleO != null) {
                code = moduleO.getCode();
                msg = moduleO.getMsg();
            }
            if (code == FAILE_CODE_TOKEN_DATED || code == FAILE_CODE_TOKEN_NULL) {
                Utils.INSTANCE.clearUserInfo();
                Intent intent = new Intent(GoldFootApplication.getInstance(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                GoldFootApplication.getInstance().startActivity(intent);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getHeadStr(Call call) {
        String str = "";

        for (int i = 0; i < call.request().headers().names().size(); i++) {
            str = str + " name = " + call.request().headers().name(i) + " value =" + call.request().headers().value(i) + "\n";
        }
        return str;
    }
}
