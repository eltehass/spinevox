package com.spinevox.app.network

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Utils {

  @SuppressLint("SimpleDateFormat")
  fun parseDate(dateText: String, format: String): Date {
    val formatter = SimpleDateFormat(format)
    var date = Calendar.getInstance().time

    try {
      date = formatter.parse(dateText)
    } catch (e: ParseException) {
      e.printStackTrace()
    }

    return date
  }

}