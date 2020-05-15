package com.example.better.cubeScreen.contract
import com.example.better.cubeScreen.model.CubeActivityModel
interface CubeContract {

    interface View_ {
        fun initView()
        fun absoluteDifferenceX(): Float
        fun absoluteDifferenceY(): Float
        fun updateViewData()
    }

    interface Presenter_ {
        fun getCubeCoordinates():Array<CubeActivityModel.ViewPoint>
        fun absoluteDifferenceX(): Float
        fun absoluteDifferenceY(): Float
        fun moveCube()
    }

    interface Model_ {
        fun getCubeCoordinates():Array<CubeActivityModel.ViewPoint>
        fun rotateCube(diffX: Float, diffY: Float)
    }
}