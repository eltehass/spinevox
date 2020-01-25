package com.spinevox.app.screens.diagnostic

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.spinevox.app.R
import com.spinevox.app.common.TwoBitmapHolders
import com.spinevox.app.common.convertBitmapToBase64
import com.spinevox.app.common.showToast
import com.spinevox.app.network.RequestInpectionList
import com.spinevox.app.network.SpineVoxService
import com.spinevox.app.network.serverErrorMessage
import com.spinevox.app.screens.diagnostic.camera.CustomCameraUIActivity
import com.spinevox.app.screens.diagnostic.camera.CustomCameraUISecondActivity
import kotlinx.android.synthetic.main.activity_photo_preview.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class PhotoPreviewActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext by lazy { Dispatchers.Main + job }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_preview)
        job = Job()

        val screenNumber = intent.getIntExtra("ScreenNumber", 1)
        val bitmap = when (screenNumber) {
            1 -> TwoBitmapHolders.bitmap1
            2 -> TwoBitmapHolders.bitmap2
            else -> TwoBitmapHolders.bitmap1
        }

        iv_preview.setImageBitmap(bitmap)

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
                    startActivity(Intent(this@PhotoPreviewActivity, CustomCameraUISecondActivity::class.java))
                    finish()
                }

                2 -> {
                    val sharedPreferences = getSharedPreferences("", Context.MODE_PRIVATE)
                    val token = sharedPreferences.getString("serverToken", "")

                    //TODO
                    val image1 = convertBitmapToBase64(TwoBitmapHolders.bitmap1!!)
                    val image2 = convertBitmapToBase64(TwoBitmapHolders.bitmap2!!)

                    launch {
                        showProgress()
                        try {
                            val height = SpineVoxService.getService(this@PhotoPreviewActivity, true).me("JWT $token").await().data.profile.height
                            SpineVoxService.getService(this@PhotoPreviewActivity, true)
                                .sendInspectionList("JWT $token",
                                        RequestInpectionList("image", image1, image2)).await()
                            startActivity(Intent(this@PhotoPreviewActivity, WaitingDoctorResultsActivity::class.java))
                        }  catch (e: HttpException) {
                            showToast(this@PhotoPreviewActivity, e.serverErrorMessage())
                        } catch (e: Exception) {
                            val a = ""
                        }
                        finally {
                            hideProgress()
                            finish()
                        }
                    }
                }

                else -> {
                    onBackPressed()
                }
            }

        }

    }

    private fun showProgress() {
        pb_loader.visibility = View.VISIBLE
        btn_save.isClickable = false
    }

    private fun hideProgress() {
        pb_loader.visibility = View.GONE
        btn_save.isClickable = true
    }

}