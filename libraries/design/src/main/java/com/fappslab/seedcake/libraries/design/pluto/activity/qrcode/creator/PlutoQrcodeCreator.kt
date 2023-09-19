package com.fappslab.seedcake.libraries.design.pluto.activity.qrcode.creator

import android.graphics.Bitmap
import android.graphics.Color
import com.fappslab.seedcake.libraries.extension.dp
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

private const val QR_CODE_SIZE = 192
private const val COLOR_STRING = "#F5F5F5"

class PlutoQrcodeCreator private constructor() {

    private fun generateBitMatrix(data: String): BitMatrix {
        val hints = mapOf<EncodeHintType, Any>(
            EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.L,
            EncodeHintType.MARGIN to 0
        )

        return QRCodeWriter().encode(
            data,
            BarcodeFormat.QR_CODE,
            QR_CODE_SIZE.dp,
            QR_CODE_SIZE.dp,
            hints
        )
    }

    private fun createBitmapFromBitMatrix(bitMatrix: BitMatrix): Bitmap {
        val width = bitMatrix.width
        val height = bitMatrix.height
        val pixels = IntArray(width * height)
        val codeColor = Color.BLACK
        val bgColor = Color.parseColor(COLOR_STRING)

        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] =
                    if (bitMatrix[x, y]) {
                        codeColor
                    } else bgColor
            }
        }

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)

        return bitmap
    }

    companion object {
        fun create(data: String): Bitmap? {
            val qrcodeCreator = PlutoQrcodeCreator()
            return runCatching {
                val bitMatrix = qrcodeCreator.generateBitMatrix(data)
                qrcodeCreator.createBitmapFromBitMatrix(bitMatrix)
            }.getOrElse { null }
        }
    }
}
