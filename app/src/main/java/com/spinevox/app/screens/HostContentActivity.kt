package com.spinevox.app.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.spinevox.app.R
import com.spinevox.app.network.SpineVoxService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class HostContentActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext by lazy { Dispatchers.Main + job }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host_content)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.itemIconTintList = null

        setUpNavigation(bottomNavigationView)

        job = Job()

        val sharedPreferences = getSharedPreferences("", Context.MODE_PRIVATE)
        val serverToken = sharedPreferences.getString("serverToken", "") ?: ""
        val userHeight = sharedPreferences.getInt("user_height", 0)

        if (userHeight == 0) {
            launch {
                try {
                    val height = SpineVoxService.getService(this@HostContentActivity, true).me("JWT $serverToken").await().data.profile.height
                    if (height == null || height < 50) {
                        startActivity(Intent(this@HostContentActivity, SetHeightActivity::class.java))
                    } else {
                        sharedPreferences.edit { putInt("user_height", height.toInt()) }
                    }
                } catch (e: Exception) {
                }
            }
        }

    }

    private fun setUpNavigation(bottomNavigationView: BottomNavigationView) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment!!.navController)
    }

}