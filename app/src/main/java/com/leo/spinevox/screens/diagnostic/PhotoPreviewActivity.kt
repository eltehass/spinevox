package com.leo.spinevox.screens.diagnostic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.leo.spinevox.R
import com.leo.spinevox.screens.diagnostic.camera.CustomCameraUIActivity
import kotlinx.android.synthetic.main.activity_custom_camera_ui.iv_close
import kotlinx.android.synthetic.main.activity_photo_preview.*
import android.graphics.BitmapFactory
import com.leo.spinevox.screens.diagnostic.camera.CustomCameraUISecondActivity

class PhotoPreviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_preview)

//        val bitmap = intent.getParcelableExtra("BitmapImage") as Bitmap
        val bitmapArray: ByteArray = intent.getByteArrayExtra("BitmapData")
        val screenNumber = intent.getIntExtra("ScreenNumber", 1)

        val options = BitmapFactory.Options()
        options.inMutable = true
        val bmp = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.size, options)

        iv_preview.setImageBitmap(bmp)

        iv_close.setOnClickListener { onBackPressed() }
        btn_remake.setOnClickListener {
            when (screenNumber) {
                1 -> startActivity(Intent(this@PhotoPreviewActivity, CustomCameraUIActivity::class.java))
                2 -> startActivity(Intent(this@PhotoPreviewActivity, CustomCameraUISecondActivity::class.java))
            }

            finish()
        }

        btn_save.setOnClickListener {

            when (screenNumber) {
                1 -> {
                    //TODO
                    startActivity(Intent(this@PhotoPreviewActivity, CustomCameraUISecondActivity::class.java))
                    finish()
                }

                2 -> {
                    //TODO
                    startActivity(Intent(this@PhotoPreviewActivity, WaitingDoctorResultsActivity::class.java))
                    finish()
                }

                else -> {
                    onBackPressed()
                }
            }

        }

    }
}