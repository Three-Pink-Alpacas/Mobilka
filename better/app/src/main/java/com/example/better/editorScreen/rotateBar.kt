package com.example.better.editorScreen

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_editor.*


class RotateBar(_linearLayout: LinearLayout): CustomBar {
    private val linearLayout: LinearLayout = _linearLayout

    override fun hide() {
        TODO("Not yet implemented")
    }

    override fun show() {
        linearLayout.translationY = 0f
    }
}