package com.example.better.editorScreen

import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout

class EditorActivityPresenter(_view: EditorContract.View) : EditorContract.Presenter {
    private val view: EditorContract.View = _view
    private var bottomBarAnimator: ValueAnimator? = null
    private val model: EditorContract.Model = EditorActivityModel()
    private lateinit var bitmapImage: Bitmap

    init {
        bitmapImage = view.getBitmap()
    }

    override fun onClickButtonOnBottomBar(customBar: CustomBar) {
        hideBottomBar()
        customBar.show()
    }

    override fun onRotate() {
        val rotateBar = RotateBar(view.getRotateBar())
        onClickButtonOnBottomBar(rotateBar)
    }

    override fun onRotateRight90() {
        bitmapImage = model.rotate(bitmapImage, 45f)
        view.setBitmap(bitmapImage)
    }

    private fun hideBottomBar() {
        val bottomBar = view.getBottomBar()
        bottomBarAnimator ?: initHideBottomBarValueAnimator(bottomBar)
        bottomBarAnimator?.setFloatValues(0f, bottomBar.height.toFloat())
        bottomBarAnimator?.start()
    }

    private fun showBottomBar() {
        val bottomBar = view.getBottomBar()
        bottomBarAnimator ?: initHideBottomBarValueAnimator(bottomBar)
        bottomBarAnimator?.setFloatValues(bottomBar.height.toFloat(), 0f)
        bottomBarAnimator?.start()
    }

    private fun initHideBottomBarValueAnimator(bottomBar: LinearLayout) {
        bottomBarAnimator = ValueAnimator()
        bottomBarAnimator?.setFloatValues()
        bottomBarAnimator?.addUpdateListener {
            val value = it.animatedValue as Float
            bottomBar.translationY = value
        }
        bottomBarAnimator?.interpolator = LinearInterpolator()
        bottomBarAnimator?.duration = 300
    }
}