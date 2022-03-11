package com.suitcore.feature.note.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.suitcore.base.ui.adapter.BaseRecyclerAdapter
import com.suitcore.databinding.ItemAddTagsBinding

class TagDetailAdapter(var context: Context) : BaseRecyclerAdapter<String, TagDetailItemView>() {

    private lateinit var itemAddTagsBinding: ItemAddTagsBinding
    private var mOnActionListener: TagDetailItemView.OnActionListener? = null

    fun setOnActionListener(onActionListener: TagDetailItemView.OnActionListener) {
        mOnActionListener = onActionListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagDetailItemView {
        itemAddTagsBinding = ItemAddTagsBinding.inflate(LayoutInflater.from(context), parent, false)
        val view = TagDetailItemView(itemAddTagsBinding)
        mOnActionListener?.let { view.setOnActionListener(it) }
        return view
    }
}