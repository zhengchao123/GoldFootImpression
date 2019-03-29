package com.gold.footimpression.net.download;

import android.text.TextUtils;
import com.gold.footimpression.application.RcbApplication;
import com.gold.footimpression.net.HttpCallBack;
import com.gold.footimpression.net.OkHttpRequest;
import com.gold.footimpression.net.utils.LogUtils;
import okhttp3.Call;
import okhttp3.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DownLoadCallBack extends HttpCallBack {
    final String TAG = this.getClass().getSimpleName();
    DownloadListener mDownloadListener;

    public DownLoadCallBack() {
    }

    public DownLoadCallBack(OkHttpRequest request) {
        super(request);
        mDownloadListener = request.getmDownloadListener();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        InputStream is = null;//输入流
        FileOutputStream fos = null;//输出流

        try {
            if (null != mDownloadListener) {
                mDownloadListener.onDownloadStart();
            }
            is = response.body().byteStream();//获取输入流
            long total = response.body().contentLength();
            if (is != null) {
                File file;
                if (TextUtils.isEmpty(mOkhttpRequest.getmDownloadFilePath())) {
                    file = new File(RcbApplication.getInstance().mContext.getFilesDir(), "test.jpg");
                } else {
                    file = new File(mOkhttpRequest.getmDownloadFilePath(), getNameFromUrl(call.request().url().toString()));
                }
                double progress = 0;
                fos = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                int ch = -1;
                while ((ch = is.read(buf)) != -1) {
                    fos.write(buf, 0, ch);
                    progress += ch;
                    LogUtils.INSTANCE.i(TAG, "  progress " + progress + " / " + total);
                }

            }
            fos.flush();
            // 下载完成
            if (fos != null) {
                fos.close();
            }
            if (null != mDownloadListener) {
                mDownloadListener.onDownloadComplate();
            }
        } catch (Exception e) {
            if (null != mDownloadListener) {
                mDownloadListener.onDownloadFaild(0);
            }
            e.printStackTrace();
            LogUtils.INSTANCE.e(TAG, e.toString());
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
            }
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
            }
        }
    }


    /**
     * @param url
     * @return 从下载连接中解析出文件名
     */
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

}
