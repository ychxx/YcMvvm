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
import com.yc.ycmvvm.extension.YcLogExt
import com.yc.ycmvvm.net.YcRetrofitUtil
import com.yc.ycmvvm.utils.release.YcSpecialViewConfigureBase
import com.yc.ycmvvm.view.dialog.YcDialogCommon
import com.yc.ycmvvm.view.dialog.YcIDialog
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
     * 默认保存文件夹路径
     */
    lateinit var mDefaultSaveDirPath: String

    lateinit var mApplication: Application

    fun initAll(
        app: Application,
        defaultSaveDirPath: String = app.filesDir.path + File.separator,
        hasLogShow: Boolean = BuildConfig.DEBUG,
        retrofitConfig: YcRetrofitUtil.YcRetrofitConfig
    ) {
        initImmediately(app, defaultSaveDirPath, retrofitConfig)
        initLazy(hasLogShow)
    }

    /**
     * 需在onCreate里初始化的
     */
    fun initImmediately(app: Application, defaultSaveDirPath: String = app.filesDir.path + File.separator, retrofitConfig: YcRetrofitUtil.YcRetrofitConfig) {
        YcRetrofitUtil.defaultConfig = retrofitConfig
        mApplication = app
        mDefaultSaveDirPath = defaultSaveDirPath
    }

    /**
     * 可延迟初始化的
     */
    fun initLazy(hasLogShow: Boolean = BuildConfig.DEBUG) {
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


}