package com.suitcore.feature.note

import com.suitcore.base.ui.adapter.viewholder.BaseItemViewHolder
import com.suitcore.databinding.ItemTagsBinding

class TagItemView(var binding: ItemTagsBinding) : BaseItemViewHolder<String>(binding) {

    private var tag: String? = null

    override fun bind(data: String?) {
        data?.let {
            this.tag = data
            binding.tvTag.text = tag
        }
    }
}