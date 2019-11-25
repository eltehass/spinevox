package com.leo.spinevox.screens.diagnostic.archive

import com.leo.spinevox.R
import com.leo.spinevox.databinding.ItemArchiveBinding
import com.leo.spinevox.screens.base.recycler.LazyAdapter
import java.io.Serializable

class ArchiveAdapter(onItemClickListener: OnItemClickListener<ItemArchiveData>) : LazyAdapter<ItemArchiveData, ItemArchiveBinding>(onItemClickListener) {
    override fun bindData(data: ItemArchiveData, binding: ItemArchiveBinding) {
        binding.tvTitle.text = data.title
        binding.tvDate.text = data.date
        binding.cvRoot.setOnClickListener { itemClickListener?.onLazyItemClick(data) }
    }

    override fun getLayoutId(): Int = R.layout.item_archive

}

data class ItemArchiveData(val title: String, val date: String) : Serializable