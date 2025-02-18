package com.yc.ycmvvm.view.platenum

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager.LayoutParams
import android.widget.PopupWindow
import androidx.recyclerview.widget.GridLayoutManager
import com.yc.ycmvvm.adapter.YcRecyclerViewAdapter
import com.yc.ycmvvm.data.constans.YcPlateNumData
import com.yc.ycmvvm.data.constans.YcPlateNumData.PlateNumKeyboardType
import com.yc.ycmvvm.databinding.YcPlateNumKeyboardBinding
import com.yc.ycmvvm.databinding.YcPlateNumKeyboardItemBinding

class YcPlateNumKeyboardPopup(context: Context) {
    private val mPopupWindow: PopupWindow
    private val mViewBinding: YcPlateNumKeyboardBinding
    private val mAdapter = YcRecyclerViewAdapter<Char, YcPlateNumKeyboardItemBinding>(YcPlateNumKeyboardItemBinding::inflate).apply {
        mOnUpdate = { data ->
            keyboardBtn.text = data.toString()
        }
        mItemClick = {
            mClick?.invoke(it.toString(), YcPlateNumData.PLATE_NUM_KEYBOARD_TYPE_DATA)
        }
    }

    init {
        mViewBinding = YcPlateNumKeyboardBinding.inflate(LayoutInflater.from(context))
        mViewBinding.keyboardRv.adapter = mAdapter
        mViewBinding.keyboardRv.layoutManager = GridLayoutManager(context, 8)
        mViewBinding.deleteBtn.setOnClickListener {
            mClick?.invoke("", YcPlateNumData.PLATE_NUM_KEYBOARD_TYPE_DEL)
        }
        mViewBinding.closeBtn.setOnClickListener {
            this@YcPlateNumKeyboardPopup.keyboardPopupHide()
        }
        mPopupWindow = PopupWindow(mViewBinding.root, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }

    var mClick: ((content: String, type: @PlateNumKeyboardType String) -> Unit)? = null
    private var mPlateNumIndex = 0
    fun keyboardPopupShow(view: View) {
        mPopupWindow.showAsDropDown(view, Gravity.BOTTOM, 0, 0)
    }

    public fun keyboardPopupHide() {
        mPopupWindow.dismiss()
    }

    fun updateKeyboardData(plateNumIndex: Int) {
        mPlateNumIndex = plateNumIndex
        when (plateNumIndex) {
            0 -> {
                mAdapter.addAllData(YcPlateNumData.PLATE_NUM_CONTENT_PROVINCE.toList())
            }

            1 -> {
                mAdapter.addAllData(YcPlateNumData.PLATE_NUM_CONTENT_LETTER.toList())
            }

            6, 7 -> {
                mAdapter.addAllData((YcPlateNumData.PLATE_NUM_CONTENT_DIGIT + YcPlateNumData.PLATE_NUM_CONTENT_LETTER + YcPlateNumData.PLATE_NUM_CONTENT_SPECIAL).toList())
            }

            else -> {
                mAdapter.addAllData((YcPlateNumData.PLATE_NUM_CONTENT_DIGIT + YcPlateNumData.PLATE_NUM_CONTENT_LETTER).toList())
            }
        }
    }
//    popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
}
