package com.gold.footimpression.net.download;

public interface DownloadListener {
    void onDownloadStart();
    void onDownloadProgress(long progress, long totalSize);
    void onDownloadFaild(int code);
    void onDownloadComplate();
}
