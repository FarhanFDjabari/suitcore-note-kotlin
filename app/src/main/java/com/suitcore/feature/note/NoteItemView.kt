package com.suitcore.feature.note

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.suitcore.base.ui.adapter.viewholder.BaseItemViewHolder
import com.suitcore.data.model.Note
import com.suitcore.databinding.ItemNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class NoteItemView(var binding: ItemNoteBinding) : BaseItemViewHolder<Note>(binding) {

    private var mActionListener: OnActionListener? = null
    private var mLongActionListener: OnLongActionListener? = null
    private var note: Note? = null
    private var tagAdapter: TagAdapter? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun bind(data: Note?) {
        data?.let {
            this.note = data
            binding.tvNoteTitle.text = note?.title
            val format = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.ENGLISH)
            format.timeZone = TimeZone.getTimeZone("GMT+14:00")
            val dateFormat = format.format(note?.updatedAt!!)
            binding.tvNoteDate.text = dateFormat
            binding.tvNoteContent.text = note?.body
            binding.cvNote.setOnClickListener {
                mActionListener?.onClicked(this)
            }
            binding.cvNote.setOnLongClickListener {
                mLongActionListener?.onLongClicked(this)
                true
            }
            setupTagList(note?.tags)
        }
    }

    fun getNoteId() : String? {
        return note?.id
    }

    fun getNote(): Note? {
        return note
    }

    private fun setupTagList(tags: List<String>?) {
        tagAdapter = TagAdapter(itemView.context)
        binding.rvTags.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            binding.rvTags.adapter = tagAdapter
        }
        tags.let {
            if (tags?.isNotEmpty()!!) {
                tagAdapter?.add(tags)
            }
        }
    }

    fun setOnActionListener(listener: OnActionListener) {
        mActionListener = listener
    }

    fun setOnLongActionListener(listener: OnLongActionListener) {
        mLongActionListener = listener
    }

    interface OnActionListener {
        fun onClicked(view: NoteItemView?)
    }

    interface OnLongActionListener {
        fun onLongClicked(view: NoteItemView?)
    }
}