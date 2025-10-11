package com.yc.ycmvvm.data.entity

open class YcRefreshHelperBo<T>(
    /**
     * 操作
     */
    open val state: RefreshState,
    /**
     * 列表数据
     */
    open val list: List<T>?
) {
    sealed class RefreshState {
        data object Refresh : RefreshState()
        data object LoadMore : RefreshState()
    }
}