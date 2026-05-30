package com.example.client.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream

object ImageUtils {
    private const val MAX_DIMENSION = 1024
    private const val JPEG_QUALITY = 70

    fun uriToBase64(context: Context, uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            val scaled = scaleBitmap(bitmap)
            val stream = ByteArrayOutputStream()
            scaled.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, stream)
            val bytes = stream.toByteArray()
            val base64 = android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)
            "data:image/jpeg;base64,$base64"
        } catch (e: Exception) {
            null
        }
    }

    private fun scaleBitmap(bitmap: Bitmap): Bitmap {
        val (width, height) = if (bitmap.width > bitmap.height) {
            if (bitmap.width > MAX_DIMENSION) MAX_DIMENSION to (bitmap.height * MAX_DIMENSION / bitmap.width)
            else bitmap.width to bitmap.height
        } else {
            if (bitmap.height > MAX_DIMENSION) (bitmap.width * MAX_DIMENSION / bitmap.height) to MAX_DIMENSION
            else bitmap.width to bitmap.height
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }
}
