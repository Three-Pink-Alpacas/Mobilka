package com.example.better.editorScreen

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.math.*


class EditorActivityModel : EditorContract.Model {
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)

    override fun rotate(bitmap: Bitmap, angle: Float): Bitmap {
        val rad = angle * PI / 180f
        val centerX = bitmap.width / 2
        val centerY = bitmap.height / 2

        val resSin = sin(rad)
        val resCos = cos(rad)

        val newWidth = bitmap.width
        val newHeight = bitmap.height

        val newBitmap =
            Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888)

        var newX: Int
        var newY: Int
        for (x in 0 until bitmap.width) {
            for (y in 0 until bitmap.height) {
                newX =
                    ((x - centerX) * resCos - (y - centerY) * resSin).roundToInt() + centerX
                newY =
                    ((x - centerX) * resSin + (y - centerY) * resCos).roundToInt() + centerY
                if ((newX in 1 until newWidth) && (newY in 1 until newHeight)) {
                    newBitmap.setPixel(newX, newY, bitmap.getPixel(x, y))
                }
            }
        }
        return newBitmap
    }

    override fun rotate90(bitmap: Bitmap): Bitmap {
        val arr = IntArray(bitmap.height * bitmap.width)
        val newArr = IntArray(bitmap.height * bitmap.width)

        bitmap.getPixels(arr, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        var newX: Int
        var newY: Int

        for (x in 0 until bitmap.width) {
            for (y in 0 until bitmap.height) {
                newX = bitmap.height - y - 1
                newY = x
                newArr[bitmap.height * newY + newX] = arr[bitmap.width * y + x]
            }
        }

        return Bitmap.createBitmap(
            newArr,
            0,
            bitmap.height,
            bitmap.height,
            bitmap.width,
            Bitmap.Config.ARGB_8888
        )
    }

    override fun blackAndWhiteFilter(bitmap: Bitmap): Bitmap {
        val arr = IntArray(bitmap.height * bitmap.width)
        val newArr = IntArray(bitmap.height * bitmap.width)

        bitmap.getPixels(arr, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        var pixel: Int
        var r: Float
        var g: Float
        var b: Float
        for (x in 0 until bitmap.width) {
            for (y in 0 until bitmap.height) {
                pixel = arr[bitmap.width * y + x]
                r = (pixel and 0x00FF0000 shr 16).toFloat()
                g = (pixel and 0x0000FF00 shr 8).toFloat()
                b = (pixel and 0x000000FF).toFloat()

                r = (r + g + b) / 3.0f
                g = r
                b = r
                newArr[bitmap.width * y + x] =
                    -0x1000000 or (r.toInt() shl 16) or (g.toInt() shl 8) or b.toInt()
            }
        }
        return Bitmap.createBitmap(
            newArr,
            0,
            bitmap.width,
            bitmap.width,
            bitmap.height,
            Bitmap.Config.ARGB_8888
        )
    }

    override fun violetFilter(bitmap: Bitmap): Bitmap {
        val arr = IntArray(bitmap.height * bitmap.width)
        val newArr = IntArray(bitmap.height * bitmap.width)

        bitmap.getPixels(arr, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        var pixel: Int
        var r: Int
        var g: Int
        var b: Int
        for (x in 0 until bitmap.width) {
            for (y in 0 until bitmap.height) {
                pixel = arr[bitmap.width * y + x]
                r = Color.red(pixel)
                g = ((pixel and 0x0000FF00 shr 8) - 20 * 128 / 100)
                b = Color.blue(pixel)

                g = max(0,min(g,255))
                newArr[bitmap.width * y + x] = Color.rgb(r, g, b)
            }
        }
        return Bitmap.createBitmap(
            newArr,
            0,
            bitmap.width,
            bitmap.width,
            bitmap.height,
            Bitmap.Config.ARGB_8888
        )
    }

    override fun negativeFilter(bitmap: Bitmap): Bitmap {
        val arr = IntArray(bitmap.height * bitmap.width)
        val newArr = IntArray(bitmap.height * bitmap.width)

        bitmap.getPixels(arr, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        var pixel: Int
        var r: Int
        var g: Int
        var b: Int
        for (x in 0 until bitmap.width) {
            for (y in 0 until bitmap.height) {
                pixel = arr[bitmap.width * y + x]
                r = 255 - (pixel and 0x00FF0000 shr 16)
                g = 255 - (pixel and 0x0000FF00 shr 8)
                b = 255 - (pixel and 0x000000FF)

                newArr[bitmap.width * y + x] = Color.rgb(r, g, b)
            }
        }
        return Bitmap.createBitmap(
            newArr,
            0,
            bitmap.width,
            bitmap.width,
            bitmap.height,
            Bitmap.Config.ARGB_8888
        )
    }

    override fun contrastFilter(bitmap: Bitmap): Bitmap {
        val arr = IntArray(bitmap.height * bitmap.width)
        val newArr = IntArray(bitmap.height * bitmap.width)
        val k = 50

        bitmap.getPixels(arr, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        var pixel: Int
        var r: Int
        var g: Int
        var b: Int
        for (x in 0 until bitmap.width) {
            for (y in 0 until bitmap.height) {
                pixel = arr[bitmap.width * y + x]
                r = (((pixel and 0x00FF0000 shr 16)*100 - 125*k)/k)
                g = (((pixel and 0x0000FF00 shr 8)*100 - 125*k)/k)
                b = (((pixel and 0x000000FF)*100 - 125*k)/k)

                r = max(0,min(r,255))
                g = max(0,min(g,255))
                b = max(0,min(b,255))
                newArr[bitmap.width * y + x] = Color.rgb(r, g, b)
            }
        }
        return Bitmap.createBitmap(
            newArr,
            0,
            bitmap.width,
            bitmap.width,
            bitmap.height,
            Bitmap.Config.ARGB_8888
        )
    }

    override fun sepiaFilter(bitmap: Bitmap): Bitmap {
        val arr = IntArray(bitmap.height * bitmap.width)
        val newArr = IntArray(bitmap.height * bitmap.width)

        bitmap.getPixels(arr, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        var pixel: Int
        var r: Int
        var g: Int
        var b: Int
        for (x in 0 until bitmap.width) {
            for (y in 0 until bitmap.height) {
                pixel = arr[bitmap.width * y + x]
                r = pixel and 0x00FF0000 shr 16
                g = pixel and 0x0000FF00 shr 8
                b = pixel and 0x000000FF

                r = ((r * 0.393) + (g * 0.769) + (b * 0.189)).toInt()
                g = ((r * 0.349) + (g * 0.686) + (b * 0.168)).toInt()
                b = ((r * 0.272) + (g * 0.534) + (b * 0.131)).toInt()

                r = max(0,min(r,255))
                g = max(0,min(g,255))
                b = max(0,min(b,255))

                newArr[bitmap.width * y + x] = Color.rgb(r, g, b)
            }
        }
        return Bitmap.createBitmap(
            newArr,
            0,
            bitmap.width,
            bitmap.width,
            bitmap.height,
            Bitmap.Config.ARGB_8888
        )
    }

    override fun saturationFilter(bitmap: Bitmap): Bitmap {
        val arr = IntArray(bitmap.height * bitmap.width)
        val newArr = IntArray(bitmap.height * bitmap.width)

        bitmap.getPixels(arr, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        var pixel: Int
        var r: Int
        var g: Int
        var b: Int
        val k = 2
        val HSV: FloatArray = floatArrayOf(0f, 0f, 0f)
        var newHSV: FloatArray
        var newPixel: Int
        for (x in 0 until bitmap.width) {
            for (y in 0 until bitmap.height) {
                pixel = arr[bitmap.width * y + x]
                r = pixel and 0x00FF0000 shr 16
                g = pixel and 0x0000FF00 shr 8
                b = pixel and 0x000000FF

                Color.RGBToHSV(r, g, b, HSV)
                newHSV = floatArrayOf(HSV[0], max(0f,min(HSV[1]*k,1f)), HSV[2])
                newPixel = Color.HSVToColor(newHSV)

                r = newPixel and 0x00FF0000 shr 16
                g = newPixel and 0x0000FF00 shr 8
                b = newPixel and 0x000000FF

                r = max(0,min(r,255))
                g = max(0,min(g,255))
                b = max(0,min(b,255))

                newArr[bitmap.width * y + x] = Color.rgb(r, g, b)
            }
        }
        return Bitmap.createBitmap(
            newArr,
            0,
            bitmap.width,
            bitmap.width,
            bitmap.height,
            Bitmap.Config.ARGB_8888
        )
    }

    @SuppressLint("SetTextI18n")
    override fun masking(bitmap: Bitmap, progress: Int, text: TextView): Bitmap {
        if (progress == 0) {
            text.text = "×0"
            return bitmap
        }
        val arr = IntArray(bitmap.height * bitmap.width)
        val newArr = IntArray(bitmap.height * bitmap.width)
        bitmap.getPixels(arr, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        val sharpenForce = progress*0.5.toFloat()
        val kernel = arrayOf(
            floatArrayOf(0f, -1 * sharpenForce, 0f),
            floatArrayOf(-1 * sharpenForce, 4 * sharpenForce + 1, -1 * sharpenForce),
            floatArrayOf(0f, -1 * sharpenForce, 0f)
        )
        var newR: Int
        var newG: Int
        var newB: Int
        var pixel: Int
        var r: Int
        var g: Int
        var b: Int
        for (y in 1 until bitmap.height - 1) {
            for (x in 1 until bitmap.width - 1) {
                newR = 0
                newG = 0
                newB = 0
                for (yk in -1..1) {
                    for (xk in -1..1) {
                        pixel = arr[bitmap.width*(y + yk) + x + xk]
                        r = (pixel and 0x00FF0000 shr 16)
                        g = (pixel and 0x0000FF00 shr 8)
                        b = (pixel and 0x000000FF)

                        newR += (kernel[yk + 1][xk + 1] * r).toInt()
                        newG += (kernel[yk + 1][xk + 1] * g).toInt()
                        newB += (kernel[yk + 1][xk + 1] * b).toInt()
                    }
                }
                newR = max(0,min(newR,255))
                newG = max(0,min(newG,255))
                newB = max(0,min(newB,255))

                newArr[bitmap.width * y + x] = Color.rgb(newR, newG, newB)
            }
        }
        text.text = "×$sharpenForce"
        return Bitmap.createBitmap(
            newArr,
            0,
            bitmap.width,
            bitmap.width,
            bitmap.height,
            Bitmap.Config.ARGB_8888
        )
    }

    override fun getSqueezedBitmap(
        originalBitmapImage: Bitmap,
        rect: Rect?,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap? {
        return BitmapFactory.Options().run {
            val stream = ByteArrayOutputStream()

            // Calculate inSampleSize
            inSampleSize = calculateInSampleSize(originalBitmapImage.width, originalBitmapImage.height, reqWidth, reqHeight)

            originalBitmapImage.compress(Bitmap.CompressFormat.PNG, 25, stream)

            BitmapFactory.decodeStream(ByteArrayInputStream(stream.toByteArray()), rect, this)
        }
    }

    private fun calculateInSampleSize(
        width: Int,
        height: Int,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }


    @SuppressLint("SetTextI18n")
    override fun scale(bitmap: Bitmap, progress: Int, text: TextView): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val arr = IntArray(height * width)
        bitmap.getPixels(arr, 0, width, 0, 0, width, height)
        val coeff: Double = when (progress) {
            0 -> 0.125
            1 -> 0.25
            2 -> 0.5
            3 -> 0.625
            5 -> 1.05
            6 -> 1.1
            7 -> 1.15
            8 -> 1.2
            else -> {
                text.text = "×1"
                return bitmap
            }
        }
        val newWidth = (width * coeff).toInt()
        val newHeight = (height * coeff).toInt()
        val newArr = IntArray(newHeight * newWidth)

        var a: Int
        var b: Int
        var c: Int
        var d: Int
        var x: Int
        var y: Int
        var index: Int
        val xRatio: Float = (width - 1).toFloat() / newWidth
        val yRatio: Float = (height - 1).toFloat() / newHeight
        var xDiff: Float
        var yDiff: Float
        var blue: Float
        var red: Float
        var green: Float
        var offset = 0
        for (i in 0 until newHeight) {
            for (j in 0 until newWidth) {
                x = (xRatio * j).toInt()
                y = (yRatio * i).toInt()
                xDiff = xRatio * j - x
                yDiff = yRatio * i - y
                index = (y * width + x)
                a = arr[index]
                b = arr[index + 1]
                c = arr[index + width]
                d = arr[index + width + 1]

                blue = (a and 0xff) * (1 - xDiff) * (1 - yDiff) + (b and 0xff) * xDiff * (1 - yDiff) + (c and 0xff) * yDiff * (1 - xDiff) + (d and 0xff) * (xDiff * yDiff)
                green = (a shr 8 and 0xff) * (1 - xDiff) * (1 - yDiff) + (b shr 8 and 0xff) * xDiff * (1 - yDiff) + (c shr 8 and 0xff) * yDiff * (1 - xDiff) + (d shr 8 and 0xff) * (xDiff * yDiff)
                red = (a shr 16 and 0xff) * (1 - xDiff) * (1 - yDiff) + (b shr 16 and 0xff) * xDiff * (1 - yDiff) + (c shr 16 and 0xff) * yDiff * (1 - xDiff) + (d shr 16 and 0xff) * (xDiff * yDiff)

                newArr[offset++] = -0x1000000 or
                        (red.toInt() shl 16 and 0xff0000) or
                        (green.toInt() shl 8 and 0xff00) or
                        blue.toInt()
            }
        }
        text.text = "×$coeff"
        return Bitmap.createBitmap(
            newArr,
            0,
            newWidth,
            newWidth,
            newHeight,
            Bitmap.Config.ARGB_8888
        )
    }

    override fun findCircle(bitmap: Bitmap, context: Context): Bitmap {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
        val gray = Mat()
        val tmp = Mat()
        Utils.bitmapToMat(bitmap, tmp)
        cvtColor(tmp, gray, COLOR_BGR2GRAY)
        medianBlur(gray, gray, 5)
        val circles = Mat()
        HoughCircles(
            gray, circles, HOUGH_GRADIENT, 1.0,
            gray.rows()/8.toDouble(),
            100.0, 30.0, 1, 1000
        )
        var c: DoubleArray
        var center: Point
        var radius: Int
        if (circles.cols() !=0) {
            for (x in 0 until circles.cols()) {
                c = circles[0, x]
                center = Point(c[0].roundToInt().toDouble(), c[1].roundToInt().toDouble())
                circle(tmp, center, 1, Scalar(0.0, 100.0, 100.0), 3, 8, 0)

                radius = c[2].roundToInt()
                circle(tmp, center, radius, Scalar(255.0, 0.0, 255.0), 3, 8, 0)
            }
            val newBitmap: Bitmap = bitmap
            Utils.matToBitmap(tmp, newBitmap)
            return newBitmap
        }
        else {
            val text = "No circles found"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(context, text, duration)
            toast.show()
            return bitmap
        }
    }
}