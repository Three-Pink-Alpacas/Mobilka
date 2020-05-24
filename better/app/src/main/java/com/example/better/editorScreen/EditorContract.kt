package com.example.better.editorScreen

import android.graphics.Bitmap
import android.graphics.Rect
import android.view.View.OnTouchListener
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.better.utils.CustomBar

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
        fun onBlackAndWhiteFilter()
        fun onVioletFilter()
        fun onNegativeFilter()
        fun getImageView(): ImageView?
        fun onMaskingSeekBar(progress: Int)
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
        fun getProgress(): Int
    }
}
