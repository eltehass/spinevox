package com.spinevox.app.common

import android.app.Activity
import android.content.Intent
import android.net.Uri

fun Activity.openWebPage(url: String?) {
    val webpage: Uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, webpage)
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    }
}