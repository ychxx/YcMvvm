package com.yc.ycmvvm.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.yc.ycmvvm.exception.IYcExceptionBase
import com.yc.ycmvvm.utils.YcActivityManager
import com.yc.ycmvvm.exception.YcException
import com.yc.ycmvvm.extension.ycShowToast
import com.yc.ycmvvm.net.YcResult
import com.yc.ycmvvm.view.dialog.YcILoading
import com.yc.ycmvvm.view.dialog.YcDialogLoading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * Creator: yc
 * Date: 2021/6/9 16:47
 * UseDes:
 */
@SuppressLint("SetTextI18n")
abstract class YcBaseActivity<VB : ViewBinding>(private val createVB: ((LayoutInflater) -> VB)? = null) : AppCompatActivity() {
    protected lateinit var mViewBinding: VB
    protected open lateinit var mYcLoadingDialog: YcILoading
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mYcLoadingDialog = YcDialogLoading(this, this)
        if (createVB != null) {
            mViewBinding = createVB.invoke(LayoutInflater.from(this as Context))
            setContentView(mViewBinding.root)
            mViewBinding.initView(savedInstanceState)
        }
        YcActivityManager.addActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        YcActivityManager.finishActivity(this)
    }
    fun getContext(): Context {
        return this
    }

    abstract fun VB.initView(savedInstanceState: Bundle?)

    @MainThread
    protected inline fun <reified VM : YcBaseViewModel> ComponentActivity.ycViewModels(): Lazy<VM> {
        return lazy {
            val vm: VM = ViewModelProvider(this, defaultViewModelProviderFactory)[VM::class.java]
            vm.mIsShowLoading.observe(this@YcBaseActivity) {
                if (it.isShow) {
                    this@YcBaseActivity.showLoading(it.msg)
                } else {
                    this@YcBaseActivity.hideLoading()
                }
            }
            vm
        }
    }

    open fun showLoading(msg: String? = null) {
        mYcLoadingDialog.loadShow(msg)
    }

    open fun hideLoading() {
        mYcLoadingDialog.loadHide()
    }

    protected fun ycLaunch(block: suspend () -> Unit) = lifecycleScope.launch {
        block()
    }
    protected fun ycLaunchMain(block: suspend () -> Unit) = lifecycleScope.launch(Dispatchers.Main) {
        block()
    }
    protected fun ycLaunchIO(block: suspend () -> Unit) = lifecycleScope.launch(Dispatchers.IO) {
        block()
    }
    protected fun <T> LiveData<T>.ycObserve(observer: Observer<T>) {
        this.observe(this@YcBaseActivity, observer)
    }

//    protected fun <T : Any, VH : RecyclerView.ViewHolder> PagingDataAdapter<T, VH>.ycSubmitData(pagingData: PagingData<T>) {
//        this.submitData(this@YcBaseActivity.lifecycle, pagingData)
//    }
//
//    protected fun <Data : Any, VB : ViewBinding> YcPagingDataAdapter<Data, VB>.ycSubmitData(pagingData: PagingData<Data>) {
//        this.ycSubmitData(this@YcBaseActivity, pagingData)
//    }
//
//    protected fun <Data : Any> YcRefreshBaseUtil<Data>.ycSetPagingData(pagingData: PagingData<Data>) {
//        this.setPagingData(this@YcBaseActivity, pagingData)
//    }
//
//    protected fun <Data : Any> YcRefreshBaseUtil<Data>.ycClearPagingData() {
//        this.clearPagingData(this@YcBaseActivity)
//    }

    /**
     * @param defaultMsg String 当错误msg为空时，显示的提示
     */
    protected fun YcResult<*>.ycShowNetError(defaultMsg: String = "请求失败,错误msg为空") {
        if (this is YcResult.Fail) {
            exception.ycShowNetError(defaultMsg)
        }
    }

    protected fun IYcExceptionBase.ycShowNetError(defaultMsg: String = "请求失败,错误msg为空") {
        ycShowToast(msg ?: defaultMsg)
    }
}
