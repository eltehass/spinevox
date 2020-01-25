package com.spinevox.app

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.spinevox.app.common.showToast
import com.spinevox.app.network.RequestInpectionList
import com.spinevox.app.network.SpineVoxService
import com.spinevox.app.network.serverErrorMessage
import kotlinx.android.synthetic.main.activity_scoliometr.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext
import kotlin.math.abs
import kotlin.math.atan2


class ScoliometrActivity : AppCompatActivity(), SensorEventListener, CoroutineScope {

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext by lazy { Dispatchers.Main + job }

    /** Called when the activity is first created.  */
    var mDrawable = ShapeDrawable()
    private var sensorManager: SensorManager? = null
    private var currentAnimationTime = 0

    var angleValues = mutableListOf<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scoliometr)
        job = Job()


        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        btn_return.setOnClickListener {
            onBackPressed()
        }

        btn_start.setOnClickListener {
            btn_start.visibility = View.GONE

            val firstValue = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                170f,
                resources.displayMetrics
            )

            val lastValue = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                65f,
                resources.displayMetrics
            )

            val va = ValueAnimator.ofFloat(firstValue, lastValue)
            val mDuration = 10000 //in millis


            va.duration = mDuration.toLong()
            va.interpolator = LinearInterpolator()
            va.addUpdateListener { animation ->
                val time = animation.currentPlayTime / 1000
                currentAnimationTime = time.toInt()

                run {
                    Log.i("Anim", "${animation.animatedValue as Float}")
                    iv_hands.translationY = animation.animatedValue as Float
                }
            }

            va.repeatCount = 0
            va.start()
        }

    }


    // This method will update the UI on new sensor events
    override fun onSensorChanged(sensorEvent: SensorEvent) {
        run {
            if (sensorEvent.sensor.type == Sensor.TYPE_ACCELEROMETER) { // the values you were calculating originally here were over 10000!
                val newZ = Math.pow(sensorEvent.values[0].toDouble(), 2.0).toInt()
                val newX = Math.pow(sensorEvent.values[1].toDouble(), 2.0).toInt()
                val newY = Math.pow(sensorEvent.values[2].toDouble(), 2.0).toInt()

                if (abs(x - newX) > 0.5 || abs(y - newY) > 0.5 || abs(z - newZ) > 0.5) {
                    x = newX
                    y = newY
                    z = newZ

                    val angle = atan2(sensorEvent.values[1].toDouble(), sensorEvent.values[0].toDouble()) + (Math.PI / 2)
                    val angleDegrees = angle * 180.0 / Math.PI
                    var a = (angleDegrees - 90).toInt()

                    if (currentAnimationTime == 0 && angleValues.size == 0) {
                        angleValues.add(a)
                    }
                    if (currentAnimationTime == 5 && angleValues.size == 1) {
                        angleValues.add(a)
                    }
                    if (currentAnimationTime == 9 && angleValues.size == 2) {
                        angleValues.add(a)
                        Toast.makeText(this, "${angleValues[0]}, ${angleValues[1]}, ${angleValues[2]}", Toast.LENGTH_LONG).show()

                        val sharedPreferences = getSharedPreferences("", Context.MODE_PRIVATE)
                        val token = sharedPreferences.getString("serverToken", "")

                        launch {
                            showProgress()
                            try {
                                //val height = SpineVoxService.getService(this@PhotoPreviewActivity, true).me("JWT $token").await().data.profile.height
                                SpineVoxService.getService(this@ScoliometrActivity, true)
                                    .sendInspectionList("JWT $token",
                                        RequestInpectionList("skoliometry", skoliometry_pelvis = angleValues[0], skoliometry_lumbar = angleValues[1], skoliometry_chest = angleValues[2])
                                    ).await()
                            } catch (e: HttpException) {
                                showToast(this@ScoliometrActivity, e.serverErrorMessage())
                            } catch (e: Throwable) {
                                //showToast(this@ScoliometrActivity, "Alo: ${e.message}")
                                val a = 0
                            }
                            finally {
                                hideProgress()
                                //finish()
                            }
                        }

                    }

                    if (a < -50) {
                        a = -50
                    }
                    if (a > 50) {
                        a = 50
                    }

                    var xDP = ((angleDegrees - 90) * (450.0 / 100.0)).toFloat()
                    if (xDP > 222) { xDP = 222f }
                    if (xDP < -222) { xDP = -222f }

                    val yDP = -1 * Math.sqrt(Math.pow(415.45, 2.0) - Math.pow((Math.abs(xDP) + 55).toDouble(), 2.0)) + 427

                    val xPX = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        xDP,
                        resources.displayMetrics
                    )

                    val yPX = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        -1 * abs(yDP.toFloat()),
                        resources.displayMetrics
                    )

                    iv_circle.animate()
                        .translationX(xPX)
                        .translationY(yPX)
                        .setDuration(20)
                        .setInterpolator(DecelerateInterpolator())
                        .start()
                }

            }
        }
    }

    override fun onAccuracyChanged(arg0: Sensor, arg1: Int) {}

    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this, sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onStop() { // Unregister the listener
        sensorManager!!.unregisterListener(this)
        super.onStop()
    }

    inner class CustomDrawableView(context: Context?) : View(context) {
        val mwidth = 50
        val mheight = 50

        override fun onDraw(canvas: Canvas) {
            val oval = RectF(ScoliometrActivity.x.toFloat(), ScoliometrActivity.y.toFloat(), (ScoliometrActivity.x + mwidth).toFloat(), (ScoliometrActivity.y + mheight).toFloat()) // set bounds of rectangle
            val p = Paint() // set some paint options
            p.color = Color.BLUE
            canvas.drawOval(oval, p)
            invalidate()
        }


        init {
            mDrawable = ShapeDrawable(OvalShape())
            mDrawable.paint.color = -0x8b53dd
            mDrawable.setBounds(ScoliometrActivity.x, ScoliometrActivity.y, ScoliometrActivity.x + mwidth, ScoliometrActivity.y + mheight)
        }
    }

    private fun showProgress() {
        pb_loader.visibility = View.VISIBLE
        btn_return.isClickable = false
    }

    private fun hideProgress() {
        pb_loader.visibility = View.GONE
        btn_return.isClickable = true
        btn_start.visibility = View.VISIBLE
    }

    companion object {
        var x = 0
        var y = 0
        var z = 0
    }


}
