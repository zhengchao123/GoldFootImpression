package com.gold.footimpression.net;

import android.content.Context;
import com.gold.footimpression.net.download.DownloadInterceptor;
import com.gold.footimpression.net.download.DownloadListener;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public class HttpClientManager {

    private final int TIME_OUT = 10000;
    private OkHttpClient mOkHttpClient;

    private static HttpClientManager mInstance;

    public synchronized static HttpClientManager getInstance(Context context) {
        synchronized (HttpClientManager.class) {
            if (null == mInstance) {
                mInstance = new HttpClientManager();
            }
        }
        return mInstance;
    }

    /**
     * 获取下载的okhttpclient
     * @param downloadListener
     * @return OkHttpClient
     */
    public OkHttpClient getDownLoadOkHttpInstance(DownloadListener downloadListener) {
        if (null == mOkHttpClient) {
            mOkHttpClient = newInstance();
        }
//        mOkHttpClient.newBuilder().addNetworkInterceptor(new DownloadInterceptor(downloadListener)).build();
        return mOkHttpClient.newBuilder().addNetworkInterceptor(new DownloadInterceptor(downloadListener)).build();
    }

    /**
     * 获取 okhttpclient
     * @return OkHttpClient
     */
    public OkHttpClient getOkHttpInstance() {
        if (null == mOkHttpClient) {
            mOkHttpClient = newInstance();
        }
        return mOkHttpClient;
    }

    private synchronized OkHttpClient newInstance() {
        OkHttpClient client = new OkHttpClient
                .Builder()
                .addInterceptor(new LoggingInterceptor())
                .connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                .build();
        return client;
    }
}
