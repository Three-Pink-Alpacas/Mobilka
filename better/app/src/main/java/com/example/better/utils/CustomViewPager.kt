package com.example.better.utils

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

import androidx.viewpager.widget.ViewPager


class CustomViewPager(context: Context?, attrs: AttributeSet?) : ViewPager(context!!, attrs) {
    var enbld = true


    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (enbld) {
            super.onTouchEvent(event)
        } else false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (enbld) {
            super.onInterceptTouchEvent(event)
        } else false
    }

    fun setPagingEnabled(enabled: Boolean) {
        this.enbld = enabled
    }

}