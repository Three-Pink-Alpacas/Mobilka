package com.example.better

import android.app.Activity
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View


class CubeShowingActivity : Activity() {
    private var mSurface: SurfaceView? = null
    private var mThread: Thread? = null

    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        setContentView(R.layout.activity_cube_showing)
        mSurface = findViewById(R.id.surface);

    }
    // button for getting in main menu
    fun mainMenuMove(view: View) {
        val intent = Intent(this, MainActivity::class.java);
        startActivity(intent);
    }


}
