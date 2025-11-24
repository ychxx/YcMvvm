package com.yc.ycmvvm.ui

import android.R.attr.description
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.yc.ycmvvm.base.YcBaseActivity
import com.yc.ycmvvm.databinding.TestChartAcBinding
import com.yc.ycmvvm.databinding.TestQrAcBinding
import com.yc.ycmvvm.extension.ycLogE
import com.yc.ycmvvm.extension.ycShowToast
import com.yc.ycmvvm.utils.YcColorUtil
import com.yc.ycmvvm.utils.YcResources


class TestChartAc : YcBaseActivity<TestChartAcBinding>(TestChartAcBinding::inflate) {
    companion object {
        fun toAc(context: Context) {
            context.startActivity(Intent(context, TestChartAc::class.java))
        }
    }

    override fun TestChartAcBinding.initView(savedInstanceState: Bundle?) {

    }

    fun TestChartAcBinding.load() {
        lineChart.apply {
            // background color
            setBackgroundColor(Color.WHITE)
            // disable description text
            description.isEnabled = false
            // enable touch gestures
            setTouchEnabled(true)
            // set listeners
            setDrawGridBackground(false)

//            // create marker to display box when values are selected
//            val mv: MyMarkerView = MyMarkerView(this, R.layout.custom_marker_view)
//
//
//            // Set the marker to the chart
//            mv.setChartView(chart)
//            chart.setMarker(mv)

            // enable scaling and dragging
            setDragEnabled(true)
            setScaleEnabled(false)

            // chart.setScaleXEnabled(true);
            // chart.setScaleYEnabled(true);
            // force pinch zoom along both axis
            setPinchZoom(false)

            val xAxis: XAxis = xAxis
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false) // 关闭X轴的竖线网格
                setDrawAxisLine(true) // 不绘制X轴线
                textSize = 28f
                textColor = YcResources.getColor("86909C")
                setAvoidFirstLastClipping(true)
                isEnabled = true
                xOffset = 50f // 根据需要调整这个值

            }
            val leftAxis: YAxis = axisLeft
            leftAxis.apply {
                isEnabled = true // 启用左Y轴
                setDrawAxisLine(false) // 不绘制Y轴线
                setDrawLabels(false) // 不显示Y轴标签（数值）

                // 设置水平网格线
                enableGridDashedLine(1f, 5f, 0f) // 虚线网格
                gridColor = YcResources.getColor("676767") // 网格线颜色
                gridLineWidth = 1f // 网格线宽度

                // 设置Y轴范围（根据你的数据调整）
                axisMinimum = 0f
                axisMaximum = 200f // 根据你的数据范围调整
            }
            val rightAxis: YAxis = axisRight
            rightAxis.apply {
                isEnabled = false
            }
            // draw limit lines behind data instead of on top
            leftAxis.setDrawLimitLinesBehindData(true)
            xAxis.setDrawLimitLinesBehindData(true)
            val xAxisLabels = listOf(
                "10-21", "10-22", "10-23", "10-24", "10-25",
                "10-26", "10-27"
            )
            setData2(7, 180f, xAxisLabels)
            // draw points over time
            animateX(1500)
            // get the legend (only possible after setting data)
            val l: Legend = legend
            // draw legend entries as lines
            l.form = Legend.LegendForm.SQUARE
            setDrawMarkers(true)

        }
    }

    private fun TestChartAcBinding.setData2(count: Int, range: Float, xAxisLabels: List<String>) {
        val values1 = ArrayList<Entry>()
        val values2 = ArrayList<Entry>()

        // 生成与标签数量相同的数据点
        for (i in 0 until count) {
            val val1 = (Math.random() * range).toFloat() - 30
            values1.add(Entry(i.toFloat(), val1)) // X值从0开始，对应标签列表的索引

            val val2 = (Math.random() * range).toFloat() - 15
            values2.add(Entry(i.toFloat(), val2)) // X值从0开始，对应标签列表的索引
        }

        lineChart.apply {
            // 配置 X 轴
            xAxis.apply {
                valueFormatter = LabelXAxisFormatter(xAxisLabels)
                setLabelCount(xAxisLabels.size, false)

                // 根据标签数量调整显示密度
                if (xAxisLabels.size > 7) {
                    setLabelCount(7, false) // 只显示部分标签
                    setLabelRotationAngle(45f) // 旋转标签避免重叠
                } else {
                    setLabelCount(xAxisLabels.size, false) // 显示所有标签
                    setLabelRotationAngle(0f) // 不旋转
                }
            }


            if (data != null && data.dataSetCount > 0) {
                // 更新现有数据集
                val set1 = data.getDataSetByIndex(0) as LineDataSet
                val set2 = data.getDataSetByIndex(1) as LineDataSet

                set1.values = values1
                set2.values = values2

                set1.notifyDataSetChanged()
                set2.notifyDataSetChanged()
                data.notifyDataChanged()
                notifyDataSetChanged()
            } else {
                // 创建第一个数据集
                val set1 = LineDataSet(values1, "数据集1")
                set1.setDrawIcons(false)
                set1.mode = LineDataSet.Mode.CUBIC_BEZIER

                // 默认不显示圆点和数值
                set1.setDrawCircles(false)
                set1.setDrawValues(false)

                // 点击时显示圆点
                set1.setDrawHighlightIndicators(true)
                set1.setDrawHorizontalHighlightIndicator(false) // 只显示垂直高亮线
                set1.highLightColor = YcResources.getColor("979797")
                set1.setCircleColor(YcResources.getColor("5B8FF9"))
                set1.setDrawCircleHole(true)
                set1.circleRadius = 20f
                set1.setCircleColor(YcResources.getColor("5B8FF9"))
                set1.circleHoleColor = YcResources.getColor("5B8FF9")
                set1.circleHoleRadius = 20f

                set1.color = YcResources.getColor("5B8FF9")
                set1.lineWidth = 2f
                set1.valueTextSize = 9f
                set1.setDrawFilled(true)
                set1.fillFormatter = IFillFormatter { dataSet, dataProvider -> axisLeft.axisMinimum }
                set1.fillColor = YcResources.getColor("5B8FF9")

                // 创建第二个数据集
                val set2 = LineDataSet(values2, "数据集2")
                set2.setDrawIcons(false)
                set2.mode = LineDataSet.Mode.CUBIC_BEZIER

                // 默认不显示圆点和数值
                set2.setDrawCircles(false)
                set2.setDrawValues(false)

                // 点击时显示圆点
                set2.setDrawHighlightIndicators(true)
                set2.setDrawHorizontalHighlightIndicator(false)
                set2.highLightColor = YcResources.getColor("979797")
                set2.setCircleColor(YcResources.getColor("5ED8AA"))
                set2.circleRadius = 20f
                set2.setDrawCircleHole(true)
                set2.setCircleColor(YcResources.getColor("5ED8AA"))
                set2.circleHoleColor = YcResources.getColor("5ED8AA")
                set2.circleHoleRadius = 20f

                set2.color = YcResources.getColor("5ED8AA")
                set2.lineWidth = 2f
                set2.valueTextSize = 9f
                set2.setDrawFilled(true)
                set2.fillFormatter = IFillFormatter { dataSet, dataProvider -> axisLeft.axisMinimum }
                set2.fillColor = YcResources.getColor("5ED8AA")


                val dataSets = ArrayList<ILineDataSet>()
                dataSets.add(set1)
                dataSets.add(set2)

                val data = LineData(dataSets)
                setData(data)
            }
//            marker = CustomMarkerView(this@TestChartAc, R.layout.custom_marker_view, this, xAxisLabels)
            // 重要：刷新图表
            invalidate()
        }
    }

    class LabelXAxisFormatter(private val labels: List<String>) : ValueFormatter() {

        override fun getFormattedValue(value: Float): String {
            return labels[(value + 0.9).toInt()]
        }
    }
}
