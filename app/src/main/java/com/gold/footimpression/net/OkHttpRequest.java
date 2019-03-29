package com.gold.footimpression.net;

import android.text.TextUtils;
import com.gold.footimpression.net.download.DownloadListener;
import com.gold.footimpression.net.utils.LogUtils;
import okhttp3.*;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Map;

public class OkHttpRequest {
    public static int TYPE_POST = 1;
    public static int TYPE_GET = 0;
    private final String TAG = "OKHTTP " + this.getClass().getSimpleName();
    private String mRequestFlag;
    private Request mRequest;
    private Callback mCallBack;
    private String mUrl;
    private int mType;
    private Map<String, String> mHeadParams;
    private Map<String, String> mParams;
    private RequestBody mRequestBody;
    private String mUploadFilePath;
    private String mDownloadFilePath;
    private Type mClassType;

    private DownloadListener mDownloadListener;

    private OkHttpRequest(Builder builder) {
        if (null != builder) {
            this.mRequestFlag = builder.mRequestFlag;
            this.mCallBack = builder.mCallBack;
            this.mUrl = builder.mUrl;
            this.mType = builder.mType;
            this.mParams = builder.mParams;
            this.mRequestBody = builder.mRequestBody;
            this.mUploadFilePath = builder.mUploadFilePath;
            this.mDownloadFilePath = builder.mDownloadFilePath;
            this.mHeadParams = builder.mHeadParams;
            this.mDownloadListener = builder.mDownloadListener;
            this.mClassType = builder.mClassType;
            if (null == mCallBack) {
                mCallBack = new HttpCallBack(this);
            }else {
                ((HttpCallBack)mCallBack).setRequest(this);
            }
            if (TextUtils.isEmpty(mRequestFlag)) {
                mRequestFlag = System.currentTimeMillis() + "";
            }
            initRequest();
        }
    }

    public DownloadListener getmDownloadListener() {
        return mDownloadListener;
    }

    public Callback getmCallBack() {
        return mCallBack;
    }

    public String getmRequestFlag() {
        return mRequestFlag;
    }

    public Request getmRequest() {
        return mRequest;
    }

    public String getmDownloadFilePath() {
        return mDownloadFilePath;
    }

    public String getmUploadFilePath() {
        return mUploadFilePath;
    }

    public Map<String, String> getmParams() {
        return mParams;
    }

    public Type getmClassType() {
        return mClassType;
    }

    public String getmUrl() {
        return this.mUrl;
    }

    public static class Builder {
        private final String TAG = this.getClass().getSimpleName();
        public String mUploadFilePath;
        public Map<String, String> mHeadParams;
        public DownloadListener mDownloadListener;
        public String mDownloadFilePath;
        public Type mClassType;
        private String mRequestFlag;
        private Callback mCallBack;
        private String mUrl;
        private int mType;
        private RequestBody mRequestBody;
        private Map<String, String> mParams;

        /**
         * set the return data type
         *
         * @param type
         * @return
         */
        public Builder classType(Type type) {
            this.mClassType = type;
            return this;
        }

        /**
         * set the downloadfile path
         *
         * @param filePath
         * @return
         */
        public Builder downloadFilePath(String filePath) {
            this.mDownloadFilePath = filePath;
            return this;
        }

        /**
         * set downloadlistener if we are downloading
         *
         * @param downloadListener
         * @return Builder
         */
        public Builder downloadListener(DownloadListener downloadListener) {
            this.mDownloadListener = downloadListener;
            return this;
        }

        /**
         * set headparams
         *
         * @param headParams
         * @return Builder
         */
        public Builder headParams(Map<String, String> headParams) {
            this.mHeadParams = headParams;
            return this;
        }

        /**
         * set the filepath if we use the http upload file
         *
         * @param filePath
         * @return Builder
         */
        public Builder filePath(String filePath) {
            this.mUploadFilePath = filePath;
            return this;
        }

        /**
         * set a Unique value for the request as the key
         *
         * @param requestFlag
         * @return Builder
         */
        public Builder requestFlag(String requestFlag) {
            mRequestFlag = requestFlag;
            return this;
        }

        /**
         * set the http callback
         *
         * @param callback
         * @return Builder
         */
        public Builder callBack(Callback callback) {
            mCallBack = callback;
            return this;
        }

        /**
         * set the http url
         *
         * @param url
         * @return Builder
         */
        public Builder url(String url) {
            this.mUrl = url;
            return this;
        }

        /**
         * set the http type
         *
         * @param type
         * @return Build
         */
        public Builder type(int type) {
            this.mType = type;
            return this;
        }

        /**
         * set the params about http
         *
         * @param params
         * @return
         */
        public Builder params(Map<String, String> params) {
            this.mParams = params;
            return this;
        }

        public OkHttpRequest build() {
            mRequestBody = initRequestBody();
            return new OkHttpRequest(this);
        }

        /**
         * init the requestbody .
         * if mFilepath is null will return RequestBody else return MultipartBody
         *
         * @return RequestBody
         * @throws Exception
         */
        private RequestBody initRequestBody() {
            if (TextUtils.isEmpty(mUploadFilePath)) {
                FormBody.Builder builder = new FormBody.Builder();
                if (mParams != null && !mParams.isEmpty()) {
                    for (Map.Entry<String, String> entry : mParams.entrySet()) {
                        builder.add(entry.getKey(), entry.getValue());
                    }
                }
                return builder.build();
            } else {
                MultipartBody.Builder multiBuilder = new MultipartBody.Builder();
                File file = new File(mUploadFilePath);
                if (!file.exists()) {
                    LogUtils.INSTANCE.e(TAG, "path = " + file.getAbsolutePath() + " is not exist ");
//                    throw new Exception("path = " + file.getAbsolutePath() + " is not exist ");
                }

                if (mParams != null && !mParams.isEmpty()) {
                    //上传参数
                    for (String key : mParams.keySet()) {
                        multiBuilder.addFormDataPart(key, (String) mParams.get(key));
                    }
                }
                multiBuilder.addFormDataPart(file.getName(), file.getPath()
                        , RequestBody.create(MediaType.parse("ScsoApplication/octet-stream"), file))
                        .setType(MultipartBody.FORM);
                return multiBuilder.build();
            }
        }
    }


    private String initUrl() {
        StringBuffer sb = null;
        //当用户传入null或者传了一个空的map
        if (mParams != null && !mParams.isEmpty()) {
            for (Map.Entry<String, String> entry : mParams.entrySet()) {
                if (sb == null) {
                    sb = new StringBuffer("?");
                } else {
                    //拼接好的网站去掉最后一个“&”符号
                    sb.append("&");
                }
                sb.append(entry.getKey() + "=" + entry.getValue());
            }
        }
        if (null != sb && sb.toString() != null) {
            String urlEndStr = sb.toString();
            LogUtils.INSTANCE.d(TAG, "init params String \n" + urlEndStr);
            LogUtils.INSTANCE.d(TAG, "init url = " + (mUrl + urlEndStr));
            return mUrl + urlEndStr;
        }
        LogUtils.INSTANCE.d(TAG, "init url = " + mUrl);
        return mUrl;
    }

    private void initRequest() {
        Request.Builder builder = new Request.Builder();
        if (null != mHeadParams) {
            for (Map.Entry<String, String> entry : mHeadParams.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        builder.addHeader("Accept-Encoding", "identity");
//        builder.addHeader("Transfer-Encoding", "chunked");
        builder.addHeader("Connection", "keep-alive");
        mUrl = TextUtils.isEmpty(mUrl) ? Constants.INSTANCE.getHOST_TEST() : mUrl;
        if (mType == TYPE_GET) {
            builder.url(initUrl()).get();
        } else if (mType == TYPE_POST) {
            builder.url(mUrl).post(mRequestBody);
        }
        mRequest = builder.build();
    }
}