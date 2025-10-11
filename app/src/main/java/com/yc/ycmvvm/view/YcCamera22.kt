package com.yc.ycmvvm.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.TotalCaptureResult
import android.hardware.camera2.params.StreamConfigurationMap
import android.media.ImageReader
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.util.AttributeSet
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.core.app.ActivityCompat
import com.yc.ycmvvm.exception.YcException
import com.yc.ycmvvm.extension.ycLogE
import com.yc.ycmvvm.utils.YcActivityManager
import java.io.File
import java.io.FileOutputStream


open class YcCamera22 : FrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private lateinit var textureView: TextureView
    private val surfaceTextureListener = object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
            openCamera()
        }

        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
            return false
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
        }
    }
    private var cameraDevice: CameraDevice? = null // 相机设备对象

    // 相机设备状态回调
    private val cameraStateCallback: CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
        // 相机成功打开
        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
            startPreview() // 开始预览
        }

        // 相机断开连接
        override fun onDisconnected(camera: CameraDevice) {
            closeCamera() // 关闭相机
        }

        // 相机发生错误
        override fun onError(camera: CameraDevice, error: Int) {
            closeCamera()
        }
    }

    private fun initView() {
        textureView = TextureView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            surfaceTextureListener = this@YcCamera22.surfaceTextureListener
        }
        addView(textureView)
    }

    fun onResume() {
        if (textureView.isAvailable) {
            openCamera()
        } else {
            textureView.surfaceTextureListener = this@YcCamera22.surfaceTextureListener;
        }
    }

    private val manager by lazy { context.getSystemService(Context.CAMERA_SERVICE) as CameraManager }

    // 相机相关变量
    private var cameraId: String? = null // 相机ID（通常是后置摄像头）

    /**
     * 预览尺寸
     */
    private var preViewSize: Size? = null

    /**
     * 预览尺寸
     */
    private var imageReader: ImageReader? = null

    @SuppressLint("MissingPermission")
    fun openCamera() {

        try {
            // 获取第一个相机ID（通常是后置摄像头）
            cameraId = manager.cameraIdList[0]

            initPreViewConfig()
            // 创建ImageReader用于捕获JPEG图像
            imageReader = ImageReader.newInstance(
                preViewSize!!.width,
                preViewSize!!.height,
                ImageFormat.JPEG,
                2 // 最大图像数（2保证拍照时不会阻塞预览）
            )
            imageReader?.setOnImageAvailableListener(imageAvailableListener, backgroundHandler)

            // 检查相机权限
//            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // 打开相机
            manager.openCamera(cameraId!!, cameraStateCallback, backgroundHandler)
//            } else {
//                ycLogE("没有照相机权限")
//            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val backgroundHandler by lazy {
        Handler(HandlerThread("Camera2Background").apply {
            start()
        }.getLooper());
    }
    var mSaveDirPath = if (context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) != null) {
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.path
    } else {
        context.filesDir.path
    } + "photo_" + System.currentTimeMillis() + ".jpg"

    // 图像可用监听器
    private val imageAvailableListener = ImageReader.OnImageAvailableListener { reader -> // 在后台线程处理图像
        backgroundHandler.post {
            reader.acquireNextImage().use {
                val buffer = it.planes[0].buffer
                val bytes = ByteArray(buffer.remaining())
                buffer[bytes]
                // 创建输出文件
                Thread.sleep(5000)
                val outputFile = File(mSaveDirPath)
                // 写入文件
                FileOutputStream(outputFile).use {
                    it.write(bytes)
                }
            }
        }
    }

    fun openFlash() {
//        mCamera?.cameraControl?.enableTorch(true)
    }


    /**
     * 预览配置
     */
    open fun initPreViewConfig() {
        if (preViewSize == null) {
            // 获取可用的流配置
            val map: StreamConfigurationMap? = manager.getCameraCharacteristics(cameraId!!).get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            val bigEnough: MutableList<Size> = ArrayList()
            // 收集所有满足最小要求的尺寸
            val choices = map?.getOutputSizes(SurfaceTexture::class.java) ?: return
            val viewWidth = textureView.width
            val viewHeight = textureView.height
            choices.forEach {
                // 检查宽高比是否匹配视图（16:9, 4:3等）
//                if (it.width <= 480 && it.height <= 640) {
                    bigEnough.add(it)
//                }
            }
            // 如果没有满足要求的尺寸，选择最大的可用尺寸
            if (bigEnough.isEmpty()) {
                preViewSize = choices[0]
            } else {
                // 否则选择最接近视图尺寸的选项
                preViewSize = bigEnough.maxByOrNull { it.width * it.height }
            }
        }
    }


    /**
     * 拍照配置
     */
    open fun initCaptureConfig() {

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

    }

    protected open fun error(e: YcException) {
        ycLogE(e.msg)
    }

    var captureSession: CameraCaptureSession? = null
    fun startPreview() {
        try {
            // 配置TextureView的SurfaceTexture
            val texture = textureView.surfaceTexture
            texture!!.setDefaultBufferSize(preViewSize!!.width, preViewSize!!.height)
            val previewSurface = Surface(texture)


            // 创建预览请求构建器
            val previewRequestBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            previewRequestBuilder.addTarget(previewSurface)

            // 创建捕获会话（包含预览Surface和ImageReader的Surface）
            cameraDevice!!.createCaptureSession(
                listOf<Surface>(previewSurface, imageReader!!.surface),
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(session: CameraCaptureSession) {
                        captureSession = session
                        try {
                            // 设置自动对焦模式
                            previewRequestBuilder.set(
                                CaptureRequest.CONTROL_AF_MODE,
                                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                            )
                            // 设置重复请求（持续预览）
                            session.setRepeatingRequest(previewRequestBuilder.build(), null, backgroundHandler)
                        } catch (e: CameraAccessException) {
                            e.printStackTrace()
                        }
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {
                        ycLogE("配置失败")
                    }
                },
                backgroundHandler
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    fun closeCamera() {

    }

    fun imageCapture() {
        if (cameraDevice == null) return
        try {
            // 创建拍照请求构建器
            val captureBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            captureBuilder.addTarget(imageReader!!.surface)
            // 设置自动对焦和自动曝光
            captureBuilder.set(
                CaptureRequest.CONTROL_AF_MODE,
                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
            )
            // 设置JPEG方向（根据设备旋转）
            val rotation: Int = YcActivityManager.getCurrentActivity()!!.windowManager.defaultDisplay.rotation
            captureBuilder.set(
                CaptureRequest.JPEG_ORIENTATION, when (rotation) {
                    Surface.ROTATION_0 -> 90
                    Surface.ROTATION_90 -> 0
                    Surface.ROTATION_180 -> 270
                    Surface.ROTATION_270 -> 180
                    else -> 90
                }
            )
            // 停止预览以拍照
            // 执行拍照请求
            captureSession!!.capture(captureBuilder.build(), object : CameraCaptureSession.CaptureCallback() {
                override fun onCaptureCompleted(session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult) {
                    ycLogE("拍照成功")
                }
            }, backgroundHandler)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }
}