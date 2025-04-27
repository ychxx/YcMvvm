package com.yc.ycmvvm.ui

import android.os.Bundle
import com.yc.ycmvvm.adapter.YcRecyclerViewAdapter
import com.yc.ycmvvm.base.YcBaseActivity
import com.yc.ycmvvm.config.YcInit
import com.yc.ycmvvm.databinding.MainAcBinding
import com.yc.ycmvvm.databinding.MainItemBinding
import com.yc.ycmvvm.extension.ycInitLinearLayoutManage
import com.yc.ycmvvm.extension.ycShowToast
import com.yc.ycmvvm.extension.ycToNoEmpty
import com.yc.ycmvvm.net.download.YcDownload
import com.yc.ycmvvm.utils.YcInstallUtil

class MainActivity : YcBaseActivity<MainAcBinding>(MainAcBinding::inflate) {

    private val adapter by lazy {
        YcRecyclerViewAdapter<String, MainItemBinding>(MainItemBinding::inflate).apply {
            mOnUpdate = { data ->
                tv.text = data
            }
        }
    }

    private lateinit var mYcInstallUtil: YcInstallUtil
    override fun MainAcBinding.initView(savedInstanceState: Bundle?) {
        rv.ycInitLinearLayoutManage()
        mYcInstallUtil = YcInstallUtil(this@MainActivity)
        mYcInstallUtil.mFailCall = {
            ycShowToast("安装失败")
        }
        rv.adapter = adapter
        adapter.addData("测试输入框")
        adapter.addData("测试CameraX")
        adapter.addData("测试视频播放")
        adapter.addData("测试安装下载apk")
        adapter.addData("测试选择器")
        adapter.mItemClick = {
            when (it) {
                "测试输入框" -> {
                    InputBoxAc.toAc(this@MainActivity)
                }

                "测试CameraX" -> {
                    TestCameraXAc.toAc(this@MainActivity)
                }

                "测试视频播放" -> {
                    TestVideoXAc.toAc(this@MainActivity)
                }

                "测试安装下载apk" -> {
                    val url = "https://pub-parking.zrzkwlw.com/2025-04-22/5676d59b9a1158d63e591969fec90f01.apk"
                    YcDownload.createDownloadApkHasProgress(this@MainActivity, url, YcInit.mInstance.mDefaultSaveDirPath + "test.apk", {
                        mYcInstallUtil.mApkFile = it
                        mYcInstallUtil.installApk()
                    }, { ycShowToast(it.ycToNoEmpty("下载apk失败")) }).start()
                }

                "测试选择器" -> {
                    TestPickerViewAc.toAc(this@MainActivity)
                }

            }
        }
    }
}
