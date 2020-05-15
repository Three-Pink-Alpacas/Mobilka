package com.example.better.presenter

import android.Manifest
import android.graphics.ColorSpace
import android.view.View
import com.example.better.contract.ContractInterface.*
import com.example.better.model.CubeActivityModel
import com.example.better.model.MainActivityModel

class MainActivityPresenter(_view: View_): Presenter_ {
    private var view = _view
    private var model: CubeActivityModel? = null

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

    override fun getCubeCoordinates(): Array<CubeActivityModel.ViewPoint> {
        TODO("Not yet implemented")
    }

    override fun absoluteDifferenceX(): Float {
        TODO("Not yet implemented")
    }

    override fun absoluteDifferenceY(): Float {
        TODO("Not yet implemented")
    }

    override fun moveCube() {
        TODO("Not yet implemented")
    }

}