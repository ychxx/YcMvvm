package com.yc.ycmvvm.utils

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.lang.ref.WeakReference
import java.util.*

/**
 * Creator: yc
 * Date: 2021/2/8 15:09
 * UseDes:Activity管理类
 */
object YcActivityManager {
    private val mActivityStack: Stack<WeakReference<FragmentActivity>> by lazy {
        Stack<WeakReference<FragmentActivity>>()
    }

    /**
     * 添加一个Activity到堆栈中
     */
    @JvmStatic
    fun addActivity(activity: FragmentActivity) {
        activity.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                cleanStack()
            }
        })
        mActivityStack.push(WeakReference(activity))
    }

    /**
     * 清理已被回收的Activity引用
     */
    private fun cleanStack() {
        val iterator = mActivityStack.iterator()
        while (iterator.hasNext()) {
            val ref = iterator.next()
            // 移除已被GC回收或Activity已销毁的引用
            if (ref.get() == null || ref.get()?.isDestroyed == true) {
                iterator.remove()
            }
        }
    }

    /**
     * 从堆栈中移除指定的Activity
     */
    @JvmStatic
    fun finishActivity(activity: Class<FragmentActivity>) {
        mActivityStack.forEach { ref ->
            ref.get()?.takeIf { it::class.java == activity }?.finish()
        }
    }

    @JvmStatic
    fun finishActivity(activity: FragmentActivity) {
        try {
            mActivityStack.forEach { ref ->
                ref.get()?.takeIf { it == activity }?.finish()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    /**
     * 从堆栈中移除所有Activity
     */
    @JvmStatic
    fun finishAllActivity() {
        mActivityStack.forEach { ref ->
            ref.get()?.finish()
        }
        mActivityStack.clear()
    }

    /**
     * 结束除当前传入以外所有Activity
     */
    @JvmStatic
    fun <T> finishOthersActivity(activityClass: Class<T>) {
        mActivityStack.forEach { ref ->
            ref.get()?.takeIf { it.javaClass != activityClass }?.finish()
        }
    }

    /**
     * 获取到当前显示Activity（堆栈中最后一个传入的activity）
     */
    @JvmStatic
    fun getCurrentActivity(): FragmentActivity? {
        return mActivityStack.lastOrNull { ref ->
            ref.get() != null && !ref.get()!!.isDestroyed
        }?.get()
    }

    @JvmStatic
    fun <T> getActivity(activityClass: Class<T>): FragmentActivity? {
        return mActivityStack.find { it.get()?.javaClass == activityClass }?.get()
    }

    /**
     * 关闭当前activity
     */
    @JvmStatic
    fun finishCurrentActivity() {
        mActivityStack.pop()?.get()?.finish()
    }

    /**
     * 是否存在 activity
     */
    @JvmStatic
    fun <T> hasExist(activityClass: Class<T>): Boolean {
        return mActivityStack.any { ref ->
            ref.get()?.javaClass == activityClass
        }
    }

    /**
     * 退出app
     */
    @JvmStatic
    fun existApp() {
        try {
            finishAllActivity()
            android.os.Process.killProcess(android.os.Process.myPid())
        } catch (_: Exception) {
        }
    }
}