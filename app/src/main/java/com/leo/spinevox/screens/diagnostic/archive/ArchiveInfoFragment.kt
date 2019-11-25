package com.leo.spinevox.screens.diagnostic.archive

import android.view.View
import androidx.navigation.fragment.findNavController
import com.leo.spinevox.R
import com.leo.spinevox.databinding.LayoutArchiveInfoBinding
import com.leo.spinevox.screens.base.LazyFragment

class ArchiveInfoFragment : LazyFragment<LayoutArchiveInfoBinding>() {

    override lateinit var binding: LayoutArchiveInfoBinding

    override fun initController(view: View) {
        binding.ivClose.setOnClickListener { findNavController().popBackStack() }
    }

    override fun getLayoutId(): Int = R.layout.layout_archive_info

}