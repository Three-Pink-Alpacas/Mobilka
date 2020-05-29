package com.example.better.editorScreen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.view.View.OnTouchListener
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout

interface EditorContract {

    interface Model {
        fun rotate(bitmap: Bitmap, angle: Float): Bitmap
        fun rotate90(bitmap: Bitmap): Bitmap
        fun blackAndWhiteFilter(bitmap: Bitmap): Bitmap
        fun violetFilter(bitmap: Bitmap): Bitmap
        fun negativeFilter(bitmap: Bitmap): Bitmap
        fun getSqueezedBitmap(
            originalBitmapImage: Bitmap,
            rect: Rect?,
            reqWidth: Int,
            reqHeight: Int
        ): Bitmap?

        fun masking(bitmap: Bitmap, progress: Int): Bitmap
        fun scale(bitmap: Bitmap, progress: Int): Bitmap
        fun findCircle(bitmap: Bitmap, context: Context): Bitmap
        fun findRectangle(bitmap: Bitmap, context: Context): Bitmap
    }

    interface Presenter {
        fun onClickButtonOnBottomBar()
        fun onAcceptChanges()
        fun onCancelChanges()
        fun onUndoChanges()
        fun onRedoChanges()
        fun onRotate()
        fun onRotateRight90()
        fun onFilter()
        fun onMasking()
        fun onScale()
        fun onBlackAndWhiteFilter()
        fun onVioletFilter()
        fun onNegativeFilter()
        fun getImageView(): ImageView?
        fun onMaskingSeekBar(progress: Int)
        fun onScaleSeekBar(progress: Int)
        fun onShapes()
        fun onCircle(context: Context)
        fun onRectangle(context: Context)
    }

    interface View {
        fun getBottomBar(): ConstraintLayout
        fun getTopBar(): ConstraintLayout
        fun getEditTopBar(): ConstraintLayout
        fun setBitmap(bitmap: Bitmap)
        fun getBitmap(): Bitmap
        fun setImageRotation(angle: Float)
        fun getImageView(): ImageView?
        fun setOnTouchListener(onTouchListener: OnTouchListener)
        fun detachOnTouchListener()
        fun createView(resource: Int): android.view.View
    }
}
