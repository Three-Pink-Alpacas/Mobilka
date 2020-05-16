package com.example.better.editorScreen

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.LinearLayout

interface EditorContract {

    interface Model {

    }

    interface Presenter {
        fun onClickButtonOnBottomBar()

    }

    interface View {
        fun getBottomBar(): LinearLayout
    }
}
