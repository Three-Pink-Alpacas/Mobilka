package com.example.better.presenter

import android.Manifest
import com.example.better.contract.ContractInterface.*
import com.example.better.model.MainActivityModel

class MainActivityPresenter(_view: View): Presenter {
    private var view: View = _view
    private var model: Model = MainActivityModel()

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