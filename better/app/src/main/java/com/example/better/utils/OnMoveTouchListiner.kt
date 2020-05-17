package com.example.better.utils

import android.content.Context
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import kotlin.math.abs

open class OnMoveTouchListener : OnTouchListener {
    private var startX: Float = 0f
    private var startY: Float = 0f

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_UP -> {
                v?.performClick()
                return true
            }
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val diffX = event.x - startX
                val diffY = event.y - startY
                startX = event.x
                startY = event.y
                if (abs(diffX) > abs(diffY)) {
                    if (diffX < 0) {
                        onMoveLeft(abs(diffX))
                    } else {
                        onMoveRight(diffX)
                    }
                } else {
                    if (diffY < 0) {
                        onMoveTop(abs(diffY))
                    } else {
                        onMoveBottom(diffY)
                    }
                }
                return true
            }
        }
        return false
    }

    open fun onMoveLeft(diff: Float) {}
    open fun onMoveRight(diff: Float) {}
    open fun onMoveTop(diff: Float) {}
    open fun onMoveBottom(diff: Float) {}
}