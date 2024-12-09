package com.praktikum.honeypot.Util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object BitmapUtils {
    /**
     * Converts a Bitmap to a File.
     */
    fun bitmapToFile(context: Context, bitmap: Bitmap, fileName: String): File {
        val file = File(context.cacheDir, "$fileName.jpg")
        file.createNewFile()
        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()
        return file
    }

    /**
     * Converts a Uri to a File.
     */
    fun uriToFile(context: Context, uri: Uri): File {
        val file = File(context.cacheDir, "selected_image.jpg")
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return file
    }
}
