package com.example.better.editorScreen

import android.graphics.Bitmap
import com.example.better.editorScreen.EditorContract
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class EditorActivityModel: EditorContract.Model {

    override fun rotate(bitmap: Bitmap, angle: Float): Bitmap {
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        for (x in 0..bitmap.width) {
            for (y in 0..bitmap.height) {
                val newX: Int = (x * cos(angle) - y * sin(angle)).roundToInt()
                val newY: Int = (x * sin(angle) + y * cos(angle)).roundToInt()
                if ((newX < bitmap.width && newX > 0) && (newY < bitmap.height && newY > 0)) {
                    newBitmap.setPixel(newX, newY, bitmap.getPixel(x, y))
                }
            }
        }
        return newBitmap
    }

}