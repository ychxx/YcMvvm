package com.yc.ycmvvm.utils

import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.yc.ycmvvm.data.entity.YcRefreshHelperBo

/**
 *  用于没有总页数的情况
 */
class YcRefreshNoSumUtil(
    private val mSmartRefreshLayout: SmartRefreshLayout,
    private val mPageIndexDefault: Int = 1,
    private val mPageSizeDefault: Int = 20,
    private val hasRefresh: Boolean = true,
    private val hasMore: Boolean = true,
) {

    var mPageIndex: Int = 1
    var mPageSize: Int = 20
    fun resetPageData() {
        mPageIndex = mPageIndexDefault
        mPageSize = mPageSizeDefault
    }

    fun reset() {
        resetPageData()
        if (hasRefresh) {
            mSmartRefreshLayout.setEnableRefresh(true)
            mSmartRefreshLayout.finishRefresh()
        } else {
            mSmartRefreshLayout.setEnableRefresh(false)
        }
        if (hasMore) {
            mSmartRefreshLayout.setEnableLoadMore(true)
            mSmartRefreshLayout.finishLoadMore()
            mSmartRefreshLayout.setNoMoreData(false)
        } else {
            mSmartRefreshLayout.setEnableLoadMore(false)
        }

    }

    var mRefreshAndMoreCall: ((state: YcRefreshHelperBo.RefreshState, pageIndex: Int, pageSize: Int) -> Unit)? = null

    init {
        reset()
        mSmartRefreshLayout.setOnRefreshListener {
            mSmartRefreshLayout.finishLoadMore()
            resetPageData()
            mRefreshAndMoreCall?.invoke(YcRefreshHelperBo.RefreshState.Refresh, mPageIndex, mPageSize)
        }
        mSmartRefreshLayout.setOnLoadMoreListener {
            mRefreshAndMoreCall?.invoke(YcRefreshHelperBo.RefreshState.LoadMore, mPageIndex, mPageSize)
        }
    }

    fun <T> setResult(list: List<T>?) {
        val size = list?.size ?: 0
        if (size < mPageSize) {
            setNoMoreData()
        } else {
            autoAdd()
            finish()
        }
    }

    /**
     * 设置没有更多数据
     */
    fun setNoMoreData() {
        mSmartRefreshLayout.finishRefreshWithNoMoreData()
        finish()
    }


    /**
     * 结束
     */
    fun finish() {
        mSmartRefreshLayout.finishLoadMore()
        mSmartRefreshLayout.finishRefresh()
    }


    fun autoAdd() {
        mPageIndex++
    }

    fun refresh() {
        reset()
        mSmartRefreshLayout.autoRefresh()
    }
}