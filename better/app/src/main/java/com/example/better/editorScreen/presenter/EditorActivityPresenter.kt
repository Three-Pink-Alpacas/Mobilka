package com.example.better.editorScreen.presenter

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import com.example.better.editorScreen.contract.EditorContract

class EditorActivityPresenter(_view: EditorContract.View) : EditorContract.Presenter {
    private val view: EditorContract.View = _view
    private var hideBottomBarAnimator: ValueAnimator? = null

    override fun onClickButtonOnBottomBar() {
        hideBottomBar()
    }

    private fun hideBottomBar() {
        hideBottomBarAnimator?: initValueAnimator()
        hideBottomBarAnimator?.start()
    }

    private fun initValueAnimator() {
        val bottomBar = view.getBottomBar()
        println(bottomBar.height)
        hideBottomBarAnimator = ValueAnimator.ofFloat(0f, bottomBar.height.toFloat())
        hideBottomBarAnimator?.setFloatValues()
        hideBottomBarAnimator?.addUpdateListener {
            val value = it.animatedValue as Float
            bottomBar.translationY = value
        }
        hideBottomBarAnimator?.interpolator = LinearInterpolator()
        hideBottomBarAnimator?.duration = 300
    }
}