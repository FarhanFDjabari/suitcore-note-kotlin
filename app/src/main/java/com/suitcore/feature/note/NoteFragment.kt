package com.suitcore.feature.note

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.suitcore.R
import com.suitcore.base.ui.BaseFragment
import com.suitcore.base.ui.recyclerview.BaseRecyclerView
import com.suitcore.data.model.ErrorCodeHelper
import com.suitcore.data.model.Note
import com.suitcore.databinding.FragmentNoteBinding
import com.suitcore.feature.addnote.AddNoteActivity
import com.suitcore.feature.note.detail.NoteDetailActivity

class NoteFragment : BaseFragment<FragmentNoteBinding>(), NoteView, NoteItemView.OnActionListener, NoteItemView.OnLongActionListener {

    private var notePresenter: NotePresenter? = null
    private var noteAdapter: NoteAdapter? = null

    companion object {
        fun newInstance(): Fragment {
            return NoteFragment()
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNoteBinding = FragmentNoteBinding.inflate(inflater, container, false)

    override fun onViewReady(savedInstanceState: Bundle?) {
        setupProgressView()
        setupEmptyView()
        setupErrorView()
        setupNoteList()
        notePresenter = NotePresenter()
        notePresenter?.attachView(this)
        notePresenter?.getNotes()
        binding.fabAddNote.setOnClickListener {
            if (it.id == R.id.fabAddNote) {
                goToActivity(Activity.RESULT_OK, AddNoteActivity::class.java, null)
            }
        }
    }

    private fun setupNoteList() {
        noteAdapter = context?.let {
            NoteAdapter(it)
        }
        binding.rvNote.apply {
            setUpAsStaggered(2, RecyclerView.VERTICAL)
            setAdapter(noteAdapter)
            enableSwipeRefresh(true)
            setSwipeRefreshLoadingListener {
                notePresenter?.getNotes()
            }
        }
        noteAdapter?.setOnActionListener(this)
        noteAdapter?.setOnLongActionListener(this)
        binding.rvNote.showShimmer()
    }

    private fun setupProgressView() {
        R.layout.layout_shimmer_note.apply {
            binding.rvNote.baseShimmerBinding.viewStub.layoutResource = this
        }

        binding.rvNote.baseShimmerBinding.viewStub.inflate()
    }

    private fun setupEmptyView() {
        binding.rvNote.setImageEmptyView(R.drawable.empty_state)
        binding.rvNote.setTitleEmptyView(getString(R.string.txt_empty_note))
        binding.rvNote.setContentEmptyView(getString(R.string.txt_empty_note_content))
        binding.rvNote.setEmptyButtonListener(object : BaseRecyclerView.ReloadListener {

            override fun onClick(v: View?) {
                notePresenter?.getNotes()
            }

        })
    }

    private fun setupErrorView() {
        binding.rvNote.setImageErrorView(R.drawable.empty_state)
        binding.rvNote.setTitleErrorView(getString(R.string.txt_error_no_internet))
        binding.rvNote.setContentErrorView(getString(R.string.txt_error_connection))
        binding.rvNote.setErrorButtonListener(object : BaseRecyclerView.ReloadListener {

            override fun onClick(v: View?) {
                notePresenter?.getNotes()
            }

        })
    }

    override fun onNoteLoaded(notes: List<Note>?) {
        notes.let {
            if (notes?.isNotEmpty()!!) {
                noteAdapter.let {
                    it?.clear()
                }
                noteAdapter?.add(notes)
                binding.rvNote.stopShimmer()
                binding.rvNote.showRecycler()
            }
        }
        finishLoad(binding.rvNote)
    }

    override fun onNoteEmpty() {
        showEmpty()
        binding.rvNote.setLastPage()
    }

    private fun showError() {
        finishLoad(binding.rvNote)
        binding.rvNote.showError()
    }

    private fun showEmpty() {
        finishLoad(binding.rvNote)
        binding.rvNote.showEmpty()
    }

    override fun onFailed(error: Any?) {
        error?.let { ErrorCodeHelper.getErrorMessage(context, it)?.let { msg -> showToast(msg) } }
        showError()
    }

    override fun onNoteSaved() {
        hideLoading()
        showToast("note saved")
    }

    override fun onNoteSaveFailed() {
        hideLoading()
    }

    override fun onClicked(view: NoteItemView?) {
        view?.getNoteId()?.let {
            val bundle = Bundle()
            bundle.putString("note_id", it)
            goToActivity(
                1,
                NoteDetailActivity::class.java,
                bundle,
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val isNotePosted = data?.getBooleanExtra("isNotePosted", false)
            if (isNotePosted == true) {
                notePresenter?.getNotes()
            }
        }
    }

    override fun onLongClicked(view: NoteItemView?) {
        view?.getNote()?.let {
            showDialogConfirmation(
                title = "Save Note",
                message = "Are you sure to save this note in saved note list?",
                confirmCallback = {
                    notePresenter?.saveNote(it)
                    showDialogLoading(dismiss = true, "Saving...")
                }
            )
        }
    }
}