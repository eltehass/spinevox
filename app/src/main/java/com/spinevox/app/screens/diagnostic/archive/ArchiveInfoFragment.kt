package com.spinevox.app.screens.diagnostic.archive

import android.view.View
import androidx.navigation.fragment.findNavController
import com.spinevox.app.R
import com.spinevox.app.databinding.LayoutArchiveInfoBinding
import com.spinevox.app.screens.base.LazyFragment
import java.text.SimpleDateFormat

class ArchiveInfoFragment : LazyFragment<LayoutArchiveInfoBinding>() {

    override lateinit var binding: LayoutArchiveInfoBinding

    override fun initController(view: View) {
        val data = arguments?.getSerializable("archiveData") as ItemArchiveData
        val format1 = SimpleDateFormat("yyyy-MM-dd")
        val format2 = SimpleDateFormat("dd.MM.yyyy")
        val date = format1.parse("${data.date.substring(0, 10)}")
        val formattedDate = format2.format(date)

        binding.tvDate.text = formattedDate
        binding.ivClose.setOnClickListener { findNavController().popBackStack() }
    }

    override fun getLayoutId(): Int = R.layout.layout_archive_info

}