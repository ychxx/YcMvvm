package com.yc.ycmvvm.view.spinner

/**
 * 用于存储选择的数据
 */
data class YcSpSelectBo<T>(
    var list: List<T>? = listOf(),
    var selectItem: T? = null,
    var selectIndex: Int? = null,
) {

    val hasSelect: Boolean
        get() {
            return selectItem != null || selectIndex != null
        }
}