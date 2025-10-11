package com.yc.ycmvvm.utils

import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.yc.ycmvvm.data.entity.YcRefreshHelperBo

class YcRefreshUtil(
    private val mSmartRefreshLayout: SmartRefreshLayout,
    private val mPageIndexDefault: Int = 1,
    private val mPageSizeDefault: Int = 10,
    private val hasRefresh: Boolean = true,
    private val hasMore: Boolean = true,
) {
    var mPageIndex: Int = 1
    var mPageSize: Int = 10
    var mPageSum: Int? = null
    fun resetPageData() {
        mPageIndex = mPageIndexDefault
        mPageSize = mPageSizeDefault
    }

    fun reset() {
        resetPageData()
        mPageIndex = mPageIndexDefault
        mPageSize = mPageSizeDefault
        mPageSum = null
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
            resetPageData()
            mSmartRefreshLayout.finishRefresh()
            mRefreshAndMoreCall?.invoke(YcRefreshHelperBo.RefreshState.Refresh, mPageIndex, mPageSize)
        }
        mSmartRefreshLayout.setOnLoadMoreListener {
            if (mPageSum == null || mPageIndex <= mPageSum!!) {
                mRefreshAndMoreCall?.invoke(YcRefreshHelperBo.RefreshState.LoadMore, mPageIndex, mPageSize)
            } else {
                mSmartRefreshLayout.finishRefresh()
                mSmartRefreshLayout.finishRefreshWithNoMoreData()
            }
        }
    }

    /**
     * 设置总数
     *
     * @param total
     */
    fun setTotal(total: Int) {
        mPageSum = (total + mPageSize - 1) / mPageSize
    }

    /**
     * 设置总数
     *
     * @param total
     */
    fun setPageSum(pageSum: Int) {
        mPageSum = pageSum
    }

    fun autoAdd() {
        if (mPageIndex <= mPageSum!!) {
            mPageIndex++;
        }
    }

    /**
     * 结束
     */
    fun finish() {
        mSmartRefreshLayout.finishLoadMore()
        mSmartRefreshLayout.finishRefresh()
    }


    fun refresh() {
        reset()
        if (hasRefresh) {
            mSmartRefreshLayout.autoRefresh()
        } else {
            resetPageData()
            mSmartRefreshLayout.finishRefresh()
            mRefreshAndMoreCall?.invoke(YcRefreshHelperBo.RefreshState.Refresh, mPageIndex, mPageSize)
        }
    }
}