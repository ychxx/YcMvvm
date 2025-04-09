package com.yc.ycmvvm.extension

import android.view.View

var CLICK_PRE_TIME = 0L
fun View.ycClickInvalid(call: (v: View) -> Unit) {
    this.setOnClickListener {
        if (System.currentTimeMillis() - CLICK_PRE_TIME < 500) {
            return@setOnClickListener
        }
        CLICK_PRE_TIME = System.currentTimeMillis()
        call.invoke(it)
    }
}