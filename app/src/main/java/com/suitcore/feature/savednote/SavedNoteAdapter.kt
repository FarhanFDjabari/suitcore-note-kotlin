package com.suitcore.feature.savednote

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.suitcore.base.ui.adapter.BaseRecyclerAdapter
import com.suitcore.data.model.Note
import com.suitcore.databinding.ItemNoteBinding
import com.suitcore.feature.note.NoteItemView

class SavedNoteAdapter(var context: Context): BaseRecyclerAdapter<Note, NoteItemView>() {

    private lateinit var itemNoteBinding: ItemNoteBinding
    private var mOnActionListener: NoteItemView.OnActionListener? = null
    private var mOnLongActionListener: NoteItemView.OnLongActionListener? = null

    fun setOnActionListener(onActionListener: NoteItemView.OnActionListener) {
        mOnActionListener = onActionListener
    }

    fun setOnLongActionListener(onLongActionListener: NoteItemView.OnLongActionListener) {
        mOnLongActionListener = onLongActionListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteItemView {
        itemNoteBinding = ItemNoteBinding.inflate(LayoutInflater.from(context), parent, false)
        val view = NoteItemView(itemNoteBinding)
        mOnActionListener?.let {
            view.setOnActionListener(it)
        }
        mOnLongActionListener?.let {
            view.setOnLongActionListener(it)
        }
        return view
    }
}