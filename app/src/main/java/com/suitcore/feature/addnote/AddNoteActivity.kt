package com.suitcore.feature.addnote

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.suitcore.base.ui.BaseActivity
import com.suitcore.databinding.ActivityAddNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class AddNoteActivity : BaseActivity<ActivityAddNoteBinding>(), AddNoteView, AddTagItemView.OnActionListener {

    private var addNotePresenter: AddNotePresenter? = null
    private var addTagAdapter: AddTagAdapter? = null
    private lateinit var tagList: MutableList<String>

    override fun getViewBinding(): ActivityAddNoteBinding = ActivityAddNoteBinding.inflate(layoutInflater)

    override fun onViewReady(savedInstanceState: Bundle?) {
        setupPresenter()
        setupNoteDate()
        setupTagAdapter()
        setupTagList()
        setupClickHandler()
    }

    override fun onNotePosted() {
        val returnIntent = Intent().putExtra("isNotePosted", true)
        setResult(RESULT_OK, returnIntent)
        onBackPressed()
        showToast("Note Added")
    }

    override fun onLoading() {
        binding.addNoteLoading.visibility = View.VISIBLE
        binding.btnAddNote.visibility = View.INVISIBLE
    }

    override fun onFailed(message: String) {
        binding.addNoteLoading.visibility = View.GONE
        binding.btnAddNote.visibility = View.VISIBLE
        showToast(message)
    }

    private fun setupTagList() {
        tagList = mutableListOf()
        addTagAdapter?.add(tagList)
    }

    private fun setupNoteDate() {
        val format = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.ENGLISH)
        format.timeZone = TimeZone.getTimeZone("GMT+7:00")
        val dateFormat = format.format(Date())
        binding.tvNoteDate.text = dateFormat
    }

    private fun setupTagAdapter() {
        addTagAdapter = AddTagAdapter(this)
        binding.rvTags.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            binding.rvTags.adapter = addTagAdapter
        }
        addTagAdapter?.setOnActionListener(this)
    }

    private fun setupPresenter() {
        addNotePresenter = AddNotePresenter()
        addNotePresenter?.attachView(this)
    }

    private fun setupClickHandler() {
        binding.ivBackBtn.setOnClickListener {
            onBackPressed()
        }
        binding.btnAddNote.setOnClickListener {
            val noteTitle = binding.etNoteTitle.text.toString()
            val noteBody = binding.etNoteBody.text.toString()
            val noteTags = tagList
            if (noteTitle.isNotEmpty() && noteBody.isNotEmpty()) {
                addNotePresenter?.addNote(noteTitle, noteBody, noteTags.toList())
            }
        }
        binding.btnAddTag.setOnClickListener {
            if (binding.etAddTag.text?.isNotEmpty() == true) {
                tagList.add(binding.etAddTag.text.toString())
                addTagAdapter?.clear()
                addTagAdapter?.add(tagList)
                binding.etAddTag.text?.clear()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onClicked(view: AddTagItemView?) {
        view?.getData()?.let {
            tagList.remove(it)
            addTagAdapter?.clear()
            addTagAdapter?.add(tagList)
        }
    }
}