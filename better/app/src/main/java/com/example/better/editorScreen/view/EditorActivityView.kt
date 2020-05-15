package com.example.better.editorScreen.view

import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import com.example.better.R

class EditorActivityView : AppCompatActivity() {
    private var selectedImage: ImageView? = null
    private var currentImage: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
        selectedImage = findViewById(R.id.editableImage);

        val imgUri = this.intent.getParcelableExtra<Uri>("img")
        currentImage =
            MediaStore.Images.Media.getBitmap(this.contentResolver, imgUri)
        selectedImage!!.setImageBitmap(currentImage)
    }

    fun hideBottomPanel() {

    }

    fun onRotate(view: View) {
        hideBottomPanel()
    }
}
