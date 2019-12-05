package com.spinevox.app.screens.diagnostic.archive

import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.spinevox.app.R
import com.spinevox.app.databinding.LayoutArchiveBinding
import com.spinevox.app.network.InspectionDataResponse
import com.spinevox.app.network.SpineVoxService
import com.spinevox.app.screens.base.LazyFragment
import com.spinevox.app.screens.base.recycler.LazyAdapter
import com.spinevox.app.screens.base.recycler.initWithLinLay
import kotlinx.coroutines.launch

class ArchiveFragment : LazyFragment<LayoutArchiveBinding>(), LazyAdapter.OnItemClickListener<ItemArchiveData> {

    override lateinit var binding: LayoutArchiveBinding

    override fun initController(view: View) {
        binding.ivClose.setOnClickListener { findNavController().popBackStack() }

        val listData = mutableListOf<InspectionDataResponse.InspectionDataItem>()

        val token = sharedPreferences.getString("serverToken", "")
        launch {
            try {
                val result = SpineVoxService.getService(context!!).getInspectionList("JWT $token").await()
                listData.addAll(result.data)
                binding.rvContent.initWithLinLay(LinearLayoutManager.VERTICAL, ArchiveAdapter(this@ArchiveFragment), listData.map { ItemArchiveData("Сколіометр + Штучній інтелект", it.created_at) })
            } catch (e: Throwable) {
                val a = 5
            }
        }

//        binding.rvContent.initWithLinLay(LinearLayoutManager.VERTICAL, ArchiveAdapter(this), listOf(
//                ItemArchiveData("Сколіометр + Штучній інтелект", "20.09.2019"),
//                ItemArchiveData("Сколіометр + Штучній інтелект", "20.09.2019"),
//                ItemArchiveData("Сколіометр + Штучній інтелект", "20.09.2019"),
//                ItemArchiveData("Сколіометр + Штучній інтелект", "20.09.2019"),
//                ItemArchiveData("Сколіометр + Штучній інтелект", "20.09.2019"),
//                ItemArchiveData("Сколіометр + Штучній інтелект", "20.09.2019"),
//                ItemArchiveData("Сколіометр + Штучній інтелект", "20.09.2019"),
//                ItemArchiveData("Сколіометр + Штучній інтелект", "20.09.2019"),
//                ItemArchiveData("Сколіометр + Штучній інтелект", "20.09.2019"),
//                ItemArchiveData("Сколіометр + Штучній інтелект", "20.09.2019")
//        ))
    }

    override fun onLazyItemClick(data: ItemArchiveData) {
        //TODO:::
        val bundle = bundleOf("archiveData" to data)
        findNavController().navigate(R.id.action_archiveFragment_to_archiveInfoFragment, bundle)

//        arguments?.getBundle("amount") as ItemArchiveData
    }

    override fun getLayoutId(): Int = R.layout.layout_archive

}