package com.gold.footimpression.net;

import com.gold.footimpression.net.utils.LogUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * 拦截器，可以拦截请求打印日志
 */
public class LoggingInterceptor implements Interceptor {
    final String TAG = "OKHTTP "+this.getClass().getSimpleName();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long startTime = System.nanoTime();
        LogUtils.INSTANCE.d(TAG, String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));
        Response response = chain.proceed(request);
        long endTime = System.nanoTime();
        LogUtils.INSTANCE.d(TAG, String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (endTime - startTime) / 1e6d, response.headers()));


        return response;

    }
}
