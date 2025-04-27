package com.yc.ycmvvm.view.spinner

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ListView
import androidx.appcompat.widget.ListPopupWindow

open class YcSpinner @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs), YcISpinner {
    override fun getViewGroup(): ViewGroup {
        return this
    }

    override var mAdapter: YcISpinnerAdapter<*, *, *>? = null

    private val mPop by lazy { DropdownPopup(this, context, attrs, 0) }
    override fun showDropdown() {
        mPop.show()
    }

    override fun dismissDropdown() {
        mPop.dismiss()
    }

    override fun hasDropdownShowing(): Boolean {
        return mPop.isShowing
    }

    override fun setAdapter(adapter: YcISpinnerAdapter<*, *, *>) {
        mAdapter = adapter
        adapter.onAttachedToSpinnerView(this)
        removeAllViews()
        addView(adapter.getSelectItemView().root, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        mPop.setAdapter(adapter.mDropDownAdapter)
    }

    class DropdownPopup(private val spinnerView: YcSpinner, context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
        ListPopupWindow(context!!, attrs, defStyleAttr) {
        init {
            anchorView = spinnerView
            isModal = true
            width = spinnerView.width
            promptPosition = POSITION_PROMPT_BELOW
        }


        private fun computeContentWidth() {
            setContentWidth(spinnerView.width)
        }

        override fun show() {
            computeContentWidth()
            inputMethodMode = INPUT_METHOD_NOT_NEEDED
            super.show()
            val listView = listView
            listView!!.choiceMode = ListView.CHOICE_MODE_SINGLE
            spinnerView.mAdapter?.getSelectedItemPosition()?.let {
                setSelection(it)
            }
        }
    }
}