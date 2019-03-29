package com.gold.footimpression.net.download;

import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;

/**
 * 拦截器，可以拦截请求监听下载进度
 */
public class DownloadInterceptor implements Interceptor {
    final String TAG = this.getClass().getSimpleName();

    private DownloadListener mDownloadListener;

    public DownloadInterceptor(DownloadListener downloadListener) {
        this.mDownloadListener = downloadListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .body(new DownloadResponseBody(originalResponse, mDownloadListener))
                .build();
    }
}
