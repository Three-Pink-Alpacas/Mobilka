package com.example.better.editorScreen

import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class EditorActivityPresenter(_view: EditorContract.View) : EditorContract.Presenter {
    private val view: EditorContract.View = _view
    private var bottomBarAnimator: ValueAnimator? = null
    private val model: EditorContract.Model = EditorActivityModel()
    private var originalBitmapImage: Bitmap
    private var bitmapImage: Bitmap

    init {
        originalBitmapImage = view.getBitmap()
        val stream = ByteArrayOutputStream()
        val options = BitmapFactory.Options()
        options.inSampleSize = 6
        originalBitmapImage.compress(Bitmap.CompressFormat.PNG, 50, stream)
        bitmapImage = BitmapFactory.decodeStream(
            ByteArrayInputStream(stream.toByteArray()),
            Rect(0, 0, originalBitmapImage.width, originalBitmapImage.height),
            options
        )!!
    }

    override fun onClickButtonOnBottomBar(customBar: CustomBar) {
        hideBottomBar()
        customBar.show()
    }

    override fun onBlackAndWhiteFilter(){
        bitmapImage = model.blackAndWhiteFilter(bitmapImage)
        view.setBitmap(bitmapImage)
    }
    override fun onVioletFilter(){
        bitmapImage = model.violetFilter(bitmapImage)
        view.setBitmap(bitmapImage)
    }
    override fun onNegativeFilter(){
        bitmapImage = model.negativeFilter(bitmapImage)
        view.setBitmap(bitmapImage)
    }

    override fun onRotate() {
        val rotateBar = RotateBar(view.getRotateBar())
        onClickButtonOnBottomBar(rotateBar)
    }
    override fun onFilter() {
        val filterBar = FilterBar(view.getFilterBar())
        onClickButtonOnBottomBar(filterBar)
    }

    override fun onRotateRight90() {
        bitmapImage = model.rotate(bitmapImage, 90f)
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