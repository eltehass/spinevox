package com.spinevox.app.screens.diagnostic.archive

import com.spinevox.app.R
import com.spinevox.app.databinding.ItemArchiveBinding
import com.spinevox.app.screens.base.recycler.LazyAdapter
import java.io.Serializable
import java.text.SimpleDateFormat


class ArchiveAdapter(onItemClickListener: OnItemClickListener<ItemArchiveData>) : LazyAdapter<ItemArchiveData, ItemArchiveBinding>(onItemClickListener) {
    override fun bindData(data: ItemArchiveData, binding: ItemArchiveBinding) {
        val format1 = SimpleDateFormat("yyyy-MM-dd")
        val format2 = SimpleDateFormat("dd.MM.yyyy")
        val date = format1.parse("${data.date.substring(0, 10)}")
        val formattedDate = format2.format(date)

        binding.tvTitle.text = data.title
        binding.tvDate.text = formattedDate
        binding.cvRoot.setOnClickListener { itemClickListener?.onLazyItemClick(data) }
    }

    override fun getLayoutId(): Int = R.layout.item_archive

}

data class ItemArchiveData(val title: String, val date: String) : Serializable