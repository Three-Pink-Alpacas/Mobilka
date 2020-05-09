package com.example.better.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.math.cos
import kotlin.math.sin


class MySurfaceView (context: Context, attrs: AttributeSet? = null) : SurfaceView(context, attrs), SurfaceHolder.Callback {

    val cube: Array<RealPoint> = arrayOf(
        RealPoint(2000F, 2000F, 2000F),
        RealPoint(2500F, 2000F, 2000F),
        RealPoint(2500F, 2500F, 2000F),
        RealPoint(2000F, 2500F, 2000F),
        RealPoint(2000F, 2000F, 2500F),
        RealPoint(2500F, 2000F, 2500F),
        RealPoint(2500F, 2500F, 2500F),
        RealPoint(2000F, 2500F, 2500F)
    )

    fun showCube(cube: Array<RealPoint>, canvas: Canvas?, mPaint: Paint){
        for (i in 0 until cube.size){
            canvas?.drawPoint(cube[i].into2D().getX(), cube[i].into2D().getY(), mPaint)
        }
    }

    fun rotateCubeX(cube: Array<RealPoint>, angle: Float){
        for (i in 0 until cube.size){
            cube[i] = cube[i].rotateX(angle)
        }
    }
    fun rotateCubeY(cube: Array<RealPoint>, angle: Float){
        for (i in 0 until cube.size){
            cube[i] = cube[i].rotateY(angle)
        }
    }
    fun rotateCubeZ(cube: Array<RealPoint>, angle: Float){
        for (i in 0 until cube.size){
            cube[i] = cube[i].rotateZ(angle)
        }
    }

    open class ViewPoint(private val mX: Float = 0F, private val mY: Float = 0F){
        fun getX(): Float { return mX }
        fun getY(): Float { return mY }
    }

    class RealPoint(private val mX: Float = 0F, private val mY: Float = 0F, private val mZ: Float = 0F): ViewPoint(mX, mY){
        fun getZ(): Float { return mZ }

        val orthographicMatrix: Array<FloatArray> = arrayOf(
            floatArrayOf(1F, 0F, 0F),
            floatArrayOf(0F, 1F, 0F))
        val rws = orthographicMatrix.size
        val clmns = orthographicMatrix[0].size

        fun into2D(): ViewPoint {
            val answerArr = floatArrayOf(0F, 0F)
            val pointArr = floatArrayOf(mX, mY, mZ)
            for (i in 0 until rws){
                for (j in 0 until clmns){
                    answerArr[i] += orthographicMatrix[i][j] * pointArr[j]
                }
            }
            return ViewPoint(
                answerArr[0],
                answerArr[1]
            )

        }
        fun rotateX(angle: Float): RealPoint {
            val answerArr = floatArrayOf(0F, 0F, 0F)
            val pointArr = floatArrayOf(mX, mY, mZ)
            val RxMatrix: Array<FloatArray> = arrayOf(
                floatArrayOf(1F, 0F, 0F),
                floatArrayOf(0F, cos(angle), -sin(angle)),
                floatArrayOf(0F, sin(angle), cos(angle)))
            val rws = RxMatrix.size
            val clmns = RxMatrix[0].size
            for (i in 0 until rws){
                for (j in 0 until clmns){
                    answerArr[i] += RxMatrix[i][j] * pointArr[j]
                }
            }
            return RealPoint(
                answerArr[0],
                answerArr[1],
                answerArr[2]
            )
        }
        fun rotateY(angle: Float): RealPoint {
            val answerArr = floatArrayOf(0F, 0F, 0F)
            val pointArr = floatArrayOf(mX, mY, mZ)
            val RyMatrix: Array<FloatArray> = arrayOf(
                floatArrayOf(cos(angle), 0F, sin(angle)),
                floatArrayOf(0F, 1F, 0F),
                floatArrayOf(-sin(angle), 0F, cos(angle)))
            val rws = RyMatrix.size
            val clmns = RyMatrix[0].size
            for (i in 0 until rws){
                for (j in 0 until clmns){
                    answerArr[i] += RyMatrix[i][j] * pointArr[j]
                }
            }
            return RealPoint(
                answerArr[0],
                answerArr[1],
                answerArr[2]
            )
        }
        fun rotateZ(angle: Float): RealPoint {
            val answerArr = floatArrayOf(0F, 0F, 0F)
            val pointArr = floatArrayOf(mX, mY, mZ)
            val RzMatrix: Array<FloatArray> = arrayOf(
                floatArrayOf(cos(angle), -sin(angle), 0F),
                floatArrayOf(sin(angle), cos(angle), 0F),
                floatArrayOf(0F, 0F, 1F))
            val rws = RzMatrix.size
            val clmns = RzMatrix[0].size
            for (i in 0 until rws){
                for (j in 0 until clmns){
                    answerArr[i] += RzMatrix[i][j] * pointArr[j]
                }
            }
            return RealPoint(
                answerArr[0],
                answerArr[1],
                answerArr[2]
            )
        }
    }

    fun Canvas.center(): ViewPoint =
        ViewPoint(width / 2f, height / 2f)
    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        val mPaint = Paint()
        val xMax = 5000F
        val xMin = 0F
        val yMax = 5000F
        val yMin = 0F
        val width = canvas!!.width.toFloat()
        val height = canvas!!.height.toFloat()
        canvas!!.scale(width / (xMax - xMin), -height / (yMax - yMin))
        canvas!!.translate(-xMin + 0.2F, -yMax + 0.2F)
        mPaint.setStrokeWidth(50F)
        mPaint.setColor(Color.WHITE)
        mPaint.isAntiAlias
        canvas?.drawColor(Color.BLACK)
        var angle:Float = 0.1F
        rotateCubeX(cube, angle)
        rotateCubeY(cube, angle)
        rotateCubeZ(cube, angle)
        showCube(cube, canvas, mPaint)

    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
// TODO Auto-generated method stub
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        var canvas: Canvas? = null
        try {
            canvas = holder.lockCanvas(null)
            synchronized(holder) { draw(canvas) }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas)
            }
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
// TODO Auto-generated method stub
    }

    init {
        holder.addCallback(this)
    }

}
