package com.yc.ycmvvm.net.download

/**
 * 下载状态
 */

sealed class YcDownLoadState {
    /**
     * 还未开始
     */
    data object DownloadNotStarted : YcDownLoadState()

    /**
     * 下载中
     */
    data object Downloading : YcDownLoadState()

    /**
     * 下载失败
     */
    data object DownloadFail : YcDownLoadState()

    /**
     * 下载成功
     */
    data object DownloadSuccess : YcDownLoadState()
}