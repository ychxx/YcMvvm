package com.yc.ycmvvm.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import com.yc.ycmvvm.base.YcBaseActivity
import com.yc.ycmvvm.databinding.VideoAcBinding


class TestVideoXAc : YcBaseActivity<VideoAcBinding>(VideoAcBinding::inflate) {
    companion object {
        fun toAc(context: Context) {
            context.startActivity(Intent(context, TestVideoXAc::class.java))
        }
    }

    val netUrl =".mp4"

    @OptIn(ExperimentalGetImage::class)
    override fun VideoAcBinding.initView(savedInstanceState: Bundle?) {
        changeBtn.setOnClickListener {
            playerView.start(0f, 0.0f, 0.5f, 0.5f, 1920f/1080, netUrl)
        }
        changeBtn2.setOnClickListener {
            playerView.start(0.5f, 0.5f, 1f, 1f, 1920f/1080, netUrl)
        }
    }

    override fun onResume() {
        super.onResume()
    }

}
