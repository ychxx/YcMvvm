package com.yc.ycmvvm.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import com.yc.ycmvvm.App
import com.yc.ycmvvm.base.YcBaseActivity
import com.yc.ycmvvm.databinding.CameraxAcBinding
import com.yc.ycmvvm.databinding.CompressAcBinding
import com.yc.ycmvvm.exception.YcException
import com.yc.ycmvvm.extension.ycLoadImageFile
import com.yc.ycmvvm.extension.ycLogE
import com.yc.ycmvvm.extension.ycShowToast
import com.yc.ycmvvm.net.YcResult
import com.yc.ycmvvm.utils.YcActivityResultUtil
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


class YcCompressAc : YcBaseActivity<CompressAcBinding>(CompressAcBinding::inflate) {
    companion object {
        fun toAc(context: Context) {
            context.startActivity(Intent(context, YcCompressAc::class.java))
        }
    }

    private val mPermissionUtil by lazy {
        YcPermissionUtil(this).apply {
            addPermission(XXPermissionUtil.CAMERA, XXPermissionUtil.RECORD_AUDIO, *XXPermissionUtil.STORAGE)
        }
    }
    private lateinit var mYcActivityResult: YcActivityResultUtil

    @SuppressLint("SetTextI18n")
    @OptIn(ExperimentalGetImage::class)
    override fun CompressAcBinding.initView(savedInstanceState: Bundle?) {
        mYcActivityResult = YcActivityResultUtil(this@YcCompressAc)
        playBtn.setOnClickListener {
            mYcActivityResult.launcherTakePicture { hasSuccess, ycFileTempBean, flag ->
                if (hasSuccess) {
                    ycLaunchMain {
                        imgSizeBeforeTv.text = "压缩前图片大小：${ycFileTempBean.file.length() / 1024}KB"
                        imgBeforeIv.ycLoadImageFile(ycFileTempBean.file)
                    }
                    ycLaunchIO {
                        Luban.with(this@YcCompressAc)
                            .load(ycFileTempBean.file)
                            .ignoreBy(50)
                            .setCompressListener(object : OnCompressListener {
                                override fun onStart() {
                                }

                                override fun onSuccess(file: File) {
                                    ycLaunchMain {
                                        imgSizeCompressTv.text = "压缩后图片大小：${file.length() / 1024}KB"
                                        imgCompressIv.ycLoadImageFile(file)
                                    }
                                }

                                override fun onError(e: Throwable) {
                                    e.printStackTrace()
                                }
                            })
                            .launch()
                    }

                } else {
                    ycShowToast("拍照失败")
                }
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
}
