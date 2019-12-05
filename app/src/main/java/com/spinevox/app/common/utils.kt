package com.spinevox.app.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Base64
import com.muddzdev.styleabletoast.StyleableToast
import java.io.ByteArrayOutputStream

fun showToast(context: Context, message: String, textColor: Int = Color.BLACK, backgroundColor: Int = Color.WHITE) {
    StyleableToast
        .Builder(context)
        .text(message)
        .textColor(textColor)
        .backgroundColor(backgroundColor)
        .show()
}

fun convertBitmapToBase64(bitmap: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 10, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}