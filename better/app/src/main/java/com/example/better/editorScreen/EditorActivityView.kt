package com.example.better.editorScreen

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.better.R
import com.example.better.editorScreen.presenter.EditorActivityPresenter
import kotlinx.android.synthetic.main.activity_editor.*
import me.echodev.resizer.Resizer
import java.io.File
import java.io.FileOutputStream


class EditorActivityView : AppCompatActivity(), EditorContract.View {

    private var selectedImage: ImageView? = null
    private var lowCurrentImage: Bitmap? = null
    private var fullCurrentImage: Bitmap? = null

    private var presenter: EditorContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        val imgUri = this.intent.getParcelableExtra<Uri>("img")
        fullCurrentImage = MediaStore.Images.Media.getBitmap(this.contentResolver, imgUri)
        selectedImage = findViewById(R.id.editableImage)
        selectedImage!!.setImageBitmap(fullCurrentImage)


        presenter = EditorActivityPresenter(this)
    }


    fun onRotate(view: View) {
        presenter?.onClickButtonOnBottomBar()
    }

    override fun getBottomBar(): LinearLayout {
        return bottomBar
    }
}
