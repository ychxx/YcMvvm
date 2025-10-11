package com.yc.ycmvvm.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.net.Uri
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import com.yc.ycmvvm.base.YcBaseActivity
import com.yc.ycmvvm.databinding.CameraxAcBinding
import com.yc.ycmvvm.extension.ycLoadImageFile
import com.yc.ycmvvm.extension.ycLogE
import com.yc.ycmvvm.extension.ycLogESimple
import com.yc.ycmvvm.extension.ycShowToast
import com.yc.ycmvvm.utils.YcCameraXUtil
import com.yc.ycmvvm.utils.permission.XXPermissionUtil
import com.yc.ycmvvm.utils.permission.YcPermissionUtil
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class TestCameraXAc : YcBaseActivity<CameraxAcBinding>(CameraxAcBinding::inflate) {
    companion object {
        fun toAc(context: Context) {
            context.startActivity(Intent(context, TestCameraXAc::class.java))
        }
    }

    val mPermissionUtil by lazy {
        YcPermissionUtil(this).apply {
            addPermission(XXPermissionUtil.CAMERA, XXPermissionUtil.RECORD_AUDIO, *XXPermissionUtil.STORAGE)
        }
    }

    /** Blocking camera operations are performed using this executor */
    private lateinit var cameraExecutor: ExecutorService
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    @OptIn(ExperimentalGetImage::class)
    override fun CameraxAcBinding.initView(savedInstanceState: Bundle?) {
        cameraExecutor = Executors.newSingleThreadExecutor()
        flashBtn.setOnClickListener {
            cameraPv.openFlash()
        }
        resetBtn.setOnClickListener {
            mViewBinding.resultTv.text = "请对准车牌"
        }
        capBtn.setOnClickListener {
            cameraPv.imageCapture(this@TestCameraXAc, "test.jpg", {
                imgCompress(it)
            }, {
                ycShowToast("拍照失败")
            })
        }
        cameraPv.mAnalyzer = {
            try {
                val timeStart = System.currentTimeMillis()
                ycLogE("耗时toYv21：${System.currentTimeMillis() - timeStart}")
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        checkPermission {}

    }
    private fun checkPermission(call: () -> Unit) {
        mPermissionUtil.mSuccessCall = {
            call()
        }
        mPermissionUtil.start()
    }

    private fun imgCompress(uri: Uri) {
        ycLogESimple("开始压缩：targetSize(${50}) file(${File(uri.path!!).length()})")

        ycLaunchIO {
            Luban.with(this@TestCameraXAc)
                .load(uri)
                .ignoreBy(50)
                .setCompressListener(object : OnCompressListener {
                    override fun onStart() {
                    }

                    override fun onSuccess(file: File) {
                        ycLaunchMain {
                            ycLogESimple("压缩后图片大小：targetSize(${50}) file(${file.length()})")
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
                .launch()
        }
    }
}
