package com.example.better.mainScreen.presenter

import android.Manifest
import com.example.better.mainScreen.contract.MainContract.*
import com.example.better.cubeScreen.model.CubeActivityModel
import com.example.better.mainScreen.model.MainActivityModel

class MainActivityPresenter(_view: View_): Presenter_ {
    private var view: View_ = _view
    private var model: Model_ = MainActivityModel()

    private val neededPermissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    init {
        view.initView()

        checkPermissions()
    }

    private fun checkPermissions() {
        val permissionsNotGranted = ArrayList<String>()
        for (permission in neededPermissions) {
            if (!view.checkHasPermission(permission)) {
                permissionsNotGranted.add(permission)
            }
        }

        if (permissionsNotGranted.size > 0) {
            view.requestPermissions(permissionsNotGranted.toTypedArray())
        }
    }

}