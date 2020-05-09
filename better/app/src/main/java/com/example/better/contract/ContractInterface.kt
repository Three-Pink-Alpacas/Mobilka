package com.example.better.contract

interface ContractInterface {

    interface View {
        fun initView()
        fun checkHasPermission(permission: String): Boolean
        fun requestPermissions(permissions: Array<String>)
    }

    interface Presenter {

    }

    interface Model {

    }

}