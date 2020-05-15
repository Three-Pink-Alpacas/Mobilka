package com.example.better.contract

import com.example.better.model.CubeActivityModel

interface MainContract {

    interface View_ {
        fun initView()
        fun checkHasPermission(permission: String): Boolean
        fun requestPermissions(permissions: Array<String>)
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