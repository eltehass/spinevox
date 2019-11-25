package com.leo.spinevox.screens.diagnostic.archive

import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.leo.spinevox.R
import com.leo.spinevox.databinding.LayoutArchiveBinding
import com.leo.spinevox.screens.base.LazyFragment
import com.leo.spinevox.screens.base.recycler.LazyAdapter
import com.leo.spinevox.screens.base.recycler.initWithLinLay

class ArchiveFragment : LazyFragment<LayoutArchiveBinding>(), LazyAdapter.OnItemClickListener<ItemArchiveData> {

    override lateinit var binding: LayoutArchiveBinding

    override fun initController(view: View) {
        binding.ivClose.setOnClickListener { findNavController().popBackStack() }

        binding.rvContent.initWithLinLay(LinearLayoutManager.VERTICAL, ArchiveAdapter(this), listOf(
                ItemArchiveData("Сколіометр + Штучній інтелект", "20.09.2019"),
                ItemArchiveData("Сколіометр + Штучній інтелект", "20.09.2019"),
                ItemArchiveData("Сколіометр + Штучній інтелект", "20.09.2019"),
                ItemArchiveData("Сколіометр + Штучній інтелект", "20.09.2019"),
                ItemArchiveData("Сколіометр + Штучній інтелект", "20.09.2019"),
                ItemArchiveData("Сколіометр + Штучній інтелект", "20.09.2019"),
                ItemArchiveData("Сколіометр + Штучній інтелект", "20.09.2019"),
                ItemArchiveData("Сколіометр + Штучній інтелект", "20.09.2019"),
                ItemArchiveData("Сколіометр + Штучній інтелект", "20.09.2019"),
                ItemArchiveData("Сколіометр + Штучній інтелект", "20.09.2019")
        ))
    }

    override fun onLazyItemClick(data: ItemArchiveData) {
        //TODO:::
        val bundle = bundleOf("archiveData" to data)
        findNavController().navigate(R.id.action_archiveFragment_to_archiveInfoFragment, bundle)

//        arguments?.getBundle("amount") as ItemArchiveData
    }

    override fun getLayoutId(): Int = R.layout.layout_archive

}