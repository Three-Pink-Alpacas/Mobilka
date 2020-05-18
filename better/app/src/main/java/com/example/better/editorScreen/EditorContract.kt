package com.example.better.editorScreen

import android.graphics.Bitmap
import android.graphics.Rect
import android.view.View.OnTouchListener
import android.widget.ImageView
import android.widget.LinearLayout

interface EditorContract {

    interface Model {
        fun rotate(bitmap: Bitmap, angle: Float): Bitmap
        fun blackAndWhiteFilter(bitmap: Bitmap): Bitmap
        fun violetFilter(bitmap: Bitmap): Bitmap
        fun negativeFilter(bitmap: Bitmap): Bitmap
        fun getSqueezedBitmap(originalBitmapImage: Bitmap, rect: Rect?):Bitmap
        fun masking(bitmap: Bitmap, progress: Int): Bitmap
    }

    interface Presenter {
        fun onClickButtonOnBottomBar(customBar: CustomBar)
        fun onRotate()
        fun onRotateRight90()
        fun onFilter()
        fun onMasking()
        fun onBlackAndWhiteFilter()
        fun onVioletFilter()
        fun onNegativeFilter()
        fun getImageView(): ImageView?
        fun onMaskingSeekBar(progress: Int)
    }

    interface View {
        fun getBottomBar(): LinearLayout
        fun getRotateBar(): LinearLayout
        fun setBitmap(bitmap: Bitmap)
        fun getBitmap(): Bitmap
        fun setImageRotation(angle: Float)
        fun getImageView(): ImageView?
        fun getFilterBar(): LinearLayout
        fun setOnTouchListener(onTouchListener: OnTouchListener)
        fun detachOnTouchListener()
        fun getMaskingBar(): LinearLayout
        fun getProgress():Int
    }
}
