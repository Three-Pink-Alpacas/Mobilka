package com.example.better.cubeScreen


import com.example.better.cubeScreen.CubeContract.*

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