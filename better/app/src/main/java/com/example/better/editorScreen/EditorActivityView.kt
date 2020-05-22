package com.example.better.editorScreen

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.better.R
import com.example.better.mainScreen.MainActivityView
import kotlinx.android.synthetic.main.activity_editor.*
import kotlinx.android.synthetic.main.filter_bar.*
import kotlinx.android.synthetic.main.masking_bar.*
import kotlinx.android.synthetic.main.rotate_bar.*
import kotlinx.android.synthetic.main.scale_bar.*


class EditorActivityView : AppCompatActivity(), EditorContract.View {
    private fun changeStatusBarColor(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val startColor = window.statusBarColor
            val endColor = ContextCompat.getColor(context, R.color.colorStatusBarInEditor)
            ObjectAnimator.ofArgb(window, "statusBarColor", startColor, endColor).start()
        }
    }

    private var selectedImage: ImageView? = null
    private var currentImage: Bitmap? = null

    private lateinit var presenter: EditorContract.Presenter

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeStatusBarColor(this)
        setContentView(R.layout.activity_editor)
        selectedImage = editableImage

        val imgUri = this.intent.getParcelableExtra<Uri>("img")
        currentImage = MediaStore.Images.Media.getBitmap(this.contentResolver, imgUri)
        presenter = EditorActivityPresenter(this)
    }

    fun onRotate(view: View) {
        presenter.onRotate()
    }

    fun onRotateRight90(view: View) {
        presenter.onRotateRight90()
    }

    override fun onBackPressed() {
        mainMenuMove(View(this))
    }

    // button for getting in main menu
    fun mainMenuMove(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Are you sure?")
        builder.setMessage("Changes will not be saved.")

        builder.setPositiveButton(android.R.string.yes) { _, _ ->
            val intent = Intent(this, MainActivityView::class.java)
            startActivity(intent)
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
