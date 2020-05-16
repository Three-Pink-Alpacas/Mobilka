package com.example.better.editorScreen

import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.better.R
import kotlinx.android.synthetic.main.activity_editor.*
import kotlinx.android.synthetic.main.rotate_bar.*

class EditorActivityView : AppCompatActivity(), EditorContract.View {
    private var selectedImage: ImageView? = null
    private var currentImage: Bitmap? = null

    private var presenter: EditorContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    override fun getBottomBar(): LinearLayout {
        return bottomBar
    }

    override fun getRotateBar(): LinearLayout {
        return rotateBar
    }
}
