package com.example.better.mainScreen

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