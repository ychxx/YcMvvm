package com.yc.ycmvvm.view

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.AttributeSet
import android.util.Log
import android.view.Surface
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.OptIn
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraXConfig
import androidx.camera.core.ExperimentalLensFacing
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.UseCaseGroup
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.yc.ycmvvm.exception.YcException
import com.yc.ycmvvm.extension.ycLogE
import java.util.concurrent.Executor
import java.util.concurrent.Executors

open class YcCameraX : FrameLayout {
    companion object {
        init {
            fun getCameraXConfig(): CameraXConfig {
                return CameraXConfig.Builder.fromConfig(Camera2Config.defaultConfig())
                    .setAvailableCamerasLimiter(CameraSelector.DEFAULT_BACK_CAMERA)
                    .setMinimumLoggingLevel(Log.ERROR)
                    .build()
            }
            ProcessCameraProvider.configureInstance(getCameraXConfig())
        }
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private lateinit var mCameraProvider: ProcessCameraProvider
    private var mPreviewView: PreviewView? = null
    private var mPreview: Preview? = null
    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraExecutor: Executor
    protected var mCameraSelector: CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
    protected lateinit var imageAnalysis: ImageAnalysis

    protected var mCamera: Camera? = null
    var mAnalyzer: YcAnalyzer? = null

    var mAnalysisTimeInterval = 300
    var mAnalysisTimePre = 0L

    /**
     * 拍照生成图片的宽高比
     */
    private var mAspectRatioCapture = AspectRatio.RATIO_16_9

    /**
     * 实时图片的宽高比
     */
    private var mAspectRatioAnalyzer = AspectRatio.RATIO_16_9
    private fun initView() {
        cameraExecutor = Executors.newSingleThreadExecutor()
        initCameraProvider()
    }

    fun openFlash() {
        mCamera?.cameraControl?.enableTorch(true)
    }


    open fun initCameraProvider() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            try {
                mCameraProvider = cameraProviderFuture.get()
                initPreViewConfig()
                initCaptureConfig()
                initRecordConfig()
                initAnalyzerConfig()
                setCameraSelector(CameraSelector.LENS_FACING_BACK)//TODO 待完善 默认有的摄像头
                update()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(context))
    }

    /**
     * 预览配置
     */
    open fun initPreViewConfig() {
        if (mPreviewView == null) {
            mPreviewView = PreviewView(context)
            mPreviewView!!.scaleType = PreviewView.ScaleType.FILL_CENTER
            mPreviewView!!.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            addView(mPreviewView)
            mPreview = Preview.Builder()
                .build().also {
                    it.surfaceProvider = mPreviewView!!.surfaceProvider
                }
        }
    }


    /**
     * 拍照配置
     */
    open fun initCaptureConfig() {
        imageCapture = ImageCapture.Builder() //优化捕获速度，可能降低图片质量
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY) //设置宽高比
            .setTargetAspectRatio(mAspectRatioCapture)
//            .setTargetResolution(size) //设置初始的旋转角度
            .setTargetRotation(Surface.ROTATION_0)//拍照的图片旋转角度
            .build()
    }

    /**
     * 录像配置
     */
    open fun initRecordConfig() {
    }


    /**
     * 实时图片帧配置
     */
    open fun initAnalyzerConfig() {
        imageAnalysis = ImageAnalysis.Builder()
            // enable the following line if RGBA output is needed.
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
//            .setTargetResolution(Size(width, height))
            .setTargetAspectRatio(mAspectRatioAnalyzer)
            .setOutputImageRotationEnabled(true) // 是否旋转分析器中得到的图片
            .setTargetRotation(Surface.ROTATION_0) // 允许旋转后 得到图片的旋转设置
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)// 溢出时，仅保留最好一帧
            .build()
        imageAnalysis.setAnalyzer(cameraExecutor) {
            val current = System.currentTimeMillis()
            if (current - mAnalysisTimePre > mAnalysisTimeInterval) {
                mAnalysisTimePre = System.currentTimeMillis()
                mAnalyzer?.invoke(it)
            }
            it.close()
        }
    }

    protected open fun error(e: YcException) {
        ycLogE(e.msg)
    }

    @OptIn(ExperimentalLensFacing::class)
    fun setCameraSelector(lensFacing: @CameraSelector.LensFacing Int) {
        when (lensFacing) {
            CameraSelector.LENS_FACING_BACK -> {
                if (mCameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA)) {
                    mCameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                } else {
                    error(YcException("后置摄像头不存在", -1))
                }
            }

            CameraSelector.LENS_FACING_FRONT -> {
                if (mCameraProvider.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA)) {
                    mCameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                } else {
                    error(YcException("前置摄像头不存在", -1))
                }
            }

            CameraSelector.LENS_FACING_EXTERNAL -> {
                val external = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build()
                if (mCameraProvider.hasCamera(external)) {
                    mCameraSelector = external
                } else {
                    error(YcException("外置摄像头不存在", -1))
                }
            }
        }
    }

    private fun update() {
        mCameraProvider.unbindAll()
//        val viewPort = ViewPort.Builder(Rational(width, height), display.rotation).build()// 统一预览画面，拍照生成图片，分析图片 等的尺寸
        val useCaseGroup = UseCaseGroup.Builder()
            .addUseCase(mPreview!!)
            .addUseCase(imageAnalysis)
            .addUseCase(imageCapture)
            .build()
        mCamera = mCameraProvider.bindToLifecycle((context as LifecycleOwner), mCameraSelector, useCaseGroup)
    }

    //TODO 待验证
    fun imageCapture(
        context: Context,
        fileName: String,
        callback: (uri: Uri) -> Unit,
        callFail: (e: Exception) -> Unit
    ) {
        // 创建图片存储的ContentValues
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/YourAppName/")
            }
        }

        // 创建输出选项
        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()

        // 执行拍照
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    outputFileResults.savedUri?.let {
                        callback.invoke(it)
                    } ?: run {
                        callFail.invoke(RuntimeException("Saved URI is null"))
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    callFail.invoke(exception)
                    ycLogE("拍照失败: ${exception.message}")
                }
            }
        )
    }
}
typealias YcAnalyzer = (luma: ImageProxy) -> Unit