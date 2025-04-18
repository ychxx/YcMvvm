package com.yc.ycmvvm.utils

import androidx.lifecycle.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicReference

/**
 * 在onStop时挂起协程，onResume时才重新启动协程,循环会被暂停
 */
open class YcLoopLife(val owner: LifecycleOwner) {
    init {
        owner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                finish()
            }
        })
    }

    /**
     * 执行条件
     */
    protected val mPost: MutableLiveData<Any?> = MutableLiveData<Any?>()

    /**
     * 句柄
     */
    protected var mJop: Job? = null

    /**
     * 句柄
     */
    protected var mJopHandle: Job? = null

    /**
     * 间隔时间(单位毫秒)
     */
    var mPeriodTime: Long = 2000L

    /**
     * 回调
     */
    var mBlock: (() -> Unit)? = null

    /**
     * 是否延迟
     * @return true 延迟，false 不延迟，立刻执行
     */
    private var mIsDelay: AtomicReference<Boolean> = AtomicReference(false)

    init {
        reset()
    }

    fun stop() {
        mIsDelay.set(true)
    }

    fun recovery() {
        mIsDelay.set(false)
    }

    fun stopOrRecovery(isStop: Boolean) {
        mIsDelay.set(isStop)
    }

    fun reset() {
        mPost.observe(owner, Observer {
            mJopHandle?.cancel()
            mJopHandle = owner.lifecycleScope.launch {
                while (mIsDelay.get()) {
                    delay(100)
                }
                mBlock?.invoke()
                start(true)
            }
        })
    }

    open fun start(isWait: Boolean = false) {
        mJop?.cancel()
        mJop = owner.lifecycleScope.launch {
            if (isWait)
                delay(mPeriodTime)
            mPost.postValue(null)
        }
    }

    /**
     * 主动关闭（onDestroy一般会自动调用该方法）
     */
    fun finish() {
        mJop?.cancel()
        mJopHandle?.cancel()
    }
}