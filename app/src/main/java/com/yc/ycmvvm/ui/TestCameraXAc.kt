package com.yc.ycmvvm.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import com.hyperai.hyperlpr3.HyperLPR3
import com.hyperai.hyperlpr3.bean.Plate
import com.hyperai.hyperlpr3.settings.TypeDefine
import com.yc.ycmvvm.base.YcBaseActivity
import com.yc.ycmvvm.databinding.CameraxAcBinding
import com.yc.ycmvvm.extension.ycLogE
import com.yc.ycmvvm.utils.YcCameraXUtil
import com.yc.ycmvvm.utils.permission.XXPermissionUtil
import com.yc.ycmvvm.utils.permission.YcPermissionUtil
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


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

    lateinit var preview: Preview

    @OptIn(ExperimentalGetImage::class)
    override fun CameraxAcBinding.initView() {
//        val cameraController = LifecycleCameraController(baseContext)
//        cameraController.bindToLifecycle(this@TestCameraXAc)
//        mViewBinding.cameraPv.controller = cameraController
        cameraExecutor = Executors.newSingleThreadExecutor()
        flashBtn.setOnClickListener {
            cameraPv.openFlash()
        }
        resetBtn.setOnClickListener {
            mRecognitionResult = null
            mViewBinding.resultTv.text ="请对准车牌"
        }
        cameraPv.mAnalyzer = {
            try {
                val timeStart = System.currentTimeMillis()
                val imgYv21 = YcCameraXUtil.toYv21(it)
                ycLogE("耗时toYv21：${System.currentTimeMillis() - timeStart}")

                val timeStart2 = System.currentTimeMillis()
                val plates = HyperLPR3.getInstance().plateRecognition(
                    imgYv21,
                    it.height,
                    it.width,
                    HyperLPR3.CAMERA_ROTATION_0,
                    HyperLPR3.STREAM_YUV_NV21
                )
                ycLogE("耗时HyperLPR3：${System.currentTimeMillis() - timeStart2}")
                if (plates.isNotEmpty()) {
                    ycLogE("成功：${plates[0]}")
                    plateFilter(plates[0])
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        checkPermission {
//            mViewBinding.cameraPv.post {
//                startCamera()
//            }
        }
    }

    var mRecognitionResult: Plate? = null
    fun plateFilter(plate: Plate) {
        val plateNum = plate.code
        if (hasPlateNum(plateNum)) {
            if (mRecognitionResult == null) {
                runOnUiThread {
                    mRecognitionResult = plate
                    mViewBinding.resultTv.text = "扫描结果：${plate.code}, ${TypeDefine.PLATE_TYPE_MAPS[plate.type]}"
                }
            } else {
                if (mRecognitionResult!!.confidence < plate.confidence) {
                    runOnUiThread {
                        mRecognitionResult = plate
                        mViewBinding.resultTv.text = "扫描结果：${plate.code}, ${TypeDefine.PLATE_TYPE_MAPS[plate.type]}"
                    }
                }
            }
        }
    }

    fun hasPlateNum(plate: String): Boolean {

        val pattern =
            Regex("((^(京|津|沪|渝|冀|豫|云|辽|黑|湘|皖|鲁|新|苏|浙|赣|鄂|桂|甘|晋|蒙|陕|吉|闽|贵|粤|青|藏|川|宁|琼)[A-Z]{1}[A-Z0-9]{5}[A-Z0-9挂学警港澳试超使领北南军海空外]{1}${'$'})|(^(京|津|沪|渝|冀|豫|云|辽|黑|湘|皖|鲁|新|苏|浙|赣|鄂|桂|甘|晋|蒙|陕|吉|闽|贵|粤|青|藏|川|宁|琼)[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳试超使领北南军海空外]{1}${'$'}))")
        return pattern.matches(plate)
    }

    private fun checkPermission(call: () -> Unit) {
        mPermissionUtil.mSuccessCall = {
            call()
        }
        mPermissionUtil.start()
    }

    private class LuminosityAnalyzer(val width: Int, val height: Int) : ImageAnalysis.Analyzer {
        private val frameRateWindow = 8
        private val frameTimestamps = ArrayDeque<Long>(5)
        private var lastAnalyzedTimestamp = 0L
        var framesPerSecond: Double = -1.0
            private set

        /**
         * Helper extension function used to extract a byte array from an image plane buffer
         */
        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // Rewind the buffer to zero
            val data = ByteArray(remaining())
            get(data)   // Copy the buffer into a byte array
            return data // Return the byte array
        }

        /**
         *分析图像以产生结果。
         *
         *<p>调用者负责确保此分析方法能够快速执行
         *足以防止图像采集管道中的停滞。否则，新可用
         *将不会获取和分析图像。
         *
         *<p>此方法返回后，传递给此方法的图像将无效。来电者
         *不应存储对此映像的外部引用，因为这些引用将成为
         *无效。
         *
         *正在分析@param图像非常重要：分析器方法实现必须
         *使用完毕后，对接收到的图像调用image.close（）。否则，新图像
         *根据背压设置，可能无法接收或相机可能会失速。
         *
         */
        override fun analyze(image: ImageProxy) {
            // Keep track of frames analyzed
            val currentTime = System.currentTimeMillis()
            frameTimestamps.add(currentTime)
            // Compute the FPS using a moving average
            while (frameTimestamps.size >= frameRateWindow) frameTimestamps.removeLast()
            val timestampFirst = frameTimestamps.first() ?: currentTime
            val timestampLast = frameTimestamps.last() ?: currentTime
            framesPerSecond = 1.0 / ((timestampFirst - timestampLast) /
                    frameTimestamps.size.coerceAtLeast(1).toDouble()) * 1000.0

            // Analysis could take an arbitrarily long amount of time
            // Since we are running in a different thread, it won't stall other use cases

            lastAnalyzedTimestamp = frameTimestamps.first()

            // Since format in ImageAnalysis is YUV, image.planes[0] contains the luminance plane
            val buffer = image.planes[0].buffer

            // Extract image data from callback object
//            val data = buffer.toByteArray()
            val nv21 = toYv21(image)
            val bitmap = ycGetBitmap(nv21, width, height)
            val plates = HyperLPR3.getInstance().plateRecognition(nv21, height, width, HyperLPR3.CAMERA_ROTATION_90, HyperLPR3.STREAM_YUV_NV21)
            if (plates.isNotEmpty())
                ycLogE("识别成功(Hyper), ${plates[0].code}, ${TypeDefine.PLATE_TYPE_MAPS[plates[0].type]}")
            // Call all listeners with new value
//            listeners.forEach { it(luma) }

            image.close()
        }

        fun ycGetBitmap(nv21: ByteArray, width: Int, height: Int): Bitmap {
            val yuvImage = YuvImage(nv21, ImageFormat.NV21, width, height, null)
            // 将YUV数据转换为Bitmap
            val out = ByteArrayOutputStream()
            yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 100, out)
            val imageBytes = out.toByteArray()
            // 将字节数组转换为Bitmap
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            return bitmap
        }

        fun toYv21(image: ImageProxy): ByteArray {
            // 拿到YUV数据
            val yBuffer: ByteBuffer = image.planes[0].buffer
            val uBuffer: ByteBuffer = image.planes[1].buffer
            val vBuffer: ByteBuffer = image.planes[2].buffer

            val numPixels = (width * height * 1.5f).toInt()
            val nv21 = ByteArray(numPixels) // 转换后的数据
            var index = 0
            // 复制Y的数据
            val yRowStride: Int = image.planes[0].rowStride
            val yPixelStride: Int = image.planes[0].pixelStride
            for (y in 0 until height) {
                for (x in 0 until width) {
                    nv21[index++] = yBuffer[y * yRowStride + x * yPixelStride]
                }
            }
            // 复制U/V数据
            val uvRowStride: Int = image.planes[1].rowStride
            val uvPixelStride: Int = image.planes[1].pixelStride
            val uvWidth = width / 2
            val uvHeight = height / 2

            for (y in 0 until uvHeight) {
                for (x in 0 until uvWidth) {
                    val bufferIndex = (y * uvRowStride) + (x * uvPixelStride)
                    nv21[index++] = vBuffer[bufferIndex]
                    nv21[index++] = uBuffer[bufferIndex]
                }
            }
            return nv21
        }
    }


}
