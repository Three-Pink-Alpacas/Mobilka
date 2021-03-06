package com.example.better.editorScreen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.view.View.OnTouchListener
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

interface EditorContract {

    interface Model {
        fun rotate(bitmap: Bitmap, angle: Float): Bitmap
        fun rotate90(bitmap: Bitmap): Bitmap
        fun blackAndWhiteFilter(bitmap: Bitmap): Bitmap
        fun violetFilter(bitmap: Bitmap): Bitmap
        fun negativeFilter(bitmap: Bitmap): Bitmap
        fun contrastFilter(bitmap: Bitmap): Bitmap
        fun sepiaFilter(bitmap: Bitmap): Bitmap
        fun saturationFilter(bitmap: Bitmap): Bitmap
        fun getSqueezedBitmap(
            originalBitmapImage: Bitmap,
            rect: Rect?,
            reqWidth: Int,
            reqHeight: Int
        ): Bitmap?

        fun masking(bitmap: Bitmap, progress: Int, text: TextView): Bitmap
        fun scale(bitmap: Bitmap, progress: Int, text: TextView): Bitmap
        fun findCircle(bitmap: Bitmap): Bitmap
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
        fun onContrastFilter()
        fun onSepiaFilter()
        fun onSaturationFilter()
        fun getImageView(): ImageView?
        fun onMaskingSeekBar(progress: Int, text: TextView)
        fun onScaleSeekBar(progress: Int, text: TextView)
        fun onShapes()
        fun onCircle()
        fun isMainBarHidden():Boolean
        fun showProgressBar()
        fun hideProgressBar()
        fun save():Bitmap
    }

    interface View {
        fun getBottomBar(): ConstraintLayout
        fun getTopBar(): ConstraintLayout
        fun getEditTopBar(): ConstraintLayout
        fun setBitmap(bitmap: Bitmap)
        fun getBitmap(): Bitmap
        fun setImageRotation(angle: Float)
        fun getImageView(): ImageView?
        fun getBlackAndWhiteFiltPrev(): ImageView?
        fun setOnTouchListener(onTouchListener: OnTouchListener)
        fun detachOnTouchListener()
        fun createView(resource: Int): android.view.View
        fun showProgressBar()
        fun hideProgressBar()
        fun getApply(): ImageButton
    }
}
