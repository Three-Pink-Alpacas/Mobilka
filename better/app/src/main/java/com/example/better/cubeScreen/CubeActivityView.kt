package com.example.better.cubeScreen

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.graphics.Shader.TileMode
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.content.ContextCompat.getColor
import com.example.better.R


class CubeActivityView : Activity() {
    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        setContentView(R.layout.fragment_cube)

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

    class MySurfaceView : SurfaceView, SurfaceHolder.Callback, CubeContract.View_ {

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
        var drawing = false

        private var thread: MySurfaceThread? = null
        override fun draw(canvas: Canvas) {
            super.draw(canvas)
            val canvMidX: Float = canvas.width.toFloat()/2
            val canvMidY: Float = (canvas.height.toFloat()/8)*3
            if (drawing) {
                val wallPath = Path()
                val wallPaint = Paint()
                wallPaint.reset() // precaution when resusing Paint object, here shader replaces solid GRAY anyway


                // canvas.drawLine(cube[0].getX()+canvMidX, cube[0].getY()+canvMidY, cube[1].getX()+canvMidX,cube[1].getY()+canvMidY, paint)
                // canvas.drawLine(cube[1].getX()+canvMidX, cube[1].getY()+canvMidY, cube[2].getX()+canvMidX,cube[2].getY()+canvMidY, paint)
                // canvas.drawLine(cube[2].getX()+canvMidX, cube[2].getY()+canvMidY, cube[3].getX()+canvMidX,cube[3].getY()+canvMidY, paint)
                // canvas.drawLine(cube[3].getX()+canvMidX, cube[3].getY()+canvMidY, cube[0].getX()+canvMidX,cube[0].getY()+canvMidY, paint)
                // canvas.drawLine(cube[0].getX()+canvMidX, cube[0].getY()+canvMidY, cube[4].getX()+canvMidX,cube[4].getY()+canvMidY, paint)
                // canvas.drawLine(cube[1].getX()+canvMidX, cube[1].getY()+canvMidY, cube[5].getX()+canvMidX,cube[5].getY()+canvMidY, paint)
                // canvas.drawLine(cube[2].getX()+canvMidX, cube[2].getY()+canvMidY, cube[6].getX()+canvMidX,cube[6].getY()+canvMidY, paint)
                // canvas.drawLine(cube[3].getX()+canvMidX, cube[3].getY()+canvMidY, cube[7].getX()+canvMidX,cube[7].getY()+canvMidY, paint)
                // canvas.drawLine(cube[4].getX()+canvMidX, cube[4].getY()+canvMidY, cube[5].getX()+canvMidX,cube[5].getY()+canvMidY, paint)
                // canvas.drawLine(cube[5].getX()+canvMidX, cube[5].getY()+canvMidY, cube[6].getX()+canvMidX,cube[6].getY()+canvMidY, paint)
                // canvas.drawLine(cube[6].getX()+canvMidX, cube[6].getY()+canvMidY, cube[7].getX()+canvMidX,cube[7].getY()+canvMidY, paint)
                // canvas.drawLine(cube[7].getX()+canvMidX, cube[7].getY()+canvMidY, cube[4].getX()+canvMidX,cube[4].getY()+canvMidY, paint)



                fun faceDraw(firstPoint: CubeActivityModel.ViewPoint, secondPoint: CubeActivityModel.ViewPoint, thirdPoint: CubeActivityModel.ViewPoint, fourthPoint: CubeActivityModel.ViewPoint, color: Int, number: Int){
                    wallPath.reset() // only needed when reusing this path for a new build
                    wallPath.moveTo(firstPoint.getX()+canvMidX, firstPoint.getY()+canvMidY) // used for first point
                    wallPath.lineTo(secondPoint.getX()+canvMidX, secondPoint.getY()+canvMidY)
                    wallPath.lineTo(thirdPoint.getX()+canvMidX, thirdPoint.getY()+canvMidY)
                    wallPath.lineTo(fourthPoint.getX()+canvMidX, fourthPoint.getY()+canvMidY)
                    wallPath.lineTo(firstPoint.getX()+canvMidX, firstPoint.getY()+canvMidY) // there is a setLastPoint action but i found it not to work as expected
                    wallPaint.shader = LinearGradient(
                        firstPoint.getX()+canvMidX,
                        firstPoint.getX()+canvMidX,
                        secondPoint.getX()+canvMidX,
                        firstPoint.getX()+canvMidX,
                        color,
                        color,
                        TileMode.CLAMP
                    )
                    canvas.drawPath(wallPath, wallPaint)

                    when (number) {
                        1 -> {
                            var firstLineFirstDot = CubeActivityModel.ViewPoint((firstPoint.getX() + fourthPoint.getX())/4 + secondPoint.getX()/2, (firstPoint.getY() + fourthPoint.getY())/4 + secondPoint.getY()/2)
                            var firstLineSecondDot = CubeActivityModel.ViewPoint((thirdPoint.getX() + secondPoint.getX())/4 + fourthPoint.getX()/2, (thirdPoint.getY() + secondPoint.getY())/4 + fourthPoint.getY()/2)
                            var secondLineFirstDot: CubeActivityModel.ViewPoint
                            var secondLineSecondDot: CubeActivityModel.ViewPoint
                            canvas.drawLine(firstLineFirstDot.getX()+canvMidX, firstLineFirstDot.getY()+canvMidY, firstLineSecondDot.getX()+canvMidX,firstLineSecondDot.getY()+canvMidY, paint)
                        }
                        2 -> {
                            var firstLineFirstDot = CubeActivityModel.ViewPoint((firstPoint.getX() + secondPoint.getX())/3 + thirdPoint.getX()/3, (firstPoint.getY() + secondPoint.getY())/3 + thirdPoint.getY()/3)
                            var firstLineSecondDot = CubeActivityModel.ViewPoint((thirdPoint.getX() + secondPoint.getX())/3 + fourthPoint.getX()/3, (thirdPoint.getY() + secondPoint.getY())/3 + fourthPoint.getY()/3)

                            var secondLineFirstDot = CubeActivityModel.ViewPoint((firstPoint.getX() + fourthPoint.getX())/3 + thirdPoint.getX()/3, (firstPoint.getY() + fourthPoint.getY())/3 + thirdPoint.getY()/3)
                            var secondLineSecondDot = CubeActivityModel.ViewPoint((firstPoint.getX() + secondPoint.getX())/3 + fourthPoint.getX()/3, (firstPoint.getY() + secondPoint.getY())/3 + fourthPoint.getY()/3)

                            canvas.drawLine(firstLineFirstDot.getX()+canvMidX, firstLineFirstDot.getY()+canvMidY, firstLineSecondDot.getX()+canvMidX,firstLineSecondDot.getY()+canvMidY, paint)
                            canvas.drawLine(secondLineFirstDot.getX()+canvMidX, secondLineFirstDot.getY()+canvMidY, secondLineSecondDot.getX()+canvMidX,secondLineSecondDot.getY()+canvMidY, paint)
                        }
                        3 -> {
                            var firstLineFirstDot = CubeActivityModel.ViewPoint((firstPoint.getX() + secondPoint.getX())/3 + thirdPoint.getX()/3, (firstPoint.getY() + secondPoint.getY())/3 + thirdPoint.getY()/3)
                            var firstLineSecondDot = CubeActivityModel.ViewPoint((thirdPoint.getX() + secondPoint.getX())/3 + fourthPoint.getX()/3, (thirdPoint.getY() + secondPoint.getY())/3 + fourthPoint.getY()/3)

                            var secondLineFirstDot = CubeActivityModel.ViewPoint((firstPoint.getX() + fourthPoint.getX())/3 + thirdPoint.getX()/3, (firstPoint.getY() + fourthPoint.getY())/3 + thirdPoint.getY()/3)
                            var secondLineSecondDot = CubeActivityModel.ViewPoint((firstPoint.getX() + secondPoint.getX())/3 + fourthPoint.getX()/3, (firstPoint.getY() + secondPoint.getY())/3 + fourthPoint.getY()/3)

                            var thirdLineFirstDot = CubeActivityModel.ViewPoint((firstPoint.getX() + fourthPoint.getX())/4 + secondPoint.getX()/2, (firstPoint.getY() + fourthPoint.getY())/4 + secondPoint.getY()/2)
                            var thirdLineSecondDot = CubeActivityModel.ViewPoint((thirdPoint.getX() + secondPoint.getX())/4 + fourthPoint.getX()/2, (thirdPoint.getY() + secondPoint.getY())/4 + fourthPoint.getY()/2)

                            canvas.drawLine(firstLineFirstDot.getX()+canvMidX, firstLineFirstDot.getY()+canvMidY, firstLineSecondDot.getX()+canvMidX,firstLineSecondDot.getY()+canvMidY, paint)
                            canvas.drawLine(secondLineFirstDot.getX()+canvMidX, secondLineFirstDot.getY()+canvMidY, secondLineSecondDot.getX()+canvMidX,secondLineSecondDot.getY()+canvMidY, paint)
                            canvas.drawLine(thirdLineFirstDot.getX()+canvMidX, thirdLineFirstDot.getY()+canvMidY, thirdLineSecondDot.getX()+canvMidX,thirdLineSecondDot.getY()+canvMidY, paint)
                        }
                        4 -> {
                            var firstLineFirstDot = CubeActivityModel.ViewPoint((fourthPoint.getX() + firstPoint.getX())/3 + thirdPoint.getX()/3, (fourthPoint.getY() + firstPoint.getY())/3 + thirdPoint.getY()/3)
                            var firstLineSecondDot = CubeActivityModel.ViewPoint(((firstPoint.getX() + secondPoint.getX())/2 + thirdPoint.getX())/2, ((firstPoint.getY() + secondPoint.getY())/2 + thirdPoint.getY())/2)

                            var secondLineFirstDot = CubeActivityModel.ViewPoint((firstPoint.getX() + secondPoint.getX())/3 + fourthPoint.getX()/3, (firstPoint.getY() + secondPoint.getY())/3 + fourthPoint.getY()/3)
                            var secondLineSecondDot = CubeActivityModel.ViewPoint(((firstPoint.getX() + secondPoint.getX())/2 + thirdPoint.getX())/2, ((firstPoint.getY() + secondPoint.getY())/2 + thirdPoint.getY())/2)

                            var thirdLineFirstDot = CubeActivityModel.ViewPoint((firstPoint.getX() + secondPoint.getX())/3 + thirdPoint.getX()/3, (firstPoint.getY() + secondPoint.getY())/3 + thirdPoint.getY()/3)
                            var thirdLineSecondDot = CubeActivityModel.ViewPoint((firstPoint.getX() + secondPoint.getX())/3 + fourthPoint.getX()/3, (firstPoint.getY() + secondPoint.getY())/3 + fourthPoint.getY()/3)

                            canvas.drawLine(firstLineFirstDot.getX()+canvMidX, firstLineFirstDot.getY()+canvMidY, firstLineSecondDot.getX()+canvMidX,firstLineSecondDot.getY()+canvMidY, paint)
                            canvas.drawLine(secondLineFirstDot.getX()+canvMidX, secondLineFirstDot.getY()+canvMidY, secondLineSecondDot.getX()+canvMidX,secondLineSecondDot.getY()+canvMidY, paint)
                            canvas.drawLine(thirdLineFirstDot.getX()+canvMidX, thirdLineFirstDot.getY()+canvMidY, thirdLineSecondDot.getX()+canvMidX,thirdLineSecondDot.getY()+canvMidY, paint)
                        }
                        5 ->{
                            var firstLineFirstDot = CubeActivityModel.ViewPoint((fourthPoint.getX() + firstPoint.getX())/3 + thirdPoint.getX()/3, (fourthPoint.getY() + firstPoint.getY())/3 + thirdPoint.getY()/3)
                            var firstLineSecondDot = CubeActivityModel.ViewPoint(((firstPoint.getX() + secondPoint.getX())/2 + thirdPoint.getX())/2, ((firstPoint.getY() + secondPoint.getY())/2 + thirdPoint.getY())/2)

                            var secondLineFirstDot = CubeActivityModel.ViewPoint((firstPoint.getX() + secondPoint.getX())/3 + fourthPoint.getX()/3, (firstPoint.getY() + secondPoint.getY())/3 + fourthPoint.getY()/3)
                            var secondLineSecondDot = CubeActivityModel.ViewPoint(((firstPoint.getX() + secondPoint.getX())/2 + thirdPoint.getX())/2, ((firstPoint.getY() + secondPoint.getY())/2 + thirdPoint.getY())/2)

                            canvas.drawLine(firstLineFirstDot.getX()+canvMidX, firstLineFirstDot.getY()+canvMidY, firstLineSecondDot.getX()+canvMidX,firstLineSecondDot.getY()+canvMidY, paint)
                            canvas.drawLine(secondLineFirstDot.getX()+canvMidX, secondLineFirstDot.getY()+canvMidY, secondLineSecondDot.getX()+canvMidX,secondLineSecondDot.getY()+canvMidY, paint)
                        }
                        6 ->{
                            var firstLineFirstDot = CubeActivityModel.ViewPoint((fourthPoint.getX() + firstPoint.getX())/3 + thirdPoint.getX()/3, (fourthPoint.getY() + firstPoint.getY())/3 + thirdPoint.getY()/3)
                            var firstLineSecondDot = CubeActivityModel.ViewPoint((firstPoint.getX() + fourthPoint.getX())/4 + secondPoint.getX()/2, (firstPoint.getY() + fourthPoint.getY())/4 + secondPoint.getY()/2)

                            var secondLineFirstDot = CubeActivityModel.ViewPoint((thirdPoint.getX() + secondPoint.getX())/3 + fourthPoint.getX()/3, (thirdPoint.getY() + secondPoint.getY())/3 + fourthPoint.getY()/3)
                            var secondLineSecondDot = CubeActivityModel.ViewPoint((firstPoint.getX() + fourthPoint.getX())/4 + secondPoint.getX()/2, (firstPoint.getY() + fourthPoint.getY())/4 + secondPoint.getY()/2)

                            var thirdLineFirstDot = CubeActivityModel.ViewPoint((firstPoint.getX() + secondPoint.getX())/3 + thirdPoint.getX()/3, (firstPoint.getY() + secondPoint.getY())/3 + thirdPoint.getY()/3)
                            var thirdLineSecondDot = CubeActivityModel.ViewPoint((thirdPoint.getX() + secondPoint.getX())/3 + fourthPoint.getX()/3, (thirdPoint.getY() + secondPoint.getY())/3 + fourthPoint.getY()/3)

                            canvas.drawLine(firstLineFirstDot.getX()+canvMidX, firstLineFirstDot.getY()+canvMidY, firstLineSecondDot.getX()+canvMidX,firstLineSecondDot.getY()+canvMidY, paint)
                            canvas.drawLine(secondLineFirstDot.getX()+canvMidX, secondLineFirstDot.getY()+canvMidY, secondLineSecondDot.getX()+canvMidX,secondLineSecondDot.getY()+canvMidY, paint)
                            canvas.drawLine(thirdLineFirstDot.getX()+canvMidX, thirdLineFirstDot.getY()+canvMidY, thirdLineSecondDot.getX()+canvMidX,thirdLineSecondDot.getY()+canvMidY, paint)
                        }
                    }
                }

                val allowableFaces = presenter.getAllowableFaces()

                if (allowableFaces[0])
                    faceDraw(cube[0],cube[1],cube[2],cube[3], getColor(context, R.color.colorCube2), 3)

                if (allowableFaces[1])
                    faceDraw(cube[0],cube[3],cube[7],cube[4],getColor(context, R.color.colorCube3), 4)

                if (allowableFaces[2])
                    faceDraw(cube[3],cube[2],cube[6],cube[7],getColor(context, R.color.colorCube1), 6)

                if (allowableFaces[3])
                    faceDraw(cube[1],cube[5],cube[6],cube[2],getColor(context, R.color.colorCube4), 2)

                if (allowableFaces[4])
                    faceDraw(cube[5],cube[4],cube[7],cube[6],getColor(context, R.color.colorCube5), 1)

                if (allowableFaces[5])
                    faceDraw(cube[0],cube[4],cube[5],cube[1],getColor(context, R.color.colorCube1), 5)





            }
        }

        override fun updateViewData(){
            cube = presenter?.getCubeCoordinates()
        }

        override fun initView() {
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
                presenter.moveCube()
            } else if (action == MotionEvent.ACTION_DOWN) {

                initX = event.x
                movedX = event.x
                initY = event.y
                movedY = event.y
                presenter.moveCube()
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
            thread =
                MySurfaceThread(
                    getHolder(),
                    this
                )
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