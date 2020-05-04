package com.example.better

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.Window
import java.util.*


class CubeShowingActivity : Activity() {
    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(MySurfaceView(this))
    }

    internal inner class MySurfaceView(context: Context?) :
        SurfaceView(context) {
        var path: Path? = null
        var thread: Thread? = null
        var surfaceHolder: SurfaceHolder

        @Volatile
        var running = false
        private val paint =
            Paint(Paint.ANTI_ALIAS_FLAG)
        var random: Random? = null
        override fun onTouchEvent(event: MotionEvent): Boolean {
            if (event.action == MotionEvent.ACTION_DOWN) {
                path = Path()
                path!!.moveTo(event.x, event.y)
            } else if (event.action == MotionEvent.ACTION_MOVE) {
                path!!.lineTo(event.x, event.y)
            } else if (event.action == MotionEvent.ACTION_UP) {
                path!!.lineTo(event.x, event.y)
            }
            if (path != null) {
                val canvas = surfaceHolder.lockCanvas()
                canvas.drawPath(path!!, paint)
                surfaceHolder.unlockCanvasAndPost(canvas)
            }
            return true
        }

        init {
            surfaceHolder = holder
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 3f
            paint.color = Color.WHITE
        }
    }
}
