package com.yc.ycmvvm.utils

import com.scwang.smart.refresh.layout.SmartRefreshLayout

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
    fun reset() {
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

    var mRefreshAndMoreCall: ((pageIndex: Int) -> Unit)? = null

    init {
        reset()
        mSmartRefreshLayout.setOnRefreshListener {
            mSmartRefreshLayout.finishRefresh()
            mRefreshAndMoreCall?.invoke(mPageIndex)
        }
        mSmartRefreshLayout.setOnLoadMoreListener {
            if (mPageSum == null || mPageIndex <= mPageSum!!) {
                mRefreshAndMoreCall?.invoke(mPageIndex)
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
        mSmartRefreshLayout.autoRefresh()
    }
}