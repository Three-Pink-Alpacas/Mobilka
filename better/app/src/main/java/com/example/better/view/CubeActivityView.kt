package com.example.better.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import com.example.better.R
import com.example.better.contract.MainContract
import com.example.better.presenter.CubeActivityPresenter


class CubeActivityView : Activity() {

    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        setContentView(R.layout.activity_cube_showing)

    }
    // button for getting in main menu
    fun mainMenuMove(view: View) {
        val intent = Intent(this, MainActivityView::class.java);
        startActivity(intent);
    }


    class MySurfaceThread(private val myThreadSurfaceHolder: SurfaceHolder, private val myThreadSurfaceView: MySurfaceView) : Thread() {
        private var myThreadRun = false
        fun setRunning(b: Boolean) {
            myThreadRun = b
        }
        override fun run() {
            // super.run();
            while (myThreadRun) {
                var c: Canvas? = null
                try {
                    c = myThreadSurfaceHolder.lockCanvas(null)
                    if(c != null) {
                        synchronized(myThreadSurfaceHolder) { myThreadSurfaceView.draw(c) }
                    }
                } finally {
                    // do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
                    if (c != null) {
                        myThreadSurfaceHolder.unlockCanvasAndPost(c)
                    }
                }
            }
        }

    }

    class MySurfaceView : SurfaceView, SurfaceHolder.Callback, MainContract.View_ {

        private fun init() {
            holder.addCallback(this)
            isFocusable = true // make sure we get key events
            paint.setStyle(Paint.Style.STROKE)
            paint.setStrokeWidth(3F)
            paint.setColor(Color.WHITE)
        }
        var presenter = CubeActivityPresenter(this)
        var cube = presenter?.getCubeCoordinates()
        var paint = Paint(Paint.ANTI_ALIAS_FLAG)
        var initX = 0F
        var initY = 0F
        var movedX = 0F
        var movedY = 0F
        var radius = 0F
        var drawing = false
        private var thread: MySurfaceThread? = null
        override fun draw(canvas: Canvas) {
            super.draw(canvas)
            if (drawing) {
                canvas.drawLine(cube[0].getX()+500, cube[0].getY()+500, cube[1].getX()+500,cube[1].getY()+500, paint)
                canvas.drawLine(cube[1].getX()+500, cube[1].getY()+500, cube[2].getX()+500,cube[2].getY()+500, paint)
                canvas.drawLine(cube[2].getX()+500, cube[2].getY()+500, cube[3].getX()+500,cube[3].getY()+500, paint)
                canvas.drawLine(cube[3].getX()+500, cube[3].getY()+500, cube[0].getX()+500,cube[0].getY()+500, paint)
                canvas.drawLine(cube[0].getX()+500, cube[0].getY()+500, cube[4].getX()+500,cube[4].getY()+500, paint)
                canvas.drawLine(cube[1].getX()+500, cube[1].getY()+500, cube[5].getX()+500,cube[5].getY()+500, paint)
                canvas.drawLine(cube[2].getX()+500, cube[2].getY()+500, cube[6].getX()+500,cube[6].getY()+500, paint)
                canvas.drawLine(cube[3].getX()+500, cube[3].getY()+500, cube[7].getX()+500,cube[7].getY()+500, paint)
                canvas.drawLine(cube[4].getX()+500, cube[4].getY()+500, cube[5].getX()+500,cube[5].getY()+500, paint)
                canvas.drawLine(cube[5].getX()+500, cube[5].getY()+500, cube[6].getX()+500,cube[6].getY()+500, paint)
                canvas.drawLine(cube[6].getX()+500, cube[6].getY()+500, cube[7].getX()+500,cube[7].getY()+500, paint)
                canvas.drawLine(cube[7].getX()+500, cube[7].getY()+500, cube[4].getX()+500,cube[4].getY()+500, paint)
            }
        }

        override fun updateViewData(){
            cube = presenter?.getCubeCoordinates()
        }

        override fun initView() {
            TODO("Not yet implemented")
        }

        override fun checkHasPermission(permission: String): Boolean {
            TODO("Not yet implemented")
        }

        override fun requestPermissions(permissions: Array<String>) {
            TODO("Not yet implemented")
        }

        override fun absoluteDifferenceX(): Float{
            return (movedX - initX)
        }
        override fun absoluteDifferenceY(): Float{
            return (movedY - initY)
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            // return super.onTouchEvent(event);
            val action = event.action
            if (action == MotionEvent.ACTION_MOVE) {
                initX = movedX
                movedX = event.x
                initY = movedY
                movedY = event.y
                presenter?.moveCube()
            } else if (action == MotionEvent.ACTION_DOWN) {
                initX = event.x
                movedX = event.x
                initY = event.y
                movedY = event.y
                presenter?.moveCube()
                drawing = true
            } else if (action == MotionEvent.ACTION_UP) {
                drawing = true
            }
            return true
        }

        constructor(context: Context?) : super(context) { init() }

        constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { init() }

        constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) { init() }


        override fun surfaceChanged(arg0: SurfaceHolder, arg1: Int, arg2: Int, arg3: Int) {}

        override fun surfaceCreated(holder: SurfaceHolder) {
            thread = MySurfaceThread(getHolder(), this)
            thread!!.setRunning(true)
            thread!!.start()
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            var retry = true
            thread!!.setRunning(false)
            while (retry) {
                try {
                    thread!!.join()
                    retry = false
                } catch (e: InterruptedException) {
                }
            }
        }
    }


}