package com.example.better.editorScreen

import android.view.View
import android.widget.LinearLayout


class RotateBar(_linearLayout: LinearLayout): CustomBar {
    private val linearLayout: LinearLayout = _linearLayout

    override fun hide() {
        TODO("Not yet implemented")
    }

    override fun show() {
        linearLayout.translationY = 0f
    }

}