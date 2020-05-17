package com.example.better.editorScreen

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import com.example.better.R
import com.example.better.utils.OnMoveTouchListener
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
class EditorActivityPresenter(_view: EditorContract.View) : EditorContract.Presenter {
    private val view: EditorContract.View = _view
    private var bottomBarAnimator: ValueAnimator? = null
    private val model: EditorContract.Model = EditorActivityModel()
    private var originalBitmapImage: Bitmap
    private var bitmapImage: Bitmap
    private val onTouchListener: View.OnTouchListener

    private var imageRotation: Float = 0f

    init {
        originalBitmapImage = view.getBitmap()
        val stream = ByteArrayOutputStream()
        val options = BitmapFactory.Options()
        options.inSampleSize = 6
        originalBitmapImage.compress(Bitmap.CompressFormat.PNG, 50, stream)
        bitmapImage = BitmapFactory.decodeStream(
            ByteArrayInputStream(stream.toByteArray()), view.getImageView()?.clipBounds,
            options
        )!!

        view.setBitmap(bitmapImage)

        onTouchListener = object : OnMoveTouchListener() {
            override fun onMoveLeft(diff: Float) {
                changeImageRotation(-diff)
            }

            override fun onMoveRight(diff: Float) {
                changeImageRotation(diff)
            }

            override fun onMoveTop(diff: Float) {
                changeImageRotation(-diff)
            }

            override fun onMoveBottom(diff: Float) {
                changeImageRotation(diff)
            }
        }
    }

    override fun onClickButtonOnBottomBar(customBar: CustomBar) {
        hideBottomBar()
        customBar.show()
    }

    override fun onBlackAndWhiteFilter() {
        bitmapImage = model.blackAndWhiteFilter(bitmapImage)
        view.setBitmap(bitmapImage)
    }

    override fun onVioletFilter() {
        bitmapImage = model.violetFilter(bitmapImage)
        view.setBitmap(bitmapImage)
    }

    override fun onNegativeFilter() {
        bitmapImage = model.negativeFilter(bitmapImage)
        view.setBitmap(bitmapImage)
    }

    override fun onRotate() {
        val rotateBar = RotateBar(view.getRotateBar())
        onClickButtonOnBottomBar(rotateBar)
        view.setOnTouchListener(onTouchListener)
    }

    override fun onFilter() {
        val filterBar = FilterBar(view.getFilterBar())
        onClickButtonOnBottomBar(filterBar)
    }

    override fun onRotateRight90() {
        bitmapImage = model.rotate(bitmapImage, 90f)
        view.setBitmap(bitmapImage)
    }

    private fun changeImageRotation(diff: Float) {
        when {
            imageRotation + diff / 10 > 45f -> {
                imageRotation = 45f
                view.setImageRotation(imageRotation)
            }
            imageRotation + diff / 10 < -45f -> {
                imageRotation = -45f
                view.setImageRotation(imageRotation)
            }
            else -> {
                imageRotation += diff / 10
                view.setImageRotation(imageRotation)
            }
        }
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