package com.example.better.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.better.R


class CubeShowingActivity : Activity() {

    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        setContentView(R.layout.activity_cube_showing)

    }
    // button for getting in main menu
    fun mainMenuMove(view: View) {
        val intent = Intent(this, MainActivity::class.java);
        startActivity(intent);
    }



}
