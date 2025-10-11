package com.yc.ycmvvm.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import com.yc.ycmvvm.exception.IYcExceptionBase
import com.yc.ycmvvm.exception.YcException
import com.yc.ycmvvm.extension.ycShowToast
import com.yc.ycmvvm.net.YcResult
import com.yc.ycmvvm.view.dialog.YcILoading
import com.yc.ycmvvm.view.dialog.YcDialogLoading
import kotlinx.coroutines.launch

/**
 * Creator: yc
 * Date: 2021/6/9 19:50
 * UseDes:
 */
abstract class YcBaseFragment<VB : ViewBinding>(private val createVB: ((LayoutInflater, ViewGroup?, Boolean) -> VB)? = null) : Fragment() {
    private var _mViewBinding: VB? = null
    protected val mViewBinding get() = _mViewBinding!!
    protected open lateinit var mYcLoadingDialog: YcILoading
    protected var mIsShow: Boolean = false
    protected var mFragmentState: Lifecycle.Event = Lifecycle.Event.ON_CREATE

    /**
     * 是否执行过lazySingleLoad()方法了
     */
    protected var mIsLazyLoad = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        lifecycle.addObserver(LifecycleEventObserver { source, event ->
            mFragmentState = event
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    ycOnResume()
                    if (!mIsLazyLoad) {
                        mIsLazyLoad = true
                        ycOnResumeSingle()
                    }
                }

                Lifecycle.Event.ON_CREATE,
                Lifecycle.Event.ON_START,
                Lifecycle.Event.ON_PAUSE,
                Lifecycle.Event.ON_STOP,
                Lifecycle.Event.ON_DESTROY,
                Lifecycle.Event.ON_ANY -> {
                }
            }
        })
        mYcLoadingDialog = YcDialogLoading(requireContext(), this)
        return if (createVB != null) {
            _mViewBinding = createVB.invoke(inflater, container, false)
            _mViewBinding!!.root
        } else {
            super.onCreateView(inflater, container, savedInstanceState)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.initView(view, savedInstanceState)
    }

    protected abstract fun VB.initView(view: View, savedInstanceState: Bundle?)

    /**
     * 用于懒加载（只执行一次）
     */
    protected open fun ycOnResumeSingle() {}

    /**
     * Fragment用户可见时调用
     */
    protected open fun ycOnResume() {}

    //    /**
//     * Fragment用户不可见时调用
//     */
//    protected open fun ycOnPause() {}
    override fun onDestroy() {
        super.onDestroy()
        _mViewBinding = null
    }

    @MainThread
    protected inline fun <reified VM : YcBaseViewModel> Fragment.ycViewModels(): Lazy<VM> {
        return lazy {
            val vm: VM = ViewModelProvider(this, defaultViewModelProviderFactory)[VM::class.java]
            vm.mIsShowLoading.observe(this@YcBaseFragment) {
                if (it.isShow) {
                    this@YcBaseFragment.showLoading(it.msg)
                } else {
                    this@YcBaseFragment.hideLoading()
                }
            }
            vm
        }
    }

    @MainThread
    protected inline fun <reified VM : YcBaseViewModel> Fragment.ycViewModelsActivity(): Lazy<VM> {
        return lazy {
            val vm: VM = ViewModelProvider(requireActivity().viewModelStore, defaultViewModelProviderFactory)[VM::class.java]
            vm.mIsShowLoading.observe(this@YcBaseFragment) {
                if (it.isShow) {
                    this@YcBaseFragment.showLoading(it.msg)
                } else {
                    this@YcBaseFragment.hideLoading()
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

    protected fun launch(block: suspend () -> Unit) {
        lifecycleScope.launch {
            block()
        }
    }

    protected fun <T> LiveData<T>.ycObserve(observer: Observer<T>) {
        this.observe(this@YcBaseFragment, observer)
    }

//    protected fun <T : Any, VH : RecyclerView.ViewHolder> PagingDataAdapter<T, VH>.ycSubmitData(pagingData: PagingData<T>) {
//        this.submitData(this@YcBaseFragment.viewLifecycleOwner.lifecycle, pagingData)
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