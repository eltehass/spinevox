package com.leo.spinevox.screens.diagnostic

import android.content.Intent
import android.view.View
import com.leo.spinevox.R
import com.leo.spinevox.databinding.LayoutDiagnosticMainMenuBinding
import com.leo.spinevox.screens.base.LazyFragment
import com.leo.spinevox.screens.diagnostic.camera.CustomCameraUIActivity

class DiagnosticMainMenuFragment : LazyFragment<LayoutDiagnosticMainMenuBinding>() {

    override lateinit var binding: LayoutDiagnosticMainMenuBinding

    override fun initController(view: View) {
        binding.btnScoliometr.setOnClickListener {

        }

        binding.btnAi.setOnClickListener {
            activity?.startActivity(Intent(context, CustomCameraUIActivity::class.java))
        }

        binding.btnArchive.setOnClickListener {

        }
    }

    override fun getLayoutId(): Int = R.layout.layout_diagnostic_main_menu
}