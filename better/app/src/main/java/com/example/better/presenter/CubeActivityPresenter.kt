package com.example.better.presenter


import android.graphics.ColorSpace
import android.view.SurfaceView
import android.view.View
import com.example.better.contract.ContractInterface
import com.example.better.contract.ContractInterface.*
import com.example.better.model.CubeActivityModel
import com.example.better.model.MainActivityModel
import com.example.better.view.CubeActivityView

class CubeActivityPresenter(_view: View_): Presenter_ {

    private var view: View_ = _view
    private var model: Model_ = CubeActivityModel()


    override fun getCubeCoordinates() = model.getCubeCoordinates()

    override fun absoluteDifferenceX():Float {
        return view.absoluteDifferenceX()
    }

    override fun absoluteDifferenceY():Float {
        return view.absoluteDifferenceY()
    }


    override fun moveCube() {
        model.rotateCube(absoluteDifferenceX(), absoluteDifferenceY())
        view.updateViewData()
    }
}