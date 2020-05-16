package com.example.better.cubeScreen

import kotlin.math.cos
import kotlin.math.sin
import com.example.better.cubeScreen.CubeContract.*

class CubeActivityModel(): Model_ {
    var cubeReal: Array<RealPoint> = arrayOf(
        RealPoint(50F, 50F, 50F),
        RealPoint(150F, 50F, 50F),
        RealPoint(150F, 150F, 50F),
        RealPoint(50F, 150F, 50F),
        RealPoint(50F, 50F, 150F),
        RealPoint(150F, 50F, 150F),
        RealPoint(150F, 150F, 150F),
        RealPoint(50F, 150F, 150F)
    )

    var cubeView: Array<ViewPoint> = arrayOf(
        cubeReal[0].into2D(),
        cubeReal[1].into2D(),
        cubeReal[2].into2D(),
        cubeReal[3].into2D(),
        cubeReal[4].into2D(),
        cubeReal[5].into2D(),
        cubeReal[6].into2D(),
        cubeReal[7].into2D()
    )

    override fun getCubeCoordinates():Array<ViewPoint> {
        for (i in 0 until cubeView.size){
            cubeView[i] = cubeReal[i].into2D()
        }
        return cubeView
    }

    override fun rotateCube(diffX: Float, diffY: Float) {
        rotateCubeX(-diffY/100)
        rotateCubeY(diffX/100)
    }

    fun rotateCubeX(angle: Float){
        for (i in 0 until cubeReal.size){
            cubeReal[i] = cubeReal[i].rotateX(angle)
        }
    }
    fun rotateCubeY(angle: Float){
        for (i in 0 until cubeReal.size){
            cubeReal[i] = cubeReal[i].rotateY(angle)
        }
    }
    fun rotateCubeZ(angle: Float){
        for (i in 0 until cubeReal.size){
            cubeReal[i] = cubeReal[i].rotateZ(angle)
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
            val rxMatrix: Array<FloatArray> = arrayOf(
                floatArrayOf(1F, 0F, 0F),
                floatArrayOf(0F, cos(angle), -sin(angle)),
                floatArrayOf(0F, sin(angle), cos(angle)))
            val rws = rxMatrix.size
            val clmns = rxMatrix[0].size
            for (i in 0 until rws){
                for (j in 0 until clmns){
                    answerArr[i] += rxMatrix[i][j] * pointArr[j]
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
            val ryMatrix: Array<FloatArray> = arrayOf(
                floatArrayOf(cos(angle), 0F, sin(angle)),
                floatArrayOf(0F, 1F, 0F),
                floatArrayOf(-sin(angle), 0F, cos(angle)))
            val rws = ryMatrix.size
            val clmns = ryMatrix[0].size
            for (i in 0 until rws){
                for (j in 0 until clmns){
                    answerArr[i] += ryMatrix[i][j] * pointArr[j]
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
}