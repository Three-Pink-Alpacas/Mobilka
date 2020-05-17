package com.example.better.editorScreen

import android.graphics.Bitmap
import android.widget.LinearLayout

interface EditorContract {

    interface Model {
        fun rotate(bitmap: Bitmap, angle: Float): Bitmap
        fun blackAndWhiteFilter(bitmap: Bitmap): Bitmap
        fun violetFilter(bitmap: Bitmap): Bitmap
        fun negativeFilter(bitmap: Bitmap): Bitmap
    }

    interface Presenter {
        fun onClickButtonOnBottomBar(customBar: CustomBar)
        fun onRotate()
        fun onRotateRight90()
        fun onFilter()
        fun onBlackAndWhiteFilter()
        fun onVioletFilter()
        fun onNegativeFilter()
    }

    interface View {
        fun getBottomBar(): LinearLayout
        fun getRotateBar(): LinearLayout
        fun setBitmap(bitmap: Bitmap)
        fun getBitmap(): Bitmap
        fun getFilterBar(): LinearLayout
    }
}
