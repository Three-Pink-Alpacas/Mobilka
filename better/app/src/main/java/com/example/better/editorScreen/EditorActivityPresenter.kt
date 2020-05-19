package com.example.better.editorScreen

import android.graphics.Bitmap
import android.os.Build
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.better.R
import com.example.better.utils.CustomBar
import com.example.better.utils.OnMoveTouchListener

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
class EditorActivityPresenter(_view: EditorContract.View) : EditorContract.Presenter {
    private val view: EditorContract.View = _view
    private val model: EditorContract.Model = EditorActivityModel()
    private var originalBitmapImage: Bitmap = view.getBitmap()
    private var bitmapImage: Bitmap =
        model.getSqueezedBitmap(originalBitmapImage, this.getImageView()?.clipBounds)
    private val onTouchListener: View.OnTouchListener
    private var imageRotation: Float = 0f
    private val bottomBar: CustomBar
    private val topBar: CustomBar
    private val editTopBar: CustomBar

    init {
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
        bottomBar = CustomBar(
            view.getBottomBar(),
            CustomBar.Type.BOTTOM
        )
        topBar = CustomBar(
            view.getTopBar(),
            CustomBar.Type.TOP
        )
        editTopBar = CustomBar(
            view.getEditTopBar(),
            CustomBar.Type.TOP
        )
    }

    override fun onClickButtonOnBottomBar(customBar: CustomBar) {
        bottomBar.hide()
        topBar.hide()
        editTopBar.show()
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

    override fun getImageView(): ImageView? {
        return view.getImageView()
    }

    override fun onMaskingSeekBar(progress: Int) {
        bitmapImage = model.masking(bitmapImage, progress)
        view.setBitmap(bitmapImage)
    }

    override fun onRotate() {
        val rotateBarView = view.createView(R.layout.rotate_bar)
        val rotateBar = CustomBar(
            rotateBarView as ConstraintLayout,
            CustomBar.Type.BOTTOM
        )
        onClickButtonOnBottomBar(rotateBar)
        view.setOnTouchListener(onTouchListener)
    }

    override fun onMasking() {
        val maskingBarView = view.createView(R.layout.masking_bar)
        val maskingBar = CustomBar(
            maskingBarView as ConstraintLayout,
            CustomBar.Type.BOTTOM
        )
        onClickButtonOnBottomBar(maskingBar)
    }

    override fun onFilter() {
        val filterBarView = view.createView(R.layout.filter_bar)
        val filterBar = CustomBar(
            filterBarView as ConstraintLayout,
            CustomBar.Type.BOTTOM
        )
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
}