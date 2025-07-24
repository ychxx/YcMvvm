package com.yc.ycmvvm.ui

import android.content.Context
import android.os.Bundle
import com.yc.ycmvvm.adapter.YcRecyclerViewAdapter
import com.yc.ycmvvm.base.YcBaseActivity
import com.yc.ycmvvm.config.YcInit
import com.yc.ycmvvm.databinding.MainAcBinding
import com.yc.ycmvvm.databinding.MainItemBinding
import com.yc.ycmvvm.extension.ycInitLinearLayoutManage
import com.yc.ycmvvm.extension.ycLogE
import com.yc.ycmvvm.extension.ycShowToast
import com.yc.ycmvvm.extension.ycToNoEmpty
import com.yc.ycmvvm.net.download.YcDownload
import com.yc.ycmvvm.utils.YcInstallUtil
import com.yc.ycmvvm.utils.YcPhoneUtils.getDeviceUniqueId
import com.yc.ycmvvm.utils.permission.XXPermissionUtil
import com.yc.ycmvvm.utils.permission.YcPermissionUtil

class MainActivity : YcBaseActivity<MainAcBinding>(MainAcBinding::inflate) {

    private val adapter by lazy {
        YcRecyclerViewAdapter<ToAc, MainItemBinding>(MainItemBinding::inflate).apply {
            mOnUpdate = { data ->
                tv.text = data.name
            }
        }
    }

    sealed class ToAc(val name: String, val to: (context: Context) -> Unit) {
        data object InputBox : ToAc("测试输入框", { InputBoxAc.toAc(it) })
        data object TestCameraX : ToAc("测试CameraX", { TestCameraXAc.toAc(it) })
        data object TestVideoX : ToAc("测试视频播放", { TestVideoXAc.toAc(it) })
        data object DownLoadApk : ToAc("测试安装下载apk", { })
        data object TestPickerView : ToAc("测试选择器", { TestPickerViewAc.toAc(it) })
        data object TestSpecialView : ToAc("测试替换布局", { TestSpecialViewAc.toAc(it) })
        data object TestSpinner : ToAc("测试下拉框", { TestSpinnerAc.toAc(it) })
    }

    private lateinit var mYcInstallUtil: YcInstallUtil
    override fun MainAcBinding.initView(savedInstanceState: Bundle?) {
        rv.ycInitLinearLayoutManage()
        mYcInstallUtil = YcInstallUtil(this@MainActivity)
        mYcInstallUtil.mFailCall = {
            ycShowToast("安装失败")
        }
        rv.adapter = adapter
        adapter.addAllData(
            listOf(
                ToAc.InputBox,
                ToAc.TestPickerView,
                ToAc.TestCameraX,
                ToAc.TestVideoX,
                ToAc.DownLoadApk,
                ToAc.TestPickerView,
                ToAc.TestSpecialView,
                ToAc.TestSpinner
            )
        )
        adapter.mItemClick = {
            ycLogE("设备唯一id：${getDeviceUniqueId()}")
            when (it) {
                ToAc.DownLoadApk -> {
                    val url = "https://pub-parking.zrzkwlw.com/2025-04-22/5676d59b9a1158d63e591969fec90f01.apk"
                    YcDownload.createDownloadApkHasProgress(this@MainActivity, url, YcInit.mInstance.mDefaultSaveDirPath + "test.apk", {
                        mYcInstallUtil.mApkFile = it
                        mYcInstallUtil.installApk()
                    }, { ycShowToast(it.ycToNoEmpty("下载apk失败")) }).start()
                }

                else -> it.to(this@MainActivity)
            }
        }

    }
}
