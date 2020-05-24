package com.example.better.utils

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
                        onMoveLeft(abs(diffX), event.x, event.y)
                    } else {
                        onMoveRight(diffX, event.x, event.y)
                    }
                } else {
                    if (diffY < 0) {
                        onMoveTop(abs(diffY), event.x, event.y)
                    } else {
                        onMoveBottom(diffY, event.x, event.y)
                    }
                }
                return true
            }
        }
        return false
    }

    open fun onMoveLeft(diff: Float, x: Float, y: Float) {}
    open fun onMoveRight(diff: Float, x: Float, y: Float) {}
    open fun onMoveTop(diff: Float, x: Float, y: Float) {}
    open fun onMoveBottom(diff: Float, x: Float, y: Float) {}
}