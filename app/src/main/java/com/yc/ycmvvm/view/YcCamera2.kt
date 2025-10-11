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
import android.hardware.camera2.CaptureResult
import android.hardware.camera2.TotalCaptureResult
import android.hardware.camera2.params.StreamConfigurationMap
import android.media.Image
import android.media.ImageReader
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.util.AttributeSet
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.core.app.ActivityCompat
import com.yc.ycmvvm.exception.YcException
import com.yc.ycmvvm.extension.ycLogE
import com.yc.ycmvvm.utils.YcActivityManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Closeable
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeoutException
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


open class YcCamera2 : FrameLayout, YcICamera {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private lateinit var textureView: TextureView
    protected open fun initView() {
        textureView = TextureView(context)
        textureView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
            }

            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
            }
        }
        addView(textureView)
    }

    override fun initPreViewConfig() {
    }

    override fun initCaptureConfig() {
    }

    override fun initRecordConfig() {
    }

    override fun initAnalyzerConfig() {
    }

    suspend fun startImageCapture3() = { ->

    }

    /** Readers used as buffers for camera still shots */
    private lateinit var imageReader: ImageReader
    private val cameraThread = HandlerThread("CameraThread").apply { start() }

    /** [Handler] corresponding to [cameraThread] */
    private val cameraHandler = Handler(cameraThread.looper)

    /** [HandlerThread] where all buffer reading operations run */
    private val imageReaderThread = HandlerThread("imageReaderThread").apply { start() }

    /** [Handler] corresponding to [imageReaderThread] */
    private val imageReaderHandler = Handler(imageReaderThread.looper)

    /** Internal reference to the ongoing [CameraCaptureSession] configured with our parameters */
    private lateinit var session: CameraCaptureSession
    fun startImageCapture2(space: CoroutineScope, block: (File) -> Unit) {
        space.launch(Dispatchers.IO) {
            val result: TestResult = suspendCoroutine { cont ->
                // Flush any images left in the image reader
                @Suppress("ControlFlowWithEmptyBody")
                while (imageReader.acquireNextImage() != null) {
                }
                // Start a new image queue
                val imageQueue = ArrayBlockingQueue<Image>(IMAGE_BUFFER_SIZE)
                imageReader.setOnImageAvailableListener({ reader ->
                    val image = reader.acquireNextImage()
                    Log.d("TAG", "Image available in queue: ${image.timestamp}")
                    imageQueue.add(image)
                }, imageReaderHandler)
                session.capture(
                    session.device.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE).apply { addTarget(imageReader.surface) }.build(),
                    object : CameraCaptureSession.CaptureCallback() {
                        override fun onCaptureCompleted(session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult) {
                            super.onCaptureCompleted(session, request, result)
                            val resultTimestamp = result.get(CaptureResult.SENSOR_TIMESTAMP)
                            Log.d("TAG", "Capture result received: $resultTimestamp")

                            // Set a timeout in case image captured is dropped from the pipeline
                            val exc = TimeoutException("Image dequeuing took too long")
                            val timeoutRunnable = Runnable { cont.resumeWithException(exc) }
                            imageReaderHandler.postDelayed(timeoutRunnable, IMAGE_CAPTURE_TIMEOUT_MILLIS)
                            launch(cont.context) {
                                while (true) {
                                    // Dequeue images while timestamps don't match
                                    val image = imageQueue.take()
                                    // TODO(owahltinez): b/142011420
                                    // if (image.timestamp != resultTimestamp) continue
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && image.format != ImageFormat.DEPTH_JPEG && image.timestamp != resultTimestamp)
                                        continue
                                    Log.d("TAG", "Matching image dequeued: ${image.timestamp}")

                                    // Unset the image reader listener
                                    imageReaderHandler.removeCallbacks(timeoutRunnable)
                                    imageReader.setOnImageAvailableListener(null, null)

                                    // Clear the queue of images, if there are left
                                    while (imageQueue.size > 0) {
                                        imageQueue.take().close()
                                    }
                                    // Compute EXIF orientation metadata
//                                    val rotation = relativeOrientation.value ?: 0
//                                    val mirrored = characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT
//                                    val exifOrientation = computeExifOrientation(rotation, mirrored)
//                                    // Build the result and resume progress
                                    cont.resume(TestResult(image))

                                    // There is no need to break out of the loop, this coroutine will suspend
                                }
                            }
                        }
                    }, cameraHandler
                )
            }

        }
    }

    override suspend fun startRecord() {
    }

    companion object {
        /** Maximum number of images that will be held in the reader's buffer */
        private const val IMAGE_BUFFER_SIZE: Int = 3

        /** Maximum time allowed to wait for the result of an image capture */
        private const val IMAGE_CAPTURE_TIMEOUT_MILLIS: Long = 5000

        data class TestResult(val image: Image) : Closeable {
            override fun close() {
                image.close()
            }
        }
    }
}