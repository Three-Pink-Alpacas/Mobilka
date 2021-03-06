package com.example.better.mainScreen

import android.Manifest
import com.example.better.mainScreen.MainContract.*

class MainActivityPresenter(_view: View_): Presenter_ {
    private var view: View_ = _view
    private var model: Model_ = MainActivityModel()

    val neededPermissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

    init {
        view.initView()
    }

    override fun checkPermissions(){

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