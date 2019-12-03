package com.spinevox.app.screens.diagnostic

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.spinevox.app.R
import com.spinevox.app.databinding.LayoutDiagnosticMainMenuBinding
import com.spinevox.app.screens.base.LazyFragment
import com.spinevox.app.screens.diagnostic.camera.CustomCameraUIActivity

class DiagnosticMainMenuFragment : LazyFragment<LayoutDiagnosticMainMenuBinding>() {

    override lateinit var binding: LayoutDiagnosticMainMenuBinding

    override fun initController(view: View) {
        binding.btnScoliometr.setOnClickListener {

        }

        binding.btnAi.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                activity?.startActivity(Intent(context, CustomCameraUIActivity::class.java))
            } else {
                requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), 3)
            }
        }

        binding.btnArchive.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_tab1_to_archiveFragment)
        }
    }

    override fun getLayoutId(): Int = R.layout.layout_diagnostic_main_menu
}