package com.example.better.editorScreen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import android.widget.Toast
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.opencv.imgproc.Imgproc.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.math.*


class EditorActivityModel : EditorContract.Model {

    override fun rotate(bitmap: Bitmap, angle: Float): Bitmap {
        val rad = angle * PI / 180f
        val centerX = bitmap.width / 2
        val centerY = bitmap.height / 2

        val resSin = sin(rad)
        val resCos = cos(rad)

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

        for (x in 0 until bitmap.width) {
            for (y in 0 until bitmap.height) {
                val pixel = arr[bitmap.width * y + x]
                var r = (pixel and 0x00FF0000 shr 16).toFloat()
                var g = (pixel and 0x0000FF00 shr 8).toFloat()
                var b = (pixel and 0x000000FF).toFloat()

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

        for (x in 0 until bitmap.width) {
            for (y in 0 until bitmap.height) {
                val pixel = arr[bitmap.width * y + x]
                val a = Color.alpha(pixel)
                val r = Color.red(pixel)
                var g = ((pixel and 0x0000FF00 shr 8) - 20 * 128 / 100) as Int
                val b = Color.blue(pixel)

                if (g < 0) g = 0 else if (g > 255) g = 255
                newArr[bitmap.width * y + x] = Color.argb(a, r, g, b)
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

        for (x in 0 until bitmap.width) {
            for (y in 0 until bitmap.height) {
                val pixel = arr[bitmap.width * y + x]
                val r = 255 - (pixel and 0x00FF0000 shr 16)
                val g = 255 - (pixel and 0x0000FF00 shr 8)
                val b = 255 - (pixel and 0x000000FF)

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
                        val pixel = oldBitmap.getPixel(x + xk, y + yk)
                        val r: Float = Color.red(pixel).toFloat()
                        val g: Float = Color.green(pixel).toFloat()
                        val b: Float = Color.blue(pixel).toFloat()
                        newR += (kernel[yk + 1][xk + 1] * r).toInt()
                        newG += (kernel[yk + 1][xk + 1] * g).toInt()
                        newB += (kernel[yk + 1][xk + 1] * b).toInt()
                    }
                }
                if (newR < 0) newR = 0 else if (newR > 255) newR = 255
                if (newG < 0) newG = 0 else if (newG > 255) newG = 255
                if (newB < 0) newB = 0 else if (newB > 255) newB = 255

                val newPixel = Color.rgb(newR, newG, newB)
                newBitmap.setPixel(x, y, newPixel)
            }
        }
        return newBitmap
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
            inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)

            originalBitmapImage.compress(Bitmap.CompressFormat.PNG, 50, stream)

            BitmapFactory.decodeStream(ByteArrayInputStream(stream.toByteArray()), rect, this)
        }
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        // Raw height and width of image
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
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


    override fun scale(bitmap: Bitmap, progress: Int): Bitmap {
        val oldBitmap = bitmap
        val width = oldBitmap.width
        val height = oldBitmap.height
        val coeff: Double
        when (progress) {
            0 -> coeff = 0.125
            1 -> coeff = 0.25
            2 -> coeff = 0.5
            3 -> return oldBitmap
            else -> {
                coeff = (progress - 2).toDouble()
            }
        }
        val newWidth = (ceil(width * coeff)).toInt()
        val newHeight = (ceil(height * coeff)).toInt()
        val newBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888)
        val pixels = IntArray(height * width)
        val newPixels = IntArray(newHeight * newWidth)
        oldBitmap.getPixels(pixels, 0, width, 0, 0, width, height)
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
                a = pixels[index]
                b = pixels[index + 1]
                c = pixels[index + width]
                d = pixels[index + width + 1]
                blue =
                    (a and 0xff) * (1 - xDiff) * (1 - yDiff) + (b and 0xff) * xDiff * (1 - yDiff) + (c and 0xff) * yDiff * (1 - xDiff) + (d and 0xff) * (xDiff * yDiff)
                green =
                    (a shr 8 and 0xff) * (1 - xDiff) * (1 - yDiff) + (b shr 8 and 0xff) * xDiff * (1 - yDiff) + (c shr 8 and 0xff) * yDiff * (1 - xDiff) + (d shr 8 and 0xff) * (xDiff * yDiff)
                red =
                    (a shr 16 and 0xff) * (1 - xDiff) * (1 - yDiff) + (b shr 16 and 0xff) * xDiff * (1 - yDiff) + (c shr 16 and 0xff) * yDiff * (1 - xDiff) + (d shr 16 and 0xff) * (xDiff * yDiff)
                newPixels[offset++] = -0x1000000 or
                        (red.toInt() shl 16 and 0xff0000) or
                        (green.toInt() shl 8 and 0xff00) or
                        blue.toInt()
            }
        }
        newBitmap.setPixels(newPixels, 0, newWidth, 0, 0, newWidth, newHeight)
        return newBitmap
    }

    override fun findCircle(bitmap: Bitmap, context: Context): Bitmap {
        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        val gray = Mat()
        val tmp = Mat()
        Utils.bitmapToMat(bitmap, tmp)
        cvtColor(tmp, gray, Imgproc.COLOR_BGR2GRAY)
        medianBlur(gray, gray, 5)
        val circles = Mat()
        Imgproc.HoughCircles(
            gray, circles, Imgproc.HOUGH_GRADIENT, 1.0,
            gray.rows()/8.toDouble(),
            100.0, 30.0, 1, 1000
        )
        if (circles.cols() !=0) {
            for (x in 0 until circles.cols()) {
                val c = circles[0, x]
                val center = Point(c[0].roundToInt().toDouble(), c[1].roundToInt().toDouble())
                circle(tmp, center, 1, Scalar(0.0, 100.0, 100.0), 3, 8, 0)
                val radius = c[2].roundToInt()
                circle(tmp, center, radius, Scalar(255.0, 0.0, 255.0), 3, 8, 0)
            }
            val newBitmap: Bitmap = bitmap
            Utils.matToBitmap(tmp, newBitmap);
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

    override fun findTriangle(bitmap: Bitmap, context: Context): Bitmap {

        return bitmap
    }

    override fun findRectangle(bitmap: Bitmap, context: Context): Bitmap {

        val gray = Mat()
        val tmp = Mat()
        Utils.bitmapToMat(bitmap, tmp)
        cvtColor(tmp, gray, Imgproc.COLOR_BGR2GRAY)
        medianBlur(gray, gray, 5)
        val thres = Mat()
        threshold(gray, thres, 130.0, 255.0, THRESH_BINARY_INV);
        val contours: List<MatOfPoint> = listOf()
        val hierarchy = Mat()
        findContours(thres, contours, hierarchy, RETR_TREE, CHAIN_APPROX_SIMPLE, Point(0.0, 0.0))
        hierarchy.release()
        for( i in contours.indices)
        {
            val brect: org.opencv.core.Rect = boundingRect(contours[i])
            val k: Double = (brect.width+0.0)/brect.height;
            if(brect.area() > 500 && brect.area() < 2000 && k > 0.9 && k < 1.1)
            {

                val minRect: RotatedRect = minAreaRect(MatOfPoint2f(contours[i]))
                val rectPoints: Array<Point> = Array(4){Point(0.0, 0.0)}
                minRect.points(rectPoints);
                for(j in 0 until 4)
                line(tmp, rectPoints[j], rectPoints[(j+1) % 4], Scalar(0.0,255.0,0.0), 2, 8 );
            }
        }
        val newBitmap: Bitmap = bitmap
        Utils.matToBitmap(tmp, newBitmap);
        return newBitmap
    }
}