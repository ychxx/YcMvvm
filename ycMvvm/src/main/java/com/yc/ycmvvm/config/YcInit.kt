package com.yc.ycmvvm.config

import android.app.Application
import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.LifecycleOwner
import com.elvishew.xlog.BuildConfig
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.yc.ycmvvm.utils.release.YcSpecialViewConfigureImp
import com.yc.ycmvvm.R
import com.yc.ycmvvm.data.constans.YcNetErrorCode
import com.yc.ycmvvm.exception.YcException
import com.yc.ycmvvm.extension.YcLogExt
import com.yc.ycmvvm.utils.release.YcSpecialState
import com.yc.ycmvvm.utils.release.YcSpecialViewConfigureBase
import com.yc.ycmvvm.view.dialog.YcDialogCommon
import com.yc.ycmvvm.view.dialog.YcIDialog


import okhttp3.Interceptor
import org.xutils.x
import java.io.File

/**
 * Creator: yc
 * Date: 2021/6/3 13:54
 * UseDes:
 */
class YcInit private constructor() {
    companion object {
        const val OTHER_BASE_URL = "other_base_url"

        init {
            //设置全局的Header构建器
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ -> MaterialHeader(context) }
            //设置全局的Footer构建器
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ -> ClassicsFooter(context) }
        }

        @JvmStatic
        val mInstance = YcJetpack2Holder.holder
    }

    private object YcJetpack2Holder {
        @JvmStatic
        val holder = YcInit()
    }

    /**
     * 加载网络图片失败时显示的图片
     */
    var mImgIdResFail: Int = R.drawable.yc_loading_fail

    /**
     * 加载网络图片加载时显示的图片
     */
    var mImgIdResLoading: Int = R.drawable.yc_loading

    /**
     * 请求成功返回的code
     */
    var mNetSuccessCode: MutableList<Int>? = mutableListOf(200)

    /**
     * retrofit的过滤器
     */
    val mInterceptor: MutableList<Interceptor> = mutableListOf()

    /**
     * 接口地址
     */
    var mDefaultBaseUrl = ""

        /**
     * 创建一个替换布局（用于不一致时变换）
     */
    var mCreateSpecialViewBuildBase: ((context: Context) -> YcSpecialViewConfigureBase) = {
        YcSpecialViewConfigureImp(it)
    }

    /**
     * 默认申请权限时，展示的对话框
     */
    var mDefaultPermissionDialog: ((context: Context, lifecycleOwner: LifecycleOwner) -> YcIDialog<*>?) = { context, lifecycleOwner ->
        YcDialogCommon(context, lifecycleOwner)
    }

    /**
     * 请求异常转替换布局状态
     */
    var mYcExceptionToSpecialState: (YcException) -> Int = {
        when (it.knownCode) {
            YcNetErrorCode.TIME_OUT_ERROR, YcNetErrorCode.NETWORK_NO -> {
                YcSpecialState.NETWORK_NO
            }

            YcNetErrorCode.DATE_NULL -> {
                YcSpecialState.DATA_EMPTY
            }

            YcNetErrorCode.DATE_NULL_ERROR -> {
                YcSpecialState.DATA_EMPTY_ERROR
            }

            YcNetErrorCode.JSON_ERROR, YcNetErrorCode.UN_KNOWN_ERROR, YcNetErrorCode.REQUEST_NULL -> {
                YcSpecialState.NETWORK_ERROR
            }

            else -> {
                YcSpecialState.NETWORK_ERROR
            }
        }
    }

    /**
     * 异常时会调用该方法（暂时只要网络请求出现异常时调用）
     * 返回true时，会强制跳出，不执行原有的逻辑
     */
    var mIsForceNoHandle: ((exception: YcException) -> Boolean)? = null

    /**
     * 默认保存文件夹路径
     */
    lateinit var mDefaultSaveDirPath: String

    lateinit var mApplication: Application

    fun init(app: Application, defaultSaveDirPath: String = app.filesDir.path + File.separator, hasLogShow: Boolean = BuildConfig.DEBUG) {
        mApplication = app
        mDefaultSaveDirPath = defaultSaveDirPath
        //Logger初始化
        XLog.init(LogLevel.ALL)
        x.Ext.init(mApplication)
        setLog(hasLogShow)
    }

    /**
     * 是否显示logger
     */
    fun setLog(isShow: Boolean) {
        YcLogExt.mIsShowLogger = isShow
        x.Ext.setDebug(isShow)
    }

    fun getResources(): Resources = mApplication.resources

    /**
     *检测异常时是否继续执行
     */
    inline fun isContinueWhenException(exception: YcException, crossinline execution: YcException.() -> Unit) {
        if (mIsForceNoHandle?.invoke(exception) != true) {
            execution.invoke(exception)
        }
    }

    /**
     *检测异常时是否继续执行（耗时的）
     */
    suspend inline fun isContinueWhenExceptionSuspend(exception: YcException, crossinline execution: suspend YcException.() -> Unit) {
        if (mIsForceNoHandle?.invoke(exception) != true) {
            execution.invoke(exception)
        }
    }
}