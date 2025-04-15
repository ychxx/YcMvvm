package com.yc.ycmvvm.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.yc.ycmvvm.databinding.YcVideoViewBinding
import com.yc.ycmvvm.extension.ycLogE
import kotlin.math.abs

class YcVideoView : FrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        initView()
    }

    /**
     * 组件高
     */
    private var mViewH: Int = 0

    /**
     * 组件宽
     */
    private var mViewW: Int = 0

    private var mPlaySuccessCall: (() -> Unit)? = null
    private val mExoPlayer by lazy {
        ExoPlayer.Builder(context).build().apply {
            // 添加监听器（可选）
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_READY) {
                        ycLogE("播放成功")
                        mPlaySuccessCall?.invoke()
                    }
                }
            })
        }
    }

    private val mViewBinding by lazy {
        YcVideoViewBinding.inflate(LayoutInflater.from(context), this, false)
    }

    fun initView() {
        mViewBinding.playerView.player = mExoPlayer
        mViewBinding.playerView.useController = false
        addView(mViewBinding.root)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (mViewW == 0) {//仅记录初次，后续为缩放不存储
            mViewH = MeasureSpec.getSize(heightMeasureSpec)
            mViewW = MeasureSpec.getSize(widthMeasureSpec)
        }
    }

    /**
     * @param xPer1 选区左上x坐标百分比
     * @param yPer1 选区左上y坐标百分比
     * @param xPer2 选区右下x坐标百分比
     * @param yPer2 选区右下y坐标百分比
     * @param videoScale 视频宽高比
     * @param netUrl 视频流地址
     */
    fun start(xPer1: Float, yPer1: Float, xPer2: Float, yPer2: Float, videoScale: Float, netUrl: String) {
        mViewBinding.maskV.setMask(0f, 0f)
        //视频在屏幕上的初始尺寸
        val videoInitH: Float
        val videoInitW: Float
        if (videoScale > 0) {
            //宽大于高
            videoInitH = mViewW.toFloat() / videoScale
            videoInitW = mViewW.toFloat()
        } else {
            //宽小于高
            videoInitH = mViewH.toFloat()
            videoInitW = mViewH.toFloat() * videoScale
        }
        //视频选取在屏幕上的初始尺寸
        val videoPartH = abs(yPer2 - yPer1) * videoInitH
        val videoPartW = abs(xPer2 - xPer1) * videoInitW


        val videoPartScale = videoPartW / videoPartH
        val viewScale = mViewW.toFloat() / mViewH.toFloat()
        //放大比例
        val scaleEnlarge: Float = if (videoPartScale > viewScale) {
            mViewW / videoPartW
        } else {
            mViewH / videoPartH
        }
        //选取中心点
        val videoPartMidX = (xPer2 + xPer1) / 2
        val videoPartMidY = (yPer2 + yPer1) / 2

        var translationX = 0f
        var translationY = 0f

        translationX = if (videoPartMidX > 0.5) {
            -(videoPartMidX - 0.5f) * scaleEnlarge * videoInitW
        } else {
            (0.5f - videoPartMidX) * scaleEnlarge * videoInitW
        }
        translationY = if (videoPartMidY > 0.5) {
            -(videoPartMidY - 0.5f) * scaleEnlarge * videoInitH
        } else {
            (0.5f - videoPartMidY) * scaleEnlarge * videoInitH
        }
        playView(netUrl)
        mPlaySuccessCall = {
            mViewBinding.playerView.post {
                mViewBinding.playerView.translationX = translationX
                mViewBinding.playerView.translationY = translationY
                mViewBinding.playerView.scaleX = scaleEnlarge
                mViewBinding.playerView.scaleY = scaleEnlarge
//                mViewBinding.playerView.animate()
//                    .translationX(translationX)
//                    .translationY(translationY)
//                    .scaleX(scaleEnlarge)
//                    .scaleY(scaleEnlarge)
//                    .setDuration(10)
//                    .start()
                mViewBinding.maskV.setMask(scaleEnlarge * videoPartW, scaleEnlarge * videoPartH)
            }
        }
    }

    fun playView(netUrl: String) {
        val mediaItem: MediaItem = MediaItem.fromUri(netUrl)
        mExoPlayer.setMediaItem(mediaItem)
        // 准备播放器（缓冲媒体）
        mExoPlayer.prepare()
        // 自动开始播放（可选）
        mExoPlayer.playWhenReady = true
    }
}