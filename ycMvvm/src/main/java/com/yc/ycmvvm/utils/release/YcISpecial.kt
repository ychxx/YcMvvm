package com.yc.ycmvvm.utils.release

import com.yc.ycmvvm.exception.YcException


/**
 *
 */
interface YcISpecial {
    /**
     * 设置类型
     *
     * @param specialState
     */
    fun setSpecialState(@YcSpecialState specialState: Int)

    /**
     * 显示
     */
    fun show(@YcSpecialState specialState: Int, exception: YcException? = null)

    /**
     * 恢复
     */
    fun recovery()

    fun onUpdate(@YcSpecialState specialState: Int, exception: YcException? = null)
}