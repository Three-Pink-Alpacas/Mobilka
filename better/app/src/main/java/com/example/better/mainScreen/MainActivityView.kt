package com.example.better.mainScreen

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.better.CubeFragment
import com.example.better.R
import com.example.better.StartFragment
import com.example.better.editorScreen.EditorActivityView
import com.example.better.utils.CustomBar
import com.example.better.utils.CustomViewPager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivityView : AppCompatActivity(), MainContract.View_ {
    private val REQUEST_PERMISSION_CODE = 100
    private val OPEN_GALLERY_CODE = 1
    private val OPEN_CAMERA_CODE = 2

    var presenter = MainActivityPresenter(this)
    private fun changeStatusBarColor(context: Context, num:Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val startColor: Int
            val endColor: Int
            if (num==0)
            {
                startColor = window.statusBarColor
                endColor = ContextCompat.getColor(context, R.color.colorStatusBarInEditor)
            }
            else if (num==1)
            {
                startColor = window.statusBarColor
                endColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)
            }
            else
            {
                startColor = window.statusBarColor
                endColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)
            }
            ObjectAnimator.ofArgb(window, "statusBarColor", startColor, endColor).start()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.checkPermissions()
        val adapter = MyAdapter(supportFragmentManager)
        val viewPager: CustomViewPager = findViewById(R.id.viewpager)
        var houseButton: Button = findViewById(R.id.houseButton)
        var universeButton: Button = findViewById(R.id.moveToCanvasButton)

        viewPager.addOnPageChangeListener(object : OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {

            }
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    1 ->{houseButton.isEnabled = false
                        universeButton.isEnabled = true
                        changeStatusBarColor(this@MainActivityView, 1)
                        viewPager.setPagingEnabled(true)}
                    0 -> {houseButton.isEnabled = true
                        universeButton.isEnabled = false
                        changeStatusBarColor(this@MainActivityView, 0)
                        viewPager.setPagingEnabled(false)}
                }
            }
        })
        viewPager.adapter = adapter // устанавливаем адаптер
        viewPager.currentItem = 1 // выводим main экран
    }


    class MyAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm){
        override fun getCount(): Int {
            return 2
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                1 -> StartFragment()
                0 -> CubeFragment()
                else -> StartFragment()
            }
        }
    }

    fun cubeMove(view: View) {
        val viewPager: ViewPager = findViewById(R.id.viewpager)
        viewPager.setCurrentItem(0)
    }
    fun homeMove(view: View)
    {
        val viewPager: ViewPager = findViewById(R.id.viewpager)
        viewPager.setCurrentItem(1)
    }
    fun plusMove(view: View) {
        val viewPlus = this.plusView as ConstraintLayout
        val animator: ValueAnimator = ValueAnimator()
        animator.addUpdateListener {
            val value = it.animatedValue as Float
            viewPlus.translationY = value
        }
        animator.interpolator = LinearInterpolator()
        animator.duration = 300
        animator.setFloatValues(viewPlus.translationY, 450f)
        animator.start()
    }
    override fun onBackPressed() {
        val viewPlus = this.plusView as ConstraintLayout
        val animator = ValueAnimator()
        animator.addUpdateListener {
            val value = it.animatedValue as Float
            viewPlus.translationY = value
        }
        animator.interpolator = LinearInterpolator()
        animator.duration = 300
        animator.setFloatValues(viewPlus.translationY, viewPlus.height.toFloat())
        animator.start()
    }
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
        val intent = Intent(this, EditorActivityView::class.java)
        intent.putExtra("img", imageUri)
        startActivity(intent)
    }

    override fun checkHasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun requestPermissions(permissions: Array<String>) {
        ActivityCompat.requestPermissions(this@MainActivityView, permissions, REQUEST_PERMISSION_CODE)
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
                val photoUri = FileProvider.getUriForFile(this, this.applicationContext.packageName + ".provider", file)
                if (photoUri != null) {
                    try {
                        startEditor(photoUri)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            OPEN_GALLERY_CODE -> if (resultCode === Activity.RESULT_OK) {
                val photoUri = data!!.data
                if (photoUri != null) {
                    try {
                        startEditor(photoUri)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
    }
}
