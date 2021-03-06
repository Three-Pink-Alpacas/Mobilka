package com.example.better.editorScreen

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.better.R
import com.example.better.utils.CustomBar
import kotlinx.android.synthetic.main.filter_bar.view.*
import kotlinx.android.synthetic.main.masking_bar.view.*
import kotlinx.android.synthetic.main.scale_bar.view.*
import kotlinx.coroutines.*

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
class EditorActivityPresenter(_view: EditorContract.View) : EditorContract.Presenter {
    private val view: EditorContract.View = _view
    private val model: EditorContract.Model = EditorActivityModel()
    private var originalBitmapImage: Bitmap = view.getBitmap()
    private lateinit var bitmapImage: Bitmap
    private lateinit var bitmapPrev: Bitmap
    private var imageRotation: Float = 0f
    private val bottomBar: CustomBar
    private val topBar: CustomBar
    private val editTopBar: CustomBar
    private var customBar: CustomBar? = null
    private var mainBarIsHidden = false

    private lateinit var globalHistory: HistoryHelper<Bitmap>
    private var localHistory: HistoryHelper<Bitmap>? = null
    private var historyFn: HistoryHelper<(Bitmap) -> Bitmap>
    private var globalHistoryFn: HistoryHelper<HistoryHelper<(Bitmap) -> Bitmap>>

    init {
        showProgressBar()

        GlobalScope.launch(Dispatchers.IO) {
            bitmapImage = model.getSqueezedBitmap(originalBitmapImage, view.getImageView()?.clipBounds, 256, 256)!!
            bitmapPrev = model.getSqueezedBitmap(originalBitmapImage, view.getBlackAndWhiteFiltPrev()?.clipBounds, 100, 100)!!
            withContext(Dispatchers.Main) {
                view.setBitmap(bitmapImage)
                globalHistory = HistoryHelper(bitmapImage)
                hideProgressBar()

            }
        }
        historyFn = HistoryHelper { bitmapImage ->
            bitmapImage
        }

        globalHistoryFn = HistoryHelper(HistoryHelper { bitmapImage ->
            bitmapImage
        })

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
        historyFn = HistoryHelper { bitmapImage ->
            bitmapImage
        }
        mainBarIsHidden = true
    }

    override fun onAcceptChanges() {
        editTopBar.hide()
        customBar?.hide()
        bottomBar.show()
        topBar.show()
        globalHistory.add(bitmapImage)
        globalHistoryFn.add(historyFn)
        historyFn = HistoryHelper { bitmapImage ->
            bitmapImage
        }
        localHistory = null
        mainBarIsHidden = false
        view.getApply().visibility = ImageButton.VISIBLE
    }

    override fun onCancelChanges() {
        editTopBar.hide()
        customBar?.hide()
        bottomBar.show()
        topBar.show()
        bitmapImage = globalHistory.current()
        view.setBitmap(bitmapImage)
        historyFn = HistoryHelper { bitmapImage ->
            bitmapImage
        }
        localHistory = null
        mainBarIsHidden = false
        view.getApply().visibility = ImageButton.VISIBLE
    }

    override fun onUndoChanges() {
        bitmapImage = localHistory?.undo() ?: bitmapImage
        historyFn.undo()
        view.setBitmap(bitmapImage)
    }

    override fun onRedoChanges() {
        bitmapImage = localHistory?.redo() ?: bitmapImage
        historyFn.redo()
        view.setBitmap(bitmapImage)
    }

    override fun onBlackAndWhiteFilter() {
        showProgressBar()
        CoroutineScope(Dispatchers.Default).async {
            val tmp = model.blackAndWhiteFilter(globalHistory.current())
            historyFn.replaceLast { bitmap -> model.blackAndWhiteFilter(bitmap) }
            launch(Dispatchers.Main) {
                updateBitmapReplace(tmp)
                hideProgressBar()
            }
        }
    }

    override fun onVioletFilter() {
        showProgressBar()
        CoroutineScope(Dispatchers.Default).async {
            val tmp = model.violetFilter(globalHistory.current())
            historyFn.replaceLast { bitmap -> model.violetFilter(bitmap) }
            launch(Dispatchers.Main) {
                updateBitmapReplace(tmp)
                hideProgressBar()
            }
        }
    }

    override fun onContrastFilter() {
        showProgressBar()
        CoroutineScope(Dispatchers.Default).async {
            val tmp = model.contrastFilter(globalHistory.current())
            historyFn.replaceLast { bitmap -> model.contrastFilter(bitmap) }
            launch(Dispatchers.Main) {
                updateBitmapReplace(tmp)
                hideProgressBar()
            }
        }
    }

    override fun onSepiaFilter() {
        showProgressBar()
        CoroutineScope(Dispatchers.Default).async {
            val tmp = model.sepiaFilter(globalHistory.current())
            historyFn.replaceLast { bitmap -> model.sepiaFilter(bitmap) }
            launch(Dispatchers.Main) {
                updateBitmapReplace(tmp)
                hideProgressBar()
            }
        }
    }

    override fun onNegativeFilter() {
        showProgressBar()
        CoroutineScope(Dispatchers.Default).async {
            val tmp = model.negativeFilter(globalHistory.current())
            historyFn.replaceLast { bitmap -> model.negativeFilter(bitmap) }
            launch(Dispatchers.Main) {
                updateBitmapReplace(tmp)
                hideProgressBar()
            }
        }
    }

    override fun onSaturationFilter() {
        showProgressBar()
        CoroutineScope(Dispatchers.Default).async {
            val tmp = model.saturationFilter(globalHistory.current())
            historyFn.replaceLast { bitmap -> model.saturationFilter(bitmap) }
            launch(Dispatchers.Main) {
                updateBitmapReplace(tmp)
                hideProgressBar()
            }
        }
    }

    override fun getImageView(): ImageView? {
        return view.getImageView()
    }

    override fun onMaskingSeekBar(progress: Int, text: TextView) {
        showProgressBar()
        CoroutineScope(Dispatchers.Default).async {
            val tmp = model.masking(globalHistory.current(), progress, text)
            historyFn.replaceLast { bitmap -> model.masking(bitmap, progress, text) }
            launch(Dispatchers.Main) {
                updateBitmapReplace(tmp)
                hideProgressBar()
            }
        }
    }

    override fun onScaleSeekBar(progress: Int, text: TextView) {
        showProgressBar()
        CoroutineScope(Dispatchers.Default).async {
            val tmp = model.scale(globalHistory.current(), progress, text)
            historyFn.replaceLast { bitmap -> model.scale(bitmap, progress, text) }
            launch(Dispatchers.Main) {
                updateBitmapReplace(tmp)
                hideProgressBar()
            }
        }
    }

    override fun onRotate() {
        val rotateBarView = view.createView(R.layout.rotate_bar)
        customBar = CustomBar(
            rotateBarView as ConstraintLayout,
            CustomBar.Type.BOTTOM
        )
        onClickButtonOnBottomBar()
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
                onMaskingSeekBar(maskingSeekBar.progress, maskingBarView.maskingText)
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
                onScaleSeekBar(scaleSeekBar.progress, scaleBarView.scaleText)
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

    override fun onCircle() {
        view.getApply().visibility = ImageButton.GONE
        showProgressBar()
        CoroutineScope(Dispatchers.Default).async {
            val b = bitmapImage.copy(bitmapImage.config, false)
            val tmp = model.findCircle(b)
            //historyFn.replaceLast { bitmap -> model.findCircle(bitmap.copy(bitmap.config, false)) }
            launch(Dispatchers.Main) {
                updateBitmapReplace(tmp.copy(tmp.config, false))
                hideProgressBar()
            }
        }
    }

    override fun isMainBarHidden(): Boolean {
        return mainBarIsHidden
    }

    override fun showProgressBar() {
        view.showProgressBar()
    }

    override fun hideProgressBar() {
        view.hideProgressBar()
    }

    override fun onFilter() {
        showProgressBar()
        CoroutineScope(Dispatchers.Default).async {
            val baw = model.blackAndWhiteFilter(bitmapPrev)
            val v = model.violetFilter(bitmapPrev)
            val n = model.negativeFilter(bitmapPrev)
            val c = model.contrastFilter(bitmapPrev)
            val se = model.sepiaFilter(bitmapPrev)
            val sa = model.saturationFilter(bitmapPrev)
            launch(Dispatchers.Main) {
                val filterBarView = view.createView(R.layout.filter_bar)
                filterBarView.blackAndWhiteButton.setImageBitmap(baw)
                filterBarView.violetButton.setImageBitmap(v)
                filterBarView.negativeButton.setImageBitmap(n)
                filterBarView.contrastButton.setImageBitmap(c)
                filterBarView.sepiaButton.setImageBitmap(se)
                filterBarView.saturationButton.setImageBitmap(sa)
                customBar = CustomBar(
                    filterBarView as ConstraintLayout,
                    CustomBar.Type.BOTTOM
                )
                onClickButtonOnBottomBar()
                hideProgressBar()
            }
        }
    }

    override fun save(): Bitmap {
        val globalListFn = globalHistoryFn.getList()
        globalListFn.forEach {
            val listFn = it.getList()
            listFn.forEach {
                originalBitmapImage = it(originalBitmapImage)
            }
        }
        return originalBitmapImage
    }

    override fun onRotateRight90() {
        showProgressBar()
        CoroutineScope(Dispatchers.Default).async {
            val tmp = model.rotate90(bitmapImage)
            historyFn.add { bitmap -> model.rotate90(bitmap) }
            launch(Dispatchers.Main) {
                updateBitmap(tmp)
                hideProgressBar()
            }
        }
    }

    private fun updateBitmap(bitmap: Bitmap) {
        bitmapImage = bitmap
        view.setBitmap(bitmapImage)
        localHistory?.add(bitmapImage)
    }

    private fun updateBitmapReplace(bitmap: Bitmap) {
        bitmapImage = bitmap
        view.setBitmap(bitmapImage)
        localHistory?.replaceLast(bitmapImage)
    }
}