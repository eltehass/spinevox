package com.spinevox.app.screens.diagnostic.archive

import android.view.View
import androidx.navigation.fragment.findNavController
import com.spinevox.app.R
import com.spinevox.app.databinding.LayoutArchiveInfoBinding
import com.spinevox.app.screens.base.LazyFragment

class ArchiveInfoFragment : LazyFragment<LayoutArchiveInfoBinding>() {

    override lateinit var binding: LayoutArchiveInfoBinding

    override fun initController(view: View) {
        binding.ivClose.setOnClickListener { findNavController().popBackStack() }
    }

    override fun getLayoutId(): Int = R.layout.layout_archive_info

}