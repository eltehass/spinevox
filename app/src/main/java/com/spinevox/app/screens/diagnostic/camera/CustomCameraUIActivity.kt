package com.spinevox.app.screens.diagnostic.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.spinevox.app.R
import com.spinevox.app.common.TwoBitmapHolders
import com.spinevox.app.screens.diagnostic.PhotoPreviewActivity
import com.spinevox.app.screens.diagnostic.camera.core.Camera2
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_custom_camera_ui.*

class CustomCameraUIActivity : AppCompatActivity() {

    private lateinit var camera2: Camera2
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_camera_ui)

        iv_close.setOnClickListener { onBackPressed() }
        camera2 = Camera2(this, camera_view)
        init()
        initCamera2Api()
    }

    private fun init() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        )
            initCamera2Api()
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), 3)
            else initCamera2Api()
        }
    }

    private fun initCamera2Api() {
        iv_make_capture.setOnClickListener {
            camera2.takePhoto {
                val intent = Intent(this@CustomCameraUIActivity, PhotoPreviewActivity::class.java)
                TwoBitmapHolders.bitmap1 = it
                intent.putExtra("ScreenNumber", 1)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onPause() {
        //  cameraPreview.pauseCamera()
        camera2.close()
        super.onPause()
    }

    override fun onResume() {
        // cameraPreview.resumeCamera()
        camera2.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        if (disposable != null)
            disposable!!.dispose()
        super.onDestroy()
    }


}
