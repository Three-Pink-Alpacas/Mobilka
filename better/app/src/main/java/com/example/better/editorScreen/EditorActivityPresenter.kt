package com.example.better.editorScreen

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.better.R
import com.example.better.utils.CustomBar
import com.example.better.utils.OnMoveTouchListener
import kotlinx.android.synthetic.main.masking_bar.view.*
import kotlinx.android.synthetic.main.scale_bar.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
class EditorActivityPresenter(_view: EditorContract.View) : EditorContract.Presenter {
    private val view: EditorContract.View = _view
    private val model: EditorContract.Model = EditorActivityModel()
    private var originalBitmapImage: Bitmap = view.getBitmap()
    private lateinit var bitmapImage: Bitmap
    private val onTouchListener: View.OnTouchListener
    private var imageRotation: Float = 0f
    private val bottomBar: CustomBar
    private val topBar: CustomBar
    private val editTopBar: CustomBar
    private var customBar: CustomBar? = null
    private var mainBarIsHidden = false

    private lateinit var globalHistory: HistoryHelper<Bitmap>
    private var localHistory: HistoryHelper<Bitmap>? = null
    private var historyFn: HistoryHelper<(Bitmap) -> Bitmap>

    init {
        GlobalScope.launch(Dispatchers.IO) {
            bitmapImage = model.getSqueezedBitmap(
                originalBitmapImage,
                view.getImageView()?.clipBounds,
                1024,
                1024
            )!!
            withContext(Dispatchers.Main) {
                view.setBitmap(bitmapImage)
                globalHistory = HistoryHelper(bitmapImage)
            }
        }
        historyFn = HistoryHelper { bitmapImage ->
            bitmapImage
        }
        val imageView = view.getImageView()!!
        val centerX = imageView.width.toFloat() / 2
        val centerY = imageView.height.toFloat() / 2
        onTouchListener = object : OnMoveTouchListener() {
            override fun onMoveLeft(diff: Float, x: Float, y: Float) {
                when {
                    y < centerY -> changeImageRotation(-diff)
                    y >= centerY -> changeImageRotation(diff)
                }
            }

            override fun onMoveRight(diff: Float, x: Float, y: Float) {
                when {
                    y < centerY -> changeImageRotation(diff)
                    y >= centerY -> changeImageRotation(-diff)
                }
            }

            override fun onMoveTop(diff: Float, x: Float, y: Float) {
                when {
                    x < centerX -> changeImageRotation(diff)
                    x >= centerX -> changeImageRotation(-diff)
                }
            }

            override fun onMoveBottom(diff: Float, x: Float, y: Float) {
                when {
                    x < centerX -> changeImageRotation(-diff)
                    x >= centerX -> changeImageRotation(diff)
                }
            }
        }
        bottomBar = CustomBar(
            view.getBottomBar(),
            CustomBar.Type.BOTTOM
        )
        topBar = CustomBar(
            view.getTopBar(),
            CustomBar.Type.TOP
        )
        editTopBar = CustomBar(
            view.getEditTopBar(),
            CustomBar.Type.TOP
        )
    }

    override fun onClickButtonOnBottomBar() {
        bottomBar.hide()
        topBar.hide()
        editTopBar.show()
        customBar?.show()
        localHistory = HistoryHelper(bitmapImage)
        mainBarIsHidden = true
    }

    override fun onAcceptChanges() {
        editTopBar.hide()
        customBar?.hide()
        bottomBar.show()
        topBar.show()
        globalHistory.add(bitmapImage)
        localHistory = null
        mainBarIsHidden = false
    }

    override fun onCancelChanges() {
        editTopBar.hide()
        customBar?.hide()
        bottomBar.show()
        topBar.show()
        bitmapImage = globalHistory.current()
        view.setBitmap(bitmapImage)
        localHistory = null
        mainBarIsHidden = false
    }

    override fun onUndoChanges() {
        bitmapImage = localHistory?.undo() ?: bitmapImage
        view.setBitmap(bitmapImage)
    }

    override fun onRedoChanges() {
        bitmapImage = localHistory?.redo() ?: bitmapImage
        view.setBitmap(bitmapImage)
    }

    override fun onBlackAndWhiteFilter() {
        updateBitmap(model.blackAndWhiteFilter(bitmapImage))
    }

    override fun onVioletFilter() {
        updateBitmap(model.violetFilter(bitmapImage))
    }

    override fun onContrastFilter() {
        updateBitmap(model.contrastFilter(bitmapImage))
    }

    override fun onSepiaFilter() {
        updateBitmap(model.sepiaFilter(bitmapImage))
    }

    override fun onNegativeFilter() {
        updateBitmap(model.negativeFilter(bitmapImage))
    }

    override fun getImageView(): ImageView? {
        return view.getImageView()
    }

    override fun onMaskingSeekBar(progress: Int) {
        updateBitmap(model.masking(bitmapImage, progress))
    }

    override fun onScaleSeekBar(progress: Int) {
        updateBitmap(model.scale(bitmapImage, progress))
    }

    override fun onRotate() {
        val rotateBarView = view.createView(R.layout.rotate_bar)
        customBar = CustomBar(
            rotateBarView as ConstraintLayout,
            CustomBar.Type.BOTTOM
        )
        onClickButtonOnBottomBar()
        view.setOnTouchListener(onTouchListener)
    }

    override fun onMasking() {
        val maskingBarView = view.createView(R.layout.masking_bar)
        customBar = CustomBar(
            maskingBarView as ConstraintLayout,
            CustomBar.Type.BOTTOM
        )
        val maskingSeekBar = maskingBarView.maskingDegree
        maskingSeekBar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                onMaskingSeekBar(maskingSeekBar.progress)
            }
        })
        onClickButtonOnBottomBar()
    }

    override fun onScale() {
        val scaleBarView = view.createView(R.layout.scale_bar)
        customBar = CustomBar(
            scaleBarView as ConstraintLayout,
            CustomBar.Type.BOTTOM
        )
        val scaleSeekBar = scaleBarView.scaleDegree
        scaleSeekBar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                onScaleSeekBar(scaleSeekBar.progress)
            }
        })
        onClickButtonOnBottomBar()
    }

    override fun onShapes() {
        val shapesBarView = view.createView(R.layout.shapes_bar)
        customBar = CustomBar(
            shapesBarView as ConstraintLayout,
            CustomBar.Type.BOTTOM
        )
        onClickButtonOnBottomBar()
    }

    override fun onCircle(context: Context) {
        bitmapImage = model.findCircle(bitmapImage, context)
        view.setBitmap(bitmapImage)
    }

    override fun onRectangle(context: Context) {
        bitmapImage = model.findRectangle(bitmapImage, context)
        view.setBitmap(bitmapImage)
    }

    override fun isMainBarHidden(): Boolean {
        return mainBarIsHidden
    }

    override fun onFilter() {
        val filterBarView = view.createView(R.layout.filter_bar)
        customBar = CustomBar(
            filterBarView as ConstraintLayout,
            CustomBar.Type.BOTTOM
        )
        onClickButtonOnBottomBar()
    }

    override fun save() {
        val listFn = historyFn.getList()
        listFn.forEach {
            originalBitmapImage = it(originalBitmapImage)
        }
    }

    override fun onRotateRight90() {
        updateBitmapFn { bitmap -> model.rotate90(bitmap) }
    }

    private fun updateBitmap(bitmap: Bitmap) {
        bitmapImage = bitmap
        view.setBitmap(bitmapImage)
        localHistory?.add(bitmapImage)
    }

    private fun updateBitmapFn(fn: (Bitmap) -> Bitmap) {
        bitmapImage = fn(bitmapImage)
        view.setBitmap(bitmapImage)
        localHistory?.add(bitmapImage)
    }

    private fun changeImageRotation(diff: Float) {
        when {
            imageRotation + diff / 10 > 45f -> {
                imageRotation = 45f
                view.setImageRotation(imageRotation)
            }
            imageRotation + diff / 10 < -45f -> {
                imageRotation = -45f
                view.setImageRotation(imageRotation)
            }
            else -> {
                imageRotation += diff / 10
                view.setImageRotation(imageRotation)
            }
        }
    }
}