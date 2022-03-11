package com.suitcore.feature.note.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.suitcore.R
import com.suitcore.base.ui.BaseActivity
import com.suitcore.data.model.Note
import com.suitcore.databinding.ActivityNoteDetailBinding
import java.text.SimpleDateFormat
import java.util.*

class NoteDetailActivity : BaseActivity<ActivityNoteDetailBinding>(), NoteDetailView, TagDetailItemView.OnActionListener {

    private var noteDetailPresenter: NoteDetailPresenter? = null
    private var tagDetailAdapter: TagDetailAdapter? = null
    private lateinit var detailTagList: MutableList<String>
    private lateinit var noteId: String
    private var isNoteUpdated: Boolean = false

    override fun getViewBinding(): ActivityNoteDetailBinding = ActivityNoteDetailBinding.inflate(layoutInflater)

    override fun onViewReady(savedInstanceState: Bundle?) {
        setupProgressView()
        setupPresenter()
        setupTagAdapter()
        setupTagList()
        setupClickHandler()
    }

    private fun setupProgressView() {
        binding.noteDetailLoading.visibility = View.VISIBLE
        binding.noteDetailLayout.visibility = View.GONE
    }

    private fun setupErrorView() {
        binding.noteDetailLoading.visibility = View.GONE
        binding.errorLayout.visibility = View.VISIBLE
        binding.noteDetailLayout.visibility = View.GONE

        binding.tvTitleErrorView.text = getString(R.string.txt_empty_note)
        binding.tvContentErrorView.text = getString(R.string.txt_empty_note_content)
        binding.btnError.setOnClickListener {
            noteDetailPresenter?.getNoteById(noteId)
        }
    }

    private fun setupPresenter() {
        noteDetailPresenter = NoteDetailPresenter()
        noteDetailPresenter?.attachView(this)
        noteId = intent.extras?.getString("note_id").toString()
        noteDetailPresenter?.getNoteById(noteId)
    }

    private fun setupTagAdapter() {
        tagDetailAdapter = TagDetailAdapter(this)
        binding.rvTags.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            binding.rvTags.adapter = tagDetailAdapter
        }
        tagDetailAdapter?.setOnActionListener(this)
    }

    private fun setupTagList() {
        detailTagList = mutableListOf()
        tagDetailAdapter?.add(detailTagList)
    }

    override fun onNoteEdited() {
        binding.editNoteLoading.visibility = View.GONE
        binding.btnEditNote.visibility = View.VISIBLE
        isNoteUpdated = true
        showToast("Note Updated")
        setNoteTimestamp(null, isNoteUpdated = isNoteUpdated)
    }

    override fun onNoteDeleted() {
        isNoteUpdated = true
        onBackPressed()
        showToast("Note Deleted")
    }

    override fun onNoteLoaded(note: Note) {
        setupData(note)
        binding.noteDetailLoading.visibility = View.GONE
        binding.errorLayout.visibility = View.GONE
        binding.noteDetailLayout.visibility = View.VISIBLE
    }

    private fun setNoteTimestamp(time: Date?, isNoteUpdated: Boolean) {
        val format = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.ENGLISH)
        if (isNoteUpdated) {
            format.timeZone = TimeZone.getTimeZone("GMT+7:00")
        } else {
            format.timeZone = TimeZone.getTimeZone("GMT+14:00")
        }
        val dateFormat = format.format(time?: Date())
        binding.tvNoteDate.text = dateFormat
    }

    private fun setupData(note:Note) {
        setNoteTimestamp(note.updatedAt, false)
        binding.etNoteTitle.setText(note.title)
        binding.etNoteBody.setText(note.body)
        note.tags?.toList()?.let { addNewTag(it) }
    }

    private fun addNewTag(tags: List<String>) {
        detailTagList.clear()
        detailTagList.addAll(tags)
        tagDetailAdapter?.add(detailTagList)
    }

    private fun addNewTag(tag: String) {
        detailTagList.add(tag)
        tagDetailAdapter?.clear()
        tagDetailAdapter?.add(detailTagList)
    }

    private fun removeTag(tag: String) {
        detailTagList.remove(tag)
        tagDetailAdapter?.clear()
        tagDetailAdapter?.add(detailTagList)
    }

    private fun setupClickHandler() {
        binding.ivBackBtn.setOnClickListener {
            onBackPressed()
        }
        binding.btnAddTag.setOnClickListener {
            if (binding.etAddTag.text?.isNotEmpty() == true) {
                addNewTag(binding.etAddTag.text.toString())
                binding.etAddTag.text?.clear()
            }
        }
        binding.btnEditNote.setOnClickListener {
            val noteTitle = binding.etNoteTitle.text.toString()
            val noteBody = binding.etNoteBody.text.toString()
            val noteTags = detailTagList
            if (noteTitle.isNotEmpty() && noteBody.isNotEmpty() && noteId.isNotEmpty()) {
                noteDetailPresenter?.updateNote(noteId, noteTitle, noteBody, noteTags.toList())
            }
        }
        binding.btnDeleteNote.setOnClickListener {
            if (noteId.isNotEmpty()) {
                noteDetailPresenter?.deleteNote(noteId)
            }
        }
    }

    override fun onEditLoading() {
        binding.editNoteLoading.visibility = View.VISIBLE
        binding.btnEditNote.visibility = View.INVISIBLE
    }

    override fun onDeleteLoading() {
        binding.deleteNoteLoading.visibility = View.VISIBLE
        binding.btnDeleteNote.visibility = View.INVISIBLE
    }

    override fun onFailed(message: String) {
        binding.deleteNoteLoading.visibility = View.GONE
        binding.editNoteLoading.visibility = View.GONE
        binding.btnDeleteNote.visibility = View.VISIBLE
        binding.btnEditNote.visibility = View.VISIBLE
        showToast(message)
    }

    override fun onGetNoteFailed(message: String) {
        setupErrorView()
        showToast(message)
    }

    override fun onClicked(view: TagDetailItemView?) {
        view?.getData()?.let {
            removeTag(it)
        }
    }

    override fun onBackPressed() {
        val returnIntent = Intent().putExtra("isNotePosted", isNoteUpdated)
        setResult(RESULT_OK, returnIntent)
        super.onBackPressed()
        finish()
    }

}