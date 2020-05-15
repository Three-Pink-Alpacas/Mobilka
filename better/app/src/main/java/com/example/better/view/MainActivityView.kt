package com.example.better.view

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.better.EditorActivity
import com.example.better.R
import com.example.better.contract.MainContract
import com.example.better.presenter.MainActivityPresenter
import java.io.File

class MainActivityView : AppCompatActivity(), MainContract.View_ {
    private val REQUEST_PERMISSION_CODE = 100
    private val OPEN_GALLERY_CODE = 1
    private val OPEN_CAMERA_CODE = 2

    private var selectedImage: ImageView? = null
    private var currentImage: Bitmap? = null

    private var presenter: MainActivityPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        selectedImage = findViewById(R.id.selectedImage);

        presenter = MainActivityPresenter(this)
    }

    fun cubeMove(view: View) {
        val intent = Intent(this, CubeActivityView::class.java);
        startActivity(intent);
    }

    fun plusMove(view: View) {}
    fun galleryOpen(view: View) {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, OPEN_GALLERY_CODE)
    }

    fun cameraOpen(view: View) {
        val photoMakerIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file = File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg")
        val uri = FileProvider.getUriForFile(
            this,
            this.applicationContext.packageName + ".provider",
            file
        )
        photoMakerIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(photoMakerIntent, OPEN_CAMERA_CODE)
    }

    private fun startEditor(imageUri: Uri) {
        val intent = Intent(this, EditorActivity::class.java)
        intent.putExtra("img", imageUri)
        startActivity(intent)
    }

    override fun checkHasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun requestPermissions(permissions: Array<String>) {
        ActivityCompat.requestPermissions(
            this@MainActivityView,
            permissions,
            REQUEST_PERMISSION_CODE
        )
    }

    override fun absoluteDifferenceX(): Float {
        TODO("Not yet implemented")
    }

    override fun absoluteDifferenceY(): Float {
        TODO("Not yet implemented")
    }

    override fun updateViewData() {
        TODO("Not yet implemented")
    }

    override fun initView() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            OPEN_CAMERA_CODE -> if (resultCode === Activity.RESULT_OK) {
                //File object of camera image
                val file = File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg")
                //Uri of camera image
                val photoUri = FileProvider.getUriForFile(
                    this,
                    this.applicationContext.packageName + ".provider",
                    file
                )
                if (photoUri != null) {
                    try {
                        currentImage =
                            MediaStore.Images.Media.getBitmap(this.contentResolver, photoUri)
                        selectedImage!!.setImageBitmap(currentImage)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            OPEN_GALLERY_CODE -> if (resultCode === Activity.RESULT_OK) {
                val photoUri = data!!.data
                if (photoUri != null) {
                    startEditor(photoUri)
                    try {
                        currentImage =
                            MediaStore.Images.Media.getBitmap(this.contentResolver, photoUri)
                        selectedImage!!.setImageBitmap(currentImage)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (currentImage != null) {
            currentImage!!.recycle()
            currentImage = null
            System.gc()
        }
    }
}
