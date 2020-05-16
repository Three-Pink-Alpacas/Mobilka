package com.example.better.editorScreen

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.better.R
import com.example.better.mainScreen.MainActivityView
import kotlinx.android.synthetic.main.activity_editor.*
import kotlinx.android.synthetic.main.rotate_bar.*


class EditorActivityView : AppCompatActivity(), EditorContract.View {
    fun changeStatusBarColor(context: Context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val startColor = window.statusBarColor
            val endColor = ContextCompat.getColor(context, R.color.colorStatusBarInEditor)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            ObjectAnimator.ofArgb(window, "statusBarColor", startColor, endColor).start()
        }
    }
    private var selectedImage: ImageView? = null
    private var currentImage: Bitmap? = null

    private var presenter: EditorContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeStatusBarColor(this)
        setContentView(R.layout.activity_editor)
        selectedImage = editableImage

        val imgUri = this.intent.getParcelableExtra<Uri>("img")
        currentImage =
            MediaStore.Images.Media.getBitmap(this.contentResolver, imgUri)
        selectedImage!!.setImageBitmap(currentImage)

        presenter = EditorActivityPresenter(this)
    }

    fun onRotate(view: View) {
        presenter?.onRotate()
    }



    // button for getting in main menu
    fun mainMenuMove(view: View) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("are you sure?")
        builder.setMessage("changes will not be saved.")


        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            val intent = Intent(this, MainActivityView::class.java);
            startActivity(intent);
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which -> }
        builder.show()
    }

    override fun getBottomBar(): LinearLayout {
        return bottomBar
    }

    override fun getRotateBar(): LinearLayout {
        return rotateBar
    }
}
