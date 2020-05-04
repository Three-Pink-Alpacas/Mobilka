package com.example.better

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import java.util.*


class CubeShowingActivity : Activity() {
    private var mSurface: SurfaceView? = null
    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        setContentView(R.layout.activity_cube_showing)
        mSurface = findViewById(R.id.surface);
    }
    fun mainMenuMove(view: View) {
        val intent = Intent(this, MainActivity::class.java);
        startActivity(intent);
    }
}
