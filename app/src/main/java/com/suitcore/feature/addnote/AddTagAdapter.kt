package com.suitcore.feature.addnote

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.suitcore.base.ui.adapter.BaseRecyclerAdapter
import com.suitcore.databinding.ItemAddTagsBinding

class AddTagAdapter(var context: Context) : BaseRecyclerAdapter<String, AddTagItemView>() {

    private lateinit var itemAddTagsBinding: ItemAddTagsBinding
    private var mOnActionListener: AddTagItemView.OnActionListener? = null

    fun setOnActionListener(onActionListener: AddTagItemView.OnActionListener) {
        mOnActionListener = onActionListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddTagItemView {
        itemAddTagsBinding = ItemAddTagsBinding.inflate(LayoutInflater.from(context), parent, false)
        val view = AddTagItemView(itemAddTagsBinding)
        mOnActionListener?.let { view.setOnActionListener(it) }
        return view
    }
}