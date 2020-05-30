package com.example.better.editorScreen

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.better.R
import com.example.better.mainScreen.MainActivityView
import kotlinx.android.synthetic.main.activity_editor.*
import java.io.File
import java.io.FileOutputStream
import kotlinx.android.synthetic.main.filter_bar.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.*
import java.util.*


class EditorActivityView : AppCompatActivity(), EditorContract.View {
    private fun changeStatusBarColor(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val startColor = window.statusBarColor
            val endColor = ContextCompat.getColor(context, R.color.colorStatusBarInEditor)
            ObjectAnimator.ofArgb(window, "statusBarColor", startColor, endColor).start()
        }
    }

    private var selectedImage: ImageView? = null
    private var blackAndWhiteImage: ImageView? = null


    private var currentImage: Bitmap? = null
    private var isLoading: Boolean = false

    private lateinit var presenter: EditorContract.Presenter

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeStatusBarColor(this)
        setContentView(R.layout.activity_editor)
        selectedImage = editableImage
        blackAndWhiteImage = blackAndWhiteButton
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = ProgressBar.VISIBLE
        val imgUri = this.intent.getParcelableExtra<Uri>("img")
        currentImage = MediaStore.Images.Media.getBitmap(this.contentResolver, imgUri)
        presenter = EditorActivityPresenter(this)
    }

    override fun showProgressBar() {
        progressBar.visibility = ProgressBar.VISIBLE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        isLoading = true

    }

    override fun hideProgressBar() {
        progressBar.visibility = ProgressBar.INVISIBLE
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        isLoading = false
    }

    fun onRotate(view: View) {
        presenter.onRotate()
    }

    fun onRotateRight90(view: View) {
        presenter.onRotateRight90()
    }

    override fun onBackPressed() {
        if (!isLoading) {
            if (presenter.isMainBarHidden()) {
                presenter.onCancelChanges()
            } else {
                mainMenuMove(View(this))
            }
        }
    }

    fun onAcceptChanges(view: View) {
        presenter.onAcceptChanges()
    }

    fun onCancelChanges(view: View) {
        presenter.onCancelChanges()
    }

    fun onUndo(view: View) {
        presenter.onUndoChanges()
    }

    fun onRedo(view: View) {
        presenter.onRedoChanges()
    }

    // button for getting in main menu
    fun mainMenuMove(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Leave editor?")

        builder.setPositiveButton(android.R.string.yes) { _, _ ->
            val intent = Intent(this, MainActivityView::class.java)
            startActivity(intent)
            finish()
        }

        builder.setNegativeButton(android.R.string.no) { _, _ -> }
        builder.show()
    }

    fun saveImage(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Save image?")



        builder.setPositiveButton(android.R.string.yes) { _, _ ->
            showProgressBar()
            val root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            ).toString()
            val myDir = File("$root/saved_images")
            myDir.mkdirs()

            val fname = "${UUID.randomUUID()}.jpg"
            val file = File(myDir, fname)
            CoroutineScope(Dispatchers.Default).async {
                val finalBitmap = presenter.save()
                launch(Dispatchers.Main) {

                    if (file.exists()) file.delete()
                    try {
                        val out = FileOutputStream(file)
                        finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)

                        out.flush()
                        out.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    MediaScannerConnection.scanFile(applicationContext, arrayOf(file.toString()), null
                    ) { path, uri ->
                        Log.i("ExternalStorage", "Scanned $path:")
                        Log.i("ExternalStorage", "-> uri=$uri")
                    }
                    hideProgressBar()
                    mainMenuMove(view)
                }

            }

        }

        builder.setNegativeButton(android.R.string.no) { _, _ -> }
        builder.show()
    }

    fun onFilter(view: View) {
        presenter.onFilter()
    }

    fun onMasking(view: View) {
        presenter.onMasking()
    }

    override fun getBottomBar(): ConstraintLayout {
        return bottomBarView
    }


    fun onScale(view: View) {
        presenter.onScale()
    }

    fun onShapes(view: View) {
        presenter.onShapes()
    }

    fun onCircle(view: View) {
        val context: Context = applicationContext
        presenter.onCircle(context)
    }

    override fun getTopBar(): ConstraintLayout {
        return topBar as ConstraintLayout
    }

    override fun setBitmap(bitmap: Bitmap) {
        editableImage.setImageBitmap(bitmap)
    }

    override fun getBitmap(): Bitmap {
        return currentImage!!
    }


    override fun setImageRotation(angle: Float) {
        selectedImage?.rotation = angle
    }

    override fun getImageView(): ImageView? {
        return selectedImage
    }
    override fun getBlackAndWhiteFiltPrev(): ImageView?
    {
        return blackAndWhiteImage
    }

    override fun getEditTopBar(): ConstraintLayout {
        return editTopBar
    }

    fun onClickBlackAndWhiteFilter(view: View) {
        presenter.onBlackAndWhiteFilter()
    }

    fun onClickVioletFilter(view: View) {
        presenter.onVioletFilter()
    }

    fun onClickNegativeFilter(view: View) {
        presenter.onNegativeFilter()
    }

    fun onClickContrastFilter(view: View) {
        presenter.onContrastFilter()
    }

    fun onClickSepiaFilter(view: View) {
        presenter.onSepiaFilter()
    }

    fun onClickSaturationFilter(view: View) {
        presenter.onSaturationFilter()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun setOnTouchListener(onTouchListener: View.OnTouchListener) {
        selectedImage?.setOnTouchListener(onTouchListener)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun detachOnTouchListener() {
        selectedImage?.setOnTouchListener(null)
    }

    override fun createView(resource: Int): View {
        val view = LayoutInflater.from(this).inflate(resource, mainLayout, false)
        mainLayout.addView(view)
        return view
    }
}
