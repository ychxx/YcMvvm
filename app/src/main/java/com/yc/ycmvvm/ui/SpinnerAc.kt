package com.yc.ycmvvm.ui


import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.yc.ycmvvm.base.YcBaseActivity
import com.yc.ycmvvm.databinding.SpinnerAcBinding
import com.yc.ycmvvm.extension.ycLogE

class SpinnerAc : YcBaseActivity<SpinnerAcBinding>(SpinnerAcBinding::inflate) {
    override fun SpinnerAcBinding.initView(savedInstanceState: Bundle?) {
        val adapter =object :ArrayAdapter<String>(this@SpinnerAc, android.R.layout.simple_spinner_item){
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                (view as TextView).apply {
                    setTextColor(Color.RED)
                    textSize = 26f
                }
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                return super.getDropDownView(position, convertView, parent).apply {
                    layoutParams = layoutParams.apply {
                        width = ViewGroup.LayoutParams.MATCH_PARENT
                    }
                }
            }

        }
        adapter.addAll(listOf("11","112","113","114","115","116"))
        sp.adapter = adapter
        ycLogE("222222222")
    }

}