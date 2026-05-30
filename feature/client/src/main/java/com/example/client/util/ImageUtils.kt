package com.example.client.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import java.io.ByteArrayOutputStream

object ImageUtils {
    private const val MAX_DIMENSION = 800
    private const val JPEG_QUALITY = 60
    private const val TAG = "ImageUtils"

    fun uriToBase64(context: Context, uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            if (inputStream == null) {
                Log.w(TAG, "openInputStream вернул null для URI: $uri")
                return null
            }

            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream.close()

            val (srcWidth, srcHeight) = options.outWidth to options.outHeight
            if (srcWidth <= 0 || srcHeight <= 0) {
                Log.w(TAG, "Не удалось определить размеры для URI: $uri")
                return null
            }

            val sampleSize = maxOf(
                1,
                srcWidth / MAX_DIMENSION,
                srcHeight / MAX_DIMENSION
            )

            val inputStream2 = context.contentResolver.openInputStream(uri) ?: return null
            val decodeOptions = BitmapFactory.Options().apply {
                inSampleSize = sampleSize
            }
            val bitmap = BitmapFactory.decodeStream(inputStream2, null, decodeOptions)
            inputStream2.close()

            if (bitmap == null) {
                Log.w(TAG, "BitmapFactory.decodeStream вернул null для URI: $uri")
                return null
            }

            val scaled = scaleIfNeeded(bitmap)
            if (scaled !== bitmap) bitmap.recycle()

            val stream = ByteArrayOutputStream()
            scaled.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, stream)
            val bytes = stream.toByteArray()
            val base64 = android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)
            "data:image/jpeg;base64,$base64"
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка конвертации URI в base64: ${e.message}", e)
            null
        }
    }

    private fun scaleIfNeeded(bitmap: Bitmap): Bitmap {
        val maxDim = maxOf(bitmap.width, bitmap.height)
        if (maxDim <= MAX_DIMENSION) return bitmap

        val scale = MAX_DIMENSION.toFloat() / maxDim
        val newWidth = (bitmap.width * scale).toInt()
        val newHeight = (bitmap.height * scale).toInt()
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }
}
