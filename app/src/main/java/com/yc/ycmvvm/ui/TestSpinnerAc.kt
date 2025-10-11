package com.yc.ycmvvm.ui


import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.yc.ycmvvm.R
import com.yc.ycmvvm.adapter.YcRecyclerViewAdapter
import com.yc.ycmvvm.base.YcBaseActivity
import com.yc.ycmvvm.databinding.SpinnerAcBinding
import com.yc.ycmvvm.databinding.TestSpinnerMoreSelectDropItemBinding
import com.yc.ycmvvm.databinding.TestSpinnerMoreSelectItemBinding
import com.yc.ycmvvm.databinding.TestSpinnerMoreSelectItemItemBinding
import com.yc.ycmvvm.extension.ycLogE
import com.yc.ycmvvm.extension.ycShowToast
import com.yc.ycmvvm.view.spinner.YcSpinnerMoreSelectAdapter


class TestSpinnerAc : YcBaseActivity<SpinnerAcBinding>(SpinnerAcBinding::inflate) {
    companion object {
        fun toAc(context: Context) {
            context.startActivity(Intent(context, TestSpinnerAc::class.java))
        }
    }

    private val mCommonSpMoreAd = object : YcSpinnerMoreSelectAdapter<String, TestSpinnerMoreSelectItemBinding, TestSpinnerMoreSelectDropItemBinding>(
        TestSpinnerMoreSelectItemBinding::inflate,
        TestSpinnerMoreSelectDropItemBinding::inflate
    ) {
        override var mDropDownShowCall: (TestSpinnerMoreSelectItemBinding.(hasShow: Boolean) -> Unit)? = {
            spinnerItemIv.text = if (it) "收起" else "展开"
        }
        val itemAdapter = YcRecyclerViewAdapter<String, TestSpinnerMoreSelectItemItemBinding>(TestSpinnerMoreSelectItemItemBinding::inflate).apply {
            mOnUpdate = {
                spinnerItemIv.text = it
            }
        }
        override var mSelectItemOnUpdate: (TestSpinnerMoreSelectItemBinding.(List<Int>?, List<String>?) -> Unit)? = { position, selectItem ->
            spinnerItemRv.layoutManager = FlexboxLayoutManager(spinnerItemRv.context).apply {
                flexWrap = FlexWrap.WRAP
                flexDirection = FlexDirection.ROW
                alignItems = AlignItems.STRETCH
            }
            if (spinnerItemRv.adapter == null)
                spinnerItemRv.adapter = itemAdapter
            itemAdapter.addAllData(selectItem, true)
        }
        override var mDropDownOnUpdate: (TestSpinnerMoreSelectDropItemBinding.(Int, String, Boolean) -> Unit)? = { _, it, hasSelect ->
            spaceSpDropCtv.isLongClickable = false
            spaceSpDropCtv.isChecked = hasSelect
            spaceSpDropCtv.isSelected = hasSelect
            spaceSpDropCtv.text = it
//            spaceSpDropCtv.setBackgroundResource(if (hasSelect) R.color.yc_blue else R.color.yc_white)
        }
        override var mSelectLimit: ((Int, String) -> Boolean)? = { _, _ ->
            val selectItem = this.getSelectedItemPosition()?.size ?: 0
            if (selectItem >= 3) {
                ycShowToast("最多只能选择3个标签")
                false
            } else {
                true
            }
        }
    }

    override fun SpinnerAcBinding.initView(savedInstanceState: Bundle?) {
        ycLogE("222222222")
        moreSp.setAdapter(mCommonSpMoreAd)
        mCommonSpMoreAd.addAllData(
            listOf(
                "标签1",
                "标签2标签2标签2标签2标签2",
                "标签3标签3",
                "标签4",
                "标签标签标签标签标签标签标签标签5",
                "标签标签标签标签标签6",
                "标签标签7",
                "标签8",
                "标签标签标签标签标签标签标签标签标签标签标签标签标签9",
                "标签标签10",
                "标签标签标签11",
                "标签标签12",
                "标签标签13",
                "标签14",
                "标签标签标签标签15",
                "标签标签标签标签标签标签标签标签16",
                "标签标签标签标签标签标签标签标签标签标签标签17",
                "标签标签标签标签标签标签标签标签标签标签标签标签标签标签标签标签标签标签标签标签标签标签标签标签18",
                "标签19",
                "标签20",
                "标签21",
                "标签标签标签22",
                "标签标签标签23",
                "标签标签标签标签标签24",
                "标签标签标签标签标签25",
                "标签26",
                "标签标签27",
                "标签标签标签28",
                "标签标签标签标签29",
                "标签标签标签标签标签30",
                "标签标签标签标签标签标签31"
            )
        )
    }

}