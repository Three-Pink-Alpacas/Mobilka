package com.example.better

import android.Manifest.permission.*
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {

    private val neededPermissions = arrayOf(CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)
    private val REQUEST_CODE = 100

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermissions()
    }

    fun cubeMove(view: View) {
        val intent = Intent(this, CubeShowingActivity::class.java);
        startActivity(intent);
    }
    fun plusMove(view: View) {}

}
