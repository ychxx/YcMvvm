package com.yc.ycmvvm.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.yc.ycmvvm.base.YcBaseActivity
import com.yc.ycmvvm.databinding.PickerDoubleTimeBinding
import com.yc.ycmvvm.databinding.PickerViewAcBinding
import com.yc.ycmvvm.extension.ycShowToast
import java.util.Calendar


class TestPickerViewAc : YcBaseActivity<PickerViewAcBinding>(PickerViewAcBinding::inflate) {
    companion object {
        fun toAc(context: Context) {
            context.startActivity(Intent(context, TestPickerViewAc::class.java))
        }
    }

    override fun PickerViewAcBinding.initView(savedInstanceState: Bundle?) {
        initCustomTimePicker()
        testBtn1.setOnClickListener {
            pvCustomTime.show()
        }
    }

    lateinit var pvCustomTime: TimePickerView
    private fun initCustomTimePicker() {
        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        val selectedDate: Calendar = Calendar.getInstance() //系统当前时间
        val startDate: Calendar = Calendar.getInstance()
        startDate.set(2014, 1, 23)
        val endDate: Calendar = Calendar.getInstance()
        endDate.set(2027, 2, 28)
        //时间选择器 ，自定义布局
        pvCustomTime = TimePickerBuilder(this) { date, v -> //选中事件回调
            ycShowToast("选择时间${date}")
        }.setDate(selectedDate)
            .setRangDate(startDate, endDate)
//            .setLayoutRes(R.layout.pickerview_custom_time) { v ->
//                val tvSubmit = v.findViewById<View>(R.id.tv_finish) as TextView
//                val ivCancel = v.findViewById<View>(R.id.iv_cancel) as ImageView
//                tvSubmit.setOnClickListener {
//                    pvCustomTime.returnData()
//                    pvCustomTime.dismiss()
//                }
//                ivCancel.setOnClickListener { pvCustomTime.dismiss() }
//            }
            .setContentTextSize(18)
            .setType(booleanArrayOf(false, false, false, true, true, true))
            .setLabel("年", "月", "日", "时", "分", "秒")
            .setLineSpacingMultiplier(1.2f)
            .setTextXOffset(0, 0, 0, 40, 0, -40)
            .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
            .setDividerColor(-0xdb5263)
            .build()
    }

}
