package com.yc.ycmvvm.ui

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
import com.yc.ycmvvm.base.YcBaseActivity
import com.yc.ycmvvm.databinding.Camera2AcBinding
import com.yc.ycmvvm.databinding.CameraxAcBinding
import com.yc.ycmvvm.extension.ycLogE
import com.yc.ycmvvm.utils.YcCameraXUtil
import com.yc.ycmvvm.utils.permission.XXPermissionUtil
import com.yc.ycmvvm.utils.permission.YcPermissionUtil
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class TestCamera2Ac : YcBaseActivity<Camera2AcBinding>(Camera2AcBinding::inflate) {
    companion object {
        fun toAc(context: Context) {
            context.startActivity(Intent(context, TestCamera2Ac::class.java))
        }
    }

    val mPermissionUtil by lazy {
        YcPermissionUtil(this).apply {
            addPermission(XXPermissionUtil.CAMERA, XXPermissionUtil.RECORD_AUDIO, *XXPermissionUtil.STORAGE)
        }
    }

    @OptIn(ExperimentalGetImage::class)
    override fun Camera2AcBinding.initView(savedInstanceState: Bundle?) {

        checkPermission {}
        startBtn.setOnClickListener {
//            cameraPv.imageCapture()
        }
    }


    private fun checkPermission(call: () -> Unit) {
        mPermissionUtil.mSuccessCall = {
            call()
        }
        mPermissionUtil.start()
    }
}
