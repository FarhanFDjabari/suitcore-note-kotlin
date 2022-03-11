package com.suitcore.feature.note

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.suitcore.base.ui.adapter.BaseRecyclerAdapter
import com.suitcore.databinding.ItemTagsBinding

class TagAdapter(var context: Context) : BaseRecyclerAdapter<String, TagItemView>(){

    private lateinit var itemTagsBinding: ItemTagsBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagItemView {
        itemTagsBinding = ItemTagsBinding.inflate(LayoutInflater.from(context), parent, false)
        return TagItemView(itemTagsBinding)
    }

}