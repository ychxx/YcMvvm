package com.yc.ycmvvm.ui

import android.content.ContentProviderOperation.newCall
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.webkit.WebSettings
import androidx.lifecycle.lifecycleScope
import com.yc.ycmvvm.App
import com.yc.ycmvvm.adapter.YcRecyclerViewAdapter
import com.yc.ycmvvm.base.YcBaseActivity
import com.yc.ycmvvm.config.YcInit
import com.yc.ycmvvm.databinding.MainAcBinding
import com.yc.ycmvvm.databinding.MainItemBinding
import com.yc.ycmvvm.exception.YcException
import com.yc.ycmvvm.extension.ycInitLinearLayoutManage
import com.yc.ycmvvm.extension.ycLogDSimple
import com.yc.ycmvvm.extension.ycLogE
import com.yc.ycmvvm.extension.ycShowToast
import com.yc.ycmvvm.extension.ycToNoEmpty
import com.yc.ycmvvm.file.YcFileUtils
import com.yc.ycmvvm.file.YcOpenFileUtils
import com.yc.ycmvvm.net.download.YcDownload
import com.yc.ycmvvm.utils.YcInstallUtil
import com.yc.ycmvvm.utils.YcPhoneUtils.getDeviceUniqueId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.xutils.common.Callback
import org.xutils.http.RequestParams
import org.xutils.x
import org.xutils.x.app
import java.io.File


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
        data object TestCamera2 : ToAc("测试Camera2", { TestCamera2Ac.toAc(it) })
        data object TestVideoX : ToAc("测试视频播放", { TestVideoXAc.toAc(it) })
        data object DownLoadApk : ToAc("测试安装下载apk", { })
        data object TestPickerView : ToAc("测试选择器", { TestPickerViewAc.toAc(it) })
        data object TestSpecialView : ToAc("测试替换布局", { TestSpecialViewAc.toAc(it) })
        data object TestSpinner : ToAc("测试下拉框", { TestSpinnerAc.toAc(it) })
        data object TestNet : ToAc("测试网络接口", { TestNetAc.toAc(it) })
        data object Test : ToAc("测试", { TestAc.toAc(it) })
        data object TestQr : ToAc("二维码", { TestQrAc.toAc(it) })
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
                ToAc.TestCamera2,
                ToAc.TestVideoX,
                ToAc.DownLoadApk,
                ToAc.TestPickerView,
                ToAc.TestSpecialView,
                ToAc.TestSpinner,
                ToAc.TestNet,
                ToAc.Test,
                ToAc.TestQr
            )
        )
        adapter.mItemClick = {
            ycLogE("设备唯一id：${getDeviceUniqueId()}")
            when (it) {
                ToAc.DownLoadApk -> {
                    lifecycleScope.launch(Dispatchers.IO) {

                    }
                }

                else -> it.to(this@MainActivity)
            }
        }

    }
}
