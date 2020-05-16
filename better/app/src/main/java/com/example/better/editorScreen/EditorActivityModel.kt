package com.example.better.editorScreen

import android.graphics.Bitmap
import com.example.better.editorScreen.EditorContract
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class EditorActivityModel : EditorContract.Model {

    override fun rotate(bitmap: Bitmap, angle: Float): Bitmap {
        val rad = angle * PI / 180f
        val centerX = bitmap.width / 2
        val centerY = bitmap.height / 2

        val resSin = sin(rad)
        val resCos = cos(rad)

        val x1 = -bitmap.height*resSin
        val y1 = bitmap.height*resCos
        val x2 = bitmap.width*resCos - bitmap.height*resSin
        val y2 = bitmap.height*resCos + bitmap.width*resSin
        val x3 = bitmap.width*resCos
        val y3 = bitmap.width*resSin

        val minX = minOf(minOf(0.0, x1, x2), x3)
        val minY = minOf(minOf(0.0, y1, y2), y3)
        val maxX = maxOf(x1, x2, x3)
        val maxY = maxOf(y1, y2, y3)

        val newWidth = bitmap.width
        val newHeight = bitmap.height

        val newBitmap = Bitmap.createBitmap(newWidth.toInt(), newHeight.toInt(), Bitmap.Config.ARGB_8888)

        for (x in 0 until bitmap.width) {
            for (y in 0 until bitmap.height) {
                val newX: Int =
                    ((x - centerX) * resCos - (y - centerY) * resSin).roundToInt() + centerX
                val newY: Int =
                    ((x - centerX) * resSin + (y - centerY) * resCos).roundToInt() + centerY
                if ((newX in 1 until newWidth) && (newY in 1 until newHeight)) {
                    newBitmap.setPixel(newX, newY, bitmap.getPixel(x, y))
                }
            }
        }

        return newBitmap
    }

}