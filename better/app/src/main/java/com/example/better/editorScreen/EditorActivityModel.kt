package com.example.better.editorScreen

import android.R.color
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import androidx.annotation.RequiresApi
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.*


class EditorActivityModel : EditorContract.Model {

    override fun rotate(bitmap: Bitmap, angle: Float): Bitmap {
        val rad = angle * PI / 180f
        val centerX = bitmap.width / 2
        val centerY = bitmap.height / 2

        val resSin = sin(rad)
        val resCos = cos(rad)

        val x1 = -bitmap.height * resSin
        val y1 = bitmap.height * resCos
        val x2 = bitmap.width * resCos - bitmap.height * resSin
        val y2 = bitmap.height * resCos + bitmap.width * resSin
        val x3 = bitmap.width * resCos
        val y3 = bitmap.width * resSin

        val minX = minOf(minOf(0.0, x1, x2), x3)
        val minY = minOf(minOf(0.0, y1, y2), y3)
        val maxX = maxOf(x1, x2, x3)
        val maxY = maxOf(y1, y2, y3)

        val newWidth = bitmap.width
        val newHeight = bitmap.height

        val newBitmap =
            Bitmap.createBitmap(newWidth.toInt(), newHeight.toInt(), Bitmap.Config.ARGB_8888)

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

        println(newHeight*newWidth)

        return newBitmap
    }

    override fun blackAndWhiteFilter(bitmap: Bitmap): Bitmap {
        val oldBitmap = bitmap
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        for (x in 0..(bitmap.width-1)) {
            for (y in 0..(bitmap.height-1)) {
                val pixel=oldBitmap.getPixel(x,y)
                var r = (pixel and 0x00FF0000 shr 16).toFloat()
                var g= (pixel and 0x0000FF00 shr 8).toFloat()
                var b = (pixel and 0x000000FF).toFloat()

                r = (r + g + b) / 3.0f
                g = r
                b = r
                val newPixel = -0x1000000 or (r.toInt() shl 16) or (g.toInt() shl 8) or b.toInt()
               newBitmap.setPixel(x,y,newPixel)
            }
        }
        return newBitmap
    }
    override fun violetFilter(bitmap: Bitmap): Bitmap {
        val oldBitmap = bitmap
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        for (x in 0..(bitmap.width-1)) {
            for (y in 0..(bitmap.height-1)) {
                val pixel=oldBitmap.getPixel(x,y)
                val a = Color.alpha(pixel)
                val r = Color.red(pixel)
                var g = ((pixel and 0x0000FF00 shr 8) - 20 * 128 / 100) as Int
                val b = Color.blue(pixel)

                if (g<0)  g = 0  else if (g>255)  g=255
                val newPixel = Color.argb(a,r,g,b)
                newBitmap.setPixel(x,y,newPixel)
            }
        }
        return newBitmap
    }
    override fun negativeFilter(bitmap: Bitmap): Bitmap {
        val oldBitmap = bitmap
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        for (x in 0..(bitmap.width-1)) {
            for (y in 0..(bitmap.height-1)) {
                val pixel=oldBitmap.getPixel(x,y)
                val r = 255 - (pixel and 0x00FF0000 shr 16)
                val g = 255 - (pixel and 0x0000FF00 shr 8)
                val b = 255 - (pixel and 0x000000FF)

                val newPixel = Color.rgb(r,g,b)
                newBitmap.setPixel(x,y,newPixel)
            }
        }
        return newBitmap
    }

    override fun masking(bitmap: Bitmap, progress: Int): Bitmap {
        val oldBitmap = bitmap
        val newBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true)
        val sharpenForce = progress.toFloat()
        val kernel = arrayOf(
            floatArrayOf(0f, -1 * sharpenForce, 0f),
            floatArrayOf(-1 * sharpenForce, 4 * sharpenForce + 1, -1 * sharpenForce),
            floatArrayOf(0f, -1 * sharpenForce, 0f)
        )
        for (y in 1 until oldBitmap.height - 1) {
            for (x in 1 until oldBitmap.width - 1) {
                var newR = 0
                var newG = 0
                var newB = 0
                for (yk in -1..1) {
                    for (xk in -1..1) {
                        val pixel=oldBitmap.getPixel(x+xk, y+yk)
                        val r: Float = Color.red(pixel).toFloat()
                        val g: Float = Color.green(pixel).toFloat()
                        val b: Float = Color.blue(pixel).toFloat()
                        newR += (kernel[yk + 1][xk + 1] * r).toInt()
                        newG += (kernel[yk + 1][xk + 1] * g).toInt()
                        newB += (kernel[yk + 1][xk + 1] * b).toInt()
                    }
                }
                if (newR<0) newR=0 else if (newR>255) newR=255
                if (newG<0) newG=0 else if (newG>255) newG=255
                if (newB<0) newB=0 else if (newB>255) newB=255

                val newPixel = Color.rgb(newR, newG, newB)
                newBitmap.setPixel(x, y, newPixel)
            }
        }
        return newBitmap
    }


    override fun getSqueezedBitmap(originalBitmapImage: Bitmap, rect:Rect?): Bitmap {
        var bitmapImage: Bitmap? = null
        val runnable = Runnable {
            val stream = ByteArrayOutputStream()
            val options = BitmapFactory.Options()
            options.inSampleSize = 6
            originalBitmapImage.compress(Bitmap.CompressFormat.PNG, 50, stream)
            bitmapImage = BitmapFactory.decodeStream(
                ByteArrayInputStream(stream.toByteArray()), rect,
                options
            )!!

        }
        val thread = Thread(runnable)
        thread.start()
        thread.join()
        return if (bitmapImage!=null)
            bitmapImage as Bitmap
        else
            originalBitmapImage
    }
}