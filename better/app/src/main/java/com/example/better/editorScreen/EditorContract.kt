package com.example.better.editorScreen

import android.widget.LinearLayout

interface EditorContract {
    interface Presenter {
        fun onClickButtonOnBottomBar()
    }

    interface View {
        fun getBottomBar(): LinearLayout
    }
}
