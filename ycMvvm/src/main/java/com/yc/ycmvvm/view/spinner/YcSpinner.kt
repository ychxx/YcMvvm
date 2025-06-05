package com.yc.ycmvvm.view.spinner

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ListView
import androidx.appcompat.widget.ListPopupWindow
import com.yc.ycmvvm.R
import com.yc.ycmvvm.exception.YcException
import com.yc.ycmvvm.extension.ycLogE

open class YcSpinner @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs), YcISpinner {
    init {
        setPadding(0, 0, 0, 0)
    }

    override fun getViewGroup(): ViewGroup {
        return this
    }

    override var mAdapter: YcISpinnerAdapterBase<*, *, *>? = null

    private val mPop by lazy {
        DropdownPopup(this, context, attrs, 0)
    }

    override fun showDropdown() {
        ycLogE("showDropdown" + mPop.anchorView!!.height)
        ycLogE("SelectItemView" + mAdapter!!.getSelectItemView().root.height)
        mPop.show()
    }

    override fun dismissDropdown() {
        mPop.dismiss()
    }

    override fun hasDropdownShowing(): Boolean {
        return mPop.isShowing
    }

    override fun setAdapter(adapter: YcISpinnerAdapterBase<*, *, *>) {
        mAdapter = adapter
        adapter.onAttachedToSpinnerView(this)
        removeAllViews()
        addView(adapter.getSelectItemView().root, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        mPop.setAdapter(adapter.getListAdapter())
        this.post {
            mPop.setDropDownGravity(Gravity.BOTTOM)
            mPop.computeContentWidth()
//            mPop.verticalOffset = mPop.anchorView!!.height
        }
    }

    class DropdownPopup(private val spinnerView: YcSpinner, context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
        ListPopupWindow(context!!, attrs, defStyleAttr) {

        init {
            anchorView = spinnerView
            isModal = true
        }


        fun computeContentWidth() {
            setContentWidth(spinnerView.width)
        }

        override fun show() {
            inputMethodMode = INPUT_METHOD_NEEDED
            super.show()
        }

    }
}