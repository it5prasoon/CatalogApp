package com.swipe.catalog.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

object Utils {

    fun uriToFile(context: Context, uri: Uri): File? {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        inputStream ?: return null

        val outputFile = File(context.cacheDir, "temp_image.jpg")
        val outputStream = FileOutputStream(outputFile)

        try {
            val buffer = ByteArray(4 * 1024) // 4k buffer
            var read: Int
            while (inputStream.read(buffer).also { read = it } != -1) {
                outputStream.write(buffer, 0, read)
            }
            outputStream.flush()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } finally {
            inputStream.close()
            outputStream.close()
        }

        return outputFile
    }


}