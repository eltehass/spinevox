package com.leo.spinevox.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.leo.spinevox.R
import androidx.navigation.ui.NavigationUI
import androidx.navigation.fragment.NavHostFragment

class HostContentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host_content)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.itemIconTintList = null

        setUpNavigation(bottomNavigationView)
    }

    private fun setUpNavigation(bottomNavigationView: BottomNavigationView) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment!!.navController)
    }

}