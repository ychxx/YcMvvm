package com.yc.ycmvvm.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yc.ycmvvm.extension.ycIsTrue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicInteger

class YcLoop2 {
    /**
     *  作用域
     */
    protected val mScope = CoroutineScope(Job() + Dispatchers.IO)

    /**
     * 句柄
     */
    protected var mJop: Job? = null

    /**
     * 间隔时间(单位毫秒)
     */
    var mPeriodTime: Long = 2000L

    private var mState: AtomicInteger = AtomicInteger(0)
    protected val _mPost: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val mPost: LiveData<Boolean> = _mPost
    open fun start(isWait: Boolean = false) {
        if (mState.get() != -1) {
            mJop?.cancel()
            mState.set(1)
            mJop = mScope.launch {
                try {
                    if (isWait)
                        delay(mPeriodTime)
                    if (mState.get() != -1 && isActive) {
                        _mPost.postValue(!_mPost.value.ycIsTrue())
                        start(true)
                    }
                } catch (e: Exception) {
                    coroutineContext.ensureActive()
                    if (mState.get() != -1 && isActive) {
                        start(true)
                    }
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * 主动关闭（onDestroy一般会自动调用该方法）
     */
    fun finish() {
        try {
            mState.set(-1)
            mJop?.cancel()
            mScope.cancel()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}