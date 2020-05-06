package com.example.better

import android.Manifest.permission.*
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {

    private val neededPermissions = arrayOf(CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)
    private val REQUEST_CODE = 100

    private var selectedImage: ImageView? = null
    private var currentImage: Bitmap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        selectedImage = findViewById(R.id.selectedImage);
        checkPermissions()
    }

    fun cubeMove(view: View) {
        val intent = Intent(this, CubeShowingActivity::class.java);
        startActivity(intent);
    }
    fun plusMove(view: View) {}
    fun galleryOpen(view: View) {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, 1)
    }

    private fun checkPermissions() {
        val currentAPIVersion = Build.VERSION.SDK_INT
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            val permissionsNotGranted = ArrayList<String>()
            for (permission in neededPermissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED
                ) {
                    permissionsNotGranted.add(permission)
                }
            }
            val arr = arrayOfNulls<String>(permissionsNotGranted.size)
            if (permissionsNotGranted.size > 0) {
                ActivityCompat.requestPermissions(this@MainActivity, permissionsNotGranted.toArray(arr), REQUEST_CODE)
            }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val photoUri = data!!.data
            if (photoUri != null) {
                try {
                    currentImage = MediaStore.Images.Media.getBitmap(this.contentResolver, photoUri)
                    selectedImage!!.setImageBitmap(currentImage)
                } catch (e: Exception) {
                    e.printStackTrace()
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
