package com.suitcore.feature.addnote

import com.suitcore.base.ui.adapter.viewholder.BaseItemViewHolder
import com.suitcore.databinding.ItemAddTagsBinding

class AddTagItemView(var binding: ItemAddTagsBinding) : BaseItemViewHolder<String>(binding){

    private var mActionListener: OnActionListener? = null

    override fun bind(data: String?) {
        data?.let {
            binding.tvTag.text = data
            binding.cvTags.setOnClickListener {
                mActionListener?.onClicked(this)
            }
        }
    }

    fun getData() : String {
        return binding.tvTag.text.toString()
    }

    fun setOnActionListener(listener: OnActionListener) {
        mActionListener = listener
    }

    interface OnActionListener {
        fun onClicked(view: AddTagItemView?)
    }
}