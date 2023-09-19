package com.fappslab.seedcake.libraries.extension.image

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Environment
import android.provider.MediaStore.Images
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

private const val FILE_PROVIDER_SUFFIX = ".fileprovider"
private const val MIME_TYPE = "image/png"
private const val FILE_EXTENSION = ".png"
private const val COMPRESSION = 100

private val Context.fileAuthority
    get() = this.packageName + FILE_PROVIDER_SUFFIX

fun View.createBitmap(
    customWidth: Int = 0, customHeight: Int = 0, @ColorRes bgColorRes: Int
): Bitmap {
    val actualWidth = if (customWidth != 0) customWidth else this.width
    val actualHeight = if (customHeight != 0) customHeight else this.height

    val bitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    canvas.drawColor(context.getColor(bgColorRes))
    draw(canvas)

    return bitmap
}

fun View.saveToGallery(
    fileName: String,
    customWidth: Int = 0,
    customHeight: Int = 0,
    @ColorRes bgColorRes: Int,
    resultBlock: (Boolean) -> Unit
) {
    val bitmap = createBitmap(customHeight, customWidth, bgColorRes)
    val contentResolver = context.contentResolver
    val format = Bitmap.CompressFormat.PNG

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val imageDetails = ContentValues().apply {
            put(Images.Media.MIME_TYPE, MIME_TYPE)
            put(Images.Media.DISPLAY_NAME, "$fileName$FILE_EXTENSION")
            put(Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val uri = contentResolver.insert(Images.Media.EXTERNAL_CONTENT_URI, imageDetails)
        val outputStream = uri?.let(contentResolver::openOutputStream)
        val result = bitmap.compress(format, COMPRESSION, outputStream)
        outputStream?.flush()
        outputStream?.close()

        resultBlock(result)

    } else {
        val fullFilePath = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            .toString() + "/$fileName$FILE_EXTENSION"

        val outputStream = FileOutputStream(fullFilePath)
        val result = bitmap.compress(format, COMPRESSION, outputStream)
        outputStream.close()

        val file = File(fullFilePath)
        FileProvider.getUriForFile(context, context.fileAuthority, file)
        resultBlock(result && file.exists())
    }
}
