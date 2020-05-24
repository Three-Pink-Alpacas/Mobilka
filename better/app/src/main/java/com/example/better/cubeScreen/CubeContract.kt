package com.example.better.cubeScreen

interface CubeContract {

    interface View_ {
        fun initView()
        fun absoluteDifferenceX(): Float
        fun absoluteDifferenceY(): Float
        fun updateViewData()
    }

    interface Presenter_ {
        fun getCubeCoordinates(): Array<CubeActivityModel.ViewPoint>
        fun getAllowableFaces(): Array<Boolean>
        fun absoluteDifferenceX(): Float
        fun absoluteDifferenceY(): Float
        fun moveCube()
    }

    interface Model_ {
        fun getCubeCoordinates(): Array<CubeActivityModel.ViewPoint>
        fun getAllowableFaces(): Array<Boolean>
        fun getNormalVector(firstPoint: CubeActivityModel.RealPoint, secondPoint: CubeActivityModel.RealPoint, thirdPoint: CubeActivityModel.RealPoint): CubeActivityModel.RealPoint
        fun rotateCube(diffX: Float, diffY: Float)
    }
}