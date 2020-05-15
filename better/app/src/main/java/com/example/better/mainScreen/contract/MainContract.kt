package com.example.better.mainScreen.contract

import com.example.better.cubeScreen.model.CubeActivityModel

interface MainContract {

    interface View_ {
        fun initView()
        fun checkHasPermission(permission: String): Boolean
        fun requestPermissions(permissions: Array<String>)

    }

    interface Presenter_ {

    }

    interface Model_ {

    }

}