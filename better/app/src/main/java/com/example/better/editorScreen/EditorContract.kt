package com.example.better.editorScreen

import android.graphics.Bitmap
import android.widget.LinearLayout

interface EditorContract {

    interface Model {
        fun rotate(bitmap: Bitmap, angle: Float): Bitmap
    }

    interface Presenter {
        fun onClickButtonOnBottomBar(customBar: CustomBar)
        fun onRotate()
    }

    interface View {
        fun getBottomBar(): LinearLayout
        fun getRotateBar(): LinearLayout
    }
}
