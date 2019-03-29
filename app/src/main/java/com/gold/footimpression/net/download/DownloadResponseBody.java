package com.gold.footimpression.net.download;

import com.gold.footimpression.net.utils.LogUtils;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.*;

import java.io.IOException;

public class DownloadResponseBody extends ResponseBody {

    private final String TAG = "OKHTTP " + this.getClass().getSimpleName();
    private Response originalResponse;
    private DownloadListener downloadListener;
    private long oldPoint = 0;
    private BufferedSource bufferedSource;

    public DownloadResponseBody(Response originalResponse, DownloadListener downloadListener) {
        this.originalResponse = originalResponse;
        this.downloadListener = downloadListener;
    }

    @Override
    public MediaType contentType() {
        return originalResponse.body().contentType();
    }

    @Override
    public long contentLength() {
        return originalResponse.body().contentLength();
    }

    @Override
    public BufferedSource source() {
        return Okio.buffer(new ForwardingSource(originalResponse.body().source()) {
            private long bytesReaded = 0;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                bytesReaded += bytesRead == -1 ? 0 : bytesRead;
                LogUtils.INSTANCE.i(TAG, (bytesReaded + oldPoint) + "/" + contentLength());
                if (downloadListener != null) {
                    downloadListener.onDownloadProgress(bytesReaded + oldPoint, contentLength());
                }
                return bytesRead;
            }
        });


//        if (bufferedSource == null) {
//            bufferedSource = Okio.buffer(source(originalResponse.body().source()));
//        }
//        return bufferedSource;

    }

    private Source source(final Source source) {
        return new ForwardingSource(source) {
            long totalBytes = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytes += bytesRead != -1 ? bytesRead : 0;
//                if (null != progressListener) {
//                    progressListener.update(totalBytes, bytesRead == -1);
//                }
                return bytesRead;
            }
        };
    }

}
