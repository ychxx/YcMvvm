package com.yc.ycmvvm.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
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
import java.util.concurrent.atomic.AtomicLong

class YcLoop2 {
    protected var mScope: CoroutineScope? = null
    protected var mJop: Job? = null
    var mPeriodTime: Long = 2000L
    protected val _mPost = MutableLiveData<Boolean>(false)
    val mPost: LiveData<Boolean> = _mPost
    var mCall: (() -> Unit)? = null
    private val mLoopFlag = AtomicLong(0)
    private val mObservers = mutableListOf<Observer<Boolean>>()

    fun loopObserve(owner: LifecycleOwner, block: (Boolean) -> Unit) {
        val observer = Observer<Boolean> { block(it) }
        mObservers.add(observer)
        mPost.observe(owner, observer)
    }

    fun start(isWait: Boolean = false) {
        if (mScope?.isActive != true) {
            mScope = CoroutineScope(Job() + Dispatchers.IO)
        }
        mLoopFlag.set(System.currentTimeMillis() + YcRandom.getInt(100))
        startLoop(isWait, mLoopFlag.get())
    }

    private fun startLoop(isWait: Boolean, loopFlag: Long) {
        if (mLoopFlag.get() == loopFlag) {
            mJop?.cancel()
            mJop = mScope?.launch {
                try {
                    if (isWait)
                        delay(mPeriodTime)
                    if (mLoopFlag.get() == loopFlag && isActive) {
                        _mPost.postValue(!_mPost.value.ycIsTrue())
                        mCall?.invoke()
                        startLoop(true, loopFlag)
                    }
                } catch (e: Exception) {
                    coroutineContext.ensureActive()
                    if (mLoopFlag.get() == loopFlag && isActive) {
                        startLoop(true, loopFlag)
                    }
                    e.printStackTrace()
                }
            }
        }
    }


    fun finish() {
        mLoopFlag.set(0)
        mJop?.cancel();
        mJop = null
        mScope?.cancel();
        mScope = null
        mCall = null
        mObservers.forEach { mPost.removeObserver(it) }
        mObservers.clear()
    }
}