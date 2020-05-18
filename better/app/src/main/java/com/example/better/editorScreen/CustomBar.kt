package com.example.better.editorScreen

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import androidx.constraintlayout.widget.ConstraintLayout

class CustomBar(val layout: ConstraintLayout, private val type: Type) {
    enum class Type {
        TOP, BOTTOM
    }

    private val animator: ValueAnimator = ValueAnimator()

    init {
        animator.addUpdateListener {
            val value = it.animatedValue as Float
            layout.translationY = value
        }
        animator.interpolator = LinearInterpolator()
        animator.duration = 300
    }

    fun hide() {
        when (type) {
            Type.BOTTOM -> animator.setFloatValues(0f, layout.height.toFloat())
            Type.TOP -> animator.setFloatValues(0f, -layout.height.toFloat())
        }
        animator.start()
    }

    fun show() {
        when (type) {
            Type.TOP -> animator.setFloatValues(layout.translationY, 0f)
            Type.BOTTOM -> animator.setFloatValues(layout.translationY, 0f)
        }
        animator.start()
    }

}