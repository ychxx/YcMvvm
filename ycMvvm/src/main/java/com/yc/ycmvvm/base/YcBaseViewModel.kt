package com.yc.ycmvvm.base

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yc.ycmvvm.data.entity.YcLoadingBean
import com.yc.ycmvvm.exception.YcException
import com.yc.ycmvvm.extension.toYcException
import com.yc.ycmvvm.net.YcResult
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * Creator: yc
 * Date: 2021/6/15 16:45
 * UseDes:
 */
open class YcBaseViewModel : ViewModel() {
    /**
     * 用于显示加载框
     */
    protected val _mIsShowLoading = MutableLiveData<YcLoadingBean>()
    val mIsShowLoading: LiveData<YcLoadingBean> = _mIsShowLoading
    protected open var mDefaultDelayTime = 1500L
    private val mainHandler = Handler(Looper.getMainLooper())


    protected fun <T> ycFlow(
        block: suspend () -> T,
        handle: (ex: Throwable) -> YcException? = { ex -> errHandle(ex) },
        hasShowLoading: Boolean = true,
        hasAutoClose: Boolean = true
    ) = flow<YcResult<T>> {
        val result = block.invoke()
        emit(YcResult.Success(result))
    }.onStart {
        if (hasShowLoading) showLoading()
    }.onCompletion {
        if (hasAutoClose) hideLoading()
    }.catch {
        it.printStackTrace()
        val result = handle.invoke(it)
        if (result != null) {
            emit(YcResult.Fail(result))
        }
    }

    protected fun <T> ycFlowIo(
        block: suspend () -> T,
        handle: (ex: Throwable) -> YcException? = { ex -> errHandle(ex) },
        hasShowLoading: Boolean = true,
        hasAutoClose: Boolean = true
    ) = flow<YcResult<T>> {
        val result = block.invoke()
        emit(YcResult.Success(result))
    }.onStart {
        if (hasShowLoading) showLoading()
    }.onCompletion {
        if (hasAutoClose) hideLoading()
    }.catch {
        it.printStackTrace()
        val result = handle.invoke(it)
        if (result != null) {
            emit(YcResult.Fail(result))
        }
    }.flowOn(Dispatchers.IO)

    protected fun <Data> Flow<Data>.ycCollect(block: (Data) -> Unit) {
        viewModelScope.launch {
            this@ycCollect.collect {
                block.invoke(it)
            }
        }
    }

    protected fun <Data> Flow<Data>.ycCollect2(
        handle: (ex: Throwable) -> YcException? = { ex -> errHandle(ex) },
        hasShowLoading: Boolean = true,
        hasAutoClose: Boolean = true,
        block: (YcResult<Data>) -> Unit
    ) {
        viewModelScope.launch {
            this@ycCollect2.onStart {
                if (hasShowLoading) showLoading()
            }.onCompletion {
                if (hasAutoClose) hideLoading()
            }.catch {
                it.printStackTrace()
                val result = handle.invoke(it)
                if (result != null) {
                    block.invoke(YcResult.Fail(result))
                }
            }.flowOn(Dispatchers.IO).collect {
                block.invoke(YcResult.Success(it))
            }
        }
    }

    /**
     * 异常处理
     * @return YcException? 为空时，不执行业务中的异常捕获
     */
    open fun errHandle(ex: Throwable): YcException? {
        return ex.toYcException()
    }


    protected fun ycLaunch(block: suspend (coroutineScope: CoroutineScope) -> Unit): Job {
        return viewModelScope.launch {
            block(this)
        }
    }


    /**
     * 冷流开始时，显示加载框
     * 冷流结束时，自动隐藏加载框
     */
    protected fun <T> Flow<T>.ycHasLoading(msg: String? = null, delayTime: Long = mDefaultDelayTime): Flow<T> = onStart {
        showLoading(msg, delayTime)
    }.onCompletion {
        hideLoading()
    }
//    protected fun <T> Flow<T>.ycHasLoading(delayTime: Long = mDefaultDelayTime): Flow<T> {
//        this.onStart {
//            showLoading(delayTime)
//        }.onCompletion {
//            hideLoading()
//        }
//        return this
//    }

    /**
     * 冷流开始时，显示加载框
     * 冷流结束时，不会隐藏加载框，需手动调用hideLoading方法
     */
    protected fun <T> Flow<T>.ycHasLoadingNoAutoClose(msg: String? = null, delayTime: Long = mDefaultDelayTime): Flow<T> = onStart {
        showLoading(msg, delayTime)
    }

    /**
     * 显示加载框
     * @param delayTime Long  延迟delayTime毫秒后，再显示，减少请求很快返回时，导致闪下的体验。
     * 如果在delayTime毫秒内，请求就完成了，对话框就不会显示
     */
    protected fun showLoading(msg: String? = null, delayTime: Long = mDefaultDelayTime) {
        mainHandler.post {
            _mIsShowLoading.postValue(YcLoadingBean.show(msg))
        }
    }

    /**
     * 隐藏加载框
     */
    protected fun hideLoading() {
        mainHandler.post {
            _mIsShowLoading.postValue(YcLoadingBean.hide())
        }
    }

    override fun onCleared() {
        hideLoading()
        super.onCleared()
    }
}