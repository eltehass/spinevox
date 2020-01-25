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



  val scoliometrData = mapOf<String,String>(
    "diagnosis_scoliometry_twisted_pelvis" to "косо-скрученого тазу\n\n",
    "diagnosis_scoliometry_s_scoliosis_right" to "s-подібного сколіозу\n\n",
    "diagnosis_scoliometry_s_scoliosis_left" to "s-подібного сколіозу\n\n",
    "diagnosis_scoliometry_chest_left" to "лівобічного сколіозу грудного відділу хребта\n\n",
    "diagnosis_scoliometry_chest_right" to "правобічного сколіозу грудного відділу хребта\n\n",
    "diagnosis_scoliometry_chest_lumbar_spine_right" to "правобічного сколіозу грудо- поперекового відділу хребта\n\n",
    "diagnosis_scoliometry_chest_lumbar_spine_left" to "лівобічного сколіозу грудо- поперекового відділу хребта\n\n",
    "diagnosis_scoliometry_lumbar_spine_right" to "правобічного сколіозу поперекового відділу хребта\n\n",
    "diagnosis_scoliometry_lumbar_spine_left" to "лівобічного сколіозу поперекового відділу хребта\n\n",
    "diagnosis_profile_tech_neck" to "техношиї\n\n",
    "diagnosis_profile_neck_hyperlordosis" to "гіперлордозу шийного відділу хребта\n\n",
    "diagnosis_profile_slouch" to "сутулості\n\n",
    "diagnosis_profile_lumbar_hyperlordosis" to "гіперлордозу поперекового відділу хребта\n\n",
    "diagnosis_back_kyphoscoliosis" to "кіфосколіозу\n\n",
    "diagnosis_back_c_lumbar_scoliosis_left" to "лівобічного сколіозу поперекового відділу хребта\n\n",
    "diagnosis_back_c_lumbar_scoliosis_right" to "правобічного сколіозу поперекового відділу хребта\n\n",
    "diagnosis_back_s_scoliosis_right" to "s- подібного сколіозу\n\n",
    "diagnosis_back_s_scoliosis_left" to "s- подібного сколіозу\n\n",
    "diagnosis_back_c_scoliosis_right" to "правобічного сколіозу грудо- поперекового відділу хребта\n\n",
    "diagnosis_back_c_scoliosis_left" to "лівобічного сколіозу грудо- поперекового відділу хребта\n\n"
  )

}