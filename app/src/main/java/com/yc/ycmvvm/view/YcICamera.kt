package com.yc.ycmvvm.view

import java.io.File

interface YcICamera {
    /**
     * 预览配置
     */
    fun initPreViewConfig()

    /**
     * 拍照配置
     */
    fun initCaptureConfig()

    /**
     * 录像配置
     */
    fun initRecordConfig()

    /**
     * 实时图片帧配置
     */
    fun initAnalyzerConfig()

    /**
     * 拍照
     */
//    suspend fun startImageCapture(): File?

    /**
     * 录像
     */
    suspend fun startRecord()
}