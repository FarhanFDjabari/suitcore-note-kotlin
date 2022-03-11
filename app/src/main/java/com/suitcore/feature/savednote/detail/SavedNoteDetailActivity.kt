package com.suitcore.feature.savednote.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.suitcore.R
import com.suitcore.base.ui.BaseActivity
import com.suitcore.data.model.Note
import com.suitcore.databinding.ActivitySavedNoteDetailBinding
import com.suitcore.feature.note.detail.TagDetailAdapter
import com.suitcore.feature.note.detail.TagDetailItemView
import java.text.SimpleDateFormat
import java.util.*

class SavedNoteDetailActivity : BaseActivity<ActivitySavedNoteDetailBinding>(), SavedNoteDetailView, TagDetailItemView.OnActionListener {

    private var savedNoteDetailPresenter: SavedNoteDetailPresenter? = null
    private var tagDetailAdapter: TagDetailAdapter? = null
    private lateinit var detailTagList: MutableList<String>
    private lateinit var noteId: String
    private var isNoteUpdated: Boolean = false

    override fun getViewBinding(): ActivitySavedNoteDetailBinding = ActivitySavedNoteDetailBinding.inflate(layoutInflater)

    override fun onViewReady(savedInstanceState: Bundle?) {
        setupProgressView()
        setupPresenter()
        setupTagAdapter()
        setupTagList()
        setupClickHandler()
    }

    private fun setupProgressView() {
        binding.savedNoteDetailLoading.visibility = View.VISIBLE
        binding.savedNoteDetailLayout.visibility = View.GONE
    }

    private fun setupErrorView() {
        binding.savedNoteDetailLoading.visibility = View.GONE
        binding.errorLayout.visibility = View.VISIBLE
        binding.savedNoteDetailLayout.visibility = View.GONE

        binding.tvTitleErrorView.text = getString(R.string.txt_empty_note)
        binding.tvContentErrorView.text = getString(R.string.txt_empty_note_content)
        binding.btnError.setOnClickListener {
            savedNoteDetailPresenter?.getNoteById(noteId)
        }
    }

    private fun setupPresenter() {
        savedNoteDetailPresenter = SavedNoteDetailPresenter()
        savedNoteDetailPresenter?.attachView(this)
        noteId = intent.extras?.getString("note_id").toString()
        savedNoteDetailPresenter?.getNoteById(noteId)
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
                savedNoteDetailPresenter?.updateNote(noteId, noteTitle, noteBody, noteTags.toList())
            }
        }
        binding.btnDeleteNote.setOnClickListener {
            if (noteId.isNotEmpty()) {
                savedNoteDetailPresenter?.deleteNote(noteId)
            }
        }
    }

    override fun onClicked(view: TagDetailItemView?) {
        view?.getData()?.let {
            removeTag(it)
        }
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
        binding.savedNoteDetailLoading.visibility = View.GONE
        binding.errorLayout.visibility = View.GONE
        binding.savedNoteDetailLayout.visibility = View.VISIBLE
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

    override fun onBackPressed() {
        val returnIntent = Intent().putExtra("isNoteEdited", isNoteUpdated)
        setResult(RESULT_OK, returnIntent)
        super.onBackPressed()
        finish()
    }

}