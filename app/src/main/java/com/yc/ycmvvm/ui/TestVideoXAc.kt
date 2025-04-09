package com.yc.ycmvvm.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.yc.ycmvvm.base.YcBaseActivity
import com.yc.ycmvvm.databinding.VideoAcBinding


class TestVideoXAc : YcBaseActivity<VideoAcBinding>(VideoAcBinding::inflate) {
    companion object {
        fun toAc(context: Context) {
            context.startActivity(Intent(context, TestVideoXAc::class.java))
        }
    }

    val netUrl =
        "https://cdn.iscreator.com/aisport3/helpDocument/sportPreviewVideo/%E4%BB%B0%E5%8D%A7%E8%B5%B7%E5%9D%90%E7%A4%BA%E8%8C%83%E8%A7%86%E9%A2%91.mp4"


    @OptIn(ExperimentalGetImage::class)
    override fun VideoAcBinding.initView(savedInstanceState: Bundle?) {
        changeBtn.setOnClickListener {
            playerView.start(0f, 0.0f, 0.1f, 0.1f, 1920f/1080, netUrl)

        }
    }

    override fun onResume() {
        super.onResume()
    }

}
