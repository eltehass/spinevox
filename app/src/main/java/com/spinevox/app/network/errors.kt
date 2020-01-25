package com.spinevox.app.network

import org.json.JSONObject
import retrofit2.HttpException

fun HttpException.serverErrorMessage(): String {
    return serverErrors[serverErrorCode()] ?: "Помилка"
}

private fun HttpException.serverErrorCode(): String {
    try {
        val jObjError = JSONObject(response()?.errorBody()?.string())
        if (jObjError.getJSONObject("errors").keys().hasNext()) {
            return jObjError.getJSONObject("errors").getJSONArray(jObjError.getJSONObject("errors").keys().next())[0].toString()
        }
    } catch (e: Exception) {
        return ""
    }

    return ""
}

private val serverErrors = mapOf<String, String>(
        "" to "Помилка",
        "1121" to "Користувача з таким email вже зареєстровано. Спробуйте інший спосіб авторизації",
        "1110" to "Помилка авторизації. Не правильний логін або пароль",
        "1111" to "Аккаунт не активний",
        "1112" to "Час сесії вичерпано",
        "1113" to "Користувача не існує",
        "1117" to "Не правильний пароль",
        "1119" to "Неможливо створити аккаунт",
        "1122" to "Помилка розпізнавання фотографії. Спробуйте пізніше",
        "1124" to "Кількість СМС перевищено",
        "1211" to "Помилка розпізнавання людини на фото"
)