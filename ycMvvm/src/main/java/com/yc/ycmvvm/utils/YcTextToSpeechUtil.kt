package com.yc.ycmvvm.utils

import android.speech.tts.TextToSpeech
import com.yc.ycmvvm.config.YcInit
import com.yc.ycmvvm.extension.ycLogE
import java.util.Locale

/**
 * 文字转语音
 */
object YcTextToSpeechUtil {

    private var textToSpeech: TextToSpeech? = null
    fun init(call: () -> Unit) {
        if (textToSpeech == null) {
            textToSpeech = TextToSpeech(YcInit.mInstance.mApplication) {
                if (it == TextToSpeech.SUCCESS) {
                    textToSpeech!!.setLanguage(Locale.CHINA)
                    textToSpeech!!.setPitch(1.0f) // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
                    textToSpeech!!.setSpeechRate(0.85f)
                    call.invoke()
                } else {
                    ycLogE("TextToSpeech初始化失败")
                    textToSpeech = null
                }
            }
        } else {
            call.invoke()
        }
    }

    fun speakText(msg: String, queueMode: Int = TextToSpeech.QUEUE_FLUSH) {
        init {
            textToSpeech?.speak(msg, queueMode, null, "")
        }
    }

    fun finish() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null
    }
}