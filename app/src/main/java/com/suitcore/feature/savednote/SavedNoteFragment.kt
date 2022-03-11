package com.suitcore.feature.savednote

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
import com.suitcore.databinding.FragmentSavedNoteBinding
import com.suitcore.feature.note.NoteItemView
import com.suitcore.feature.savednote.detail.SavedNoteDetailActivity

class SavedNoteFragment : BaseFragment<FragmentSavedNoteBinding>(), SavedNoteView, NoteItemView.OnActionListener, NoteItemView.OnLongActionListener {

    private var savedNotePresenter: SavedNotePresenter? = null
    private var savedNoteAdapter: SavedNoteAdapter? = null

    companion object {
        fun newInstance(): Fragment {
            return SavedNoteFragment()
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSavedNoteBinding = FragmentSavedNoteBinding.inflate(inflater, container, false)

    override fun onViewReady(savedInstanceState: Bundle?) {
        setupProgressView()
        setupEmptyView()
        setupSavedNoteList()
        setupPresenter()
    }

    private fun setupPresenter() {
        savedNotePresenter = SavedNotePresenter()
        savedNotePresenter?.attachView(this)
        savedNotePresenter?.getNotes()
    }

    private fun setupSavedNoteList() {
        savedNoteAdapter = context?.let {
            SavedNoteAdapter(it)
        }
        binding.rvSavedNote.apply {
            setUpAsStaggered(2, RecyclerView.VERTICAL)
            setAdapter(savedNoteAdapter)
            enableSwipeRefresh(true)
            setSwipeRefreshLoadingListener {
                savedNotePresenter?.getNotes()
            }
        }
        savedNoteAdapter?.setOnActionListener(this)
        savedNoteAdapter?.setOnLongActionListener(this)
        binding.rvSavedNote.showShimmer()
    }

    private fun setupProgressView() {
        R.layout.layout_shimmer_note.apply {
            binding.rvSavedNote.baseShimmerBinding.viewStub.layoutResource = this
        }

        binding.rvSavedNote.baseShimmerBinding.viewStub.inflate()
    }

    private fun setupEmptyView() {
        binding.rvSavedNote.setImageEmptyView(R.drawable.empty_state)
        binding.rvSavedNote.setTitleEmptyView(getString(R.string.txt_empty_note))
        binding.rvSavedNote.setContentEmptyView(getString(R.string.txt_empty_note_content))
        binding.rvSavedNote.setEmptyButtonListener(object : BaseRecyclerView.ReloadListener {

            override fun onClick(v: View?) {
                savedNotePresenter?.getNotes()
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val isNoteEdited = data?.getBooleanExtra("isNoteEdited", false)
            if (isNoteEdited == true) {
                savedNotePresenter?.getNotes()
            }
        }
    }

    override fun onNoteLoaded(notes: List<Note>?) {
        notes.let {
            if (notes?.isNotEmpty()!!) {
                savedNoteAdapter.let {
                    it?.clear()
                }
                savedNoteAdapter?.add(notes)
                binding.rvSavedNote.stopShimmer()
                binding.rvSavedNote.showRecycler()
            }
        }
        finishLoad(binding.rvSavedNote)
    }

    override fun onNoteEmpty() {
        finishLoad(binding.rvSavedNote)
        binding.rvSavedNote.showEmpty()
        binding.rvSavedNote.setLastPage()
    }

    override fun onFailed(error: Any?) {
        error?.let { ErrorCodeHelper.getErrorMessage(context, it)?.let { msg -> showToast(msg) } }
    }

    override fun onNoteRemoved() {
        hideLoading()
        savedNotePresenter?.getNotes()
        showToast("note removed")
    }

    override fun onNoteRemoveFailed() {
        hideLoading()
    }

    override fun onClicked(view: NoteItemView?) {
        view?.getNoteId()?.let {
            val bundle = Bundle()
            bundle.putString("note_id", it)
            goToActivity(
                1,
                SavedNoteDetailActivity::class.java,
                bundle,
            )
        }

    }

    override fun onLongClicked(view: NoteItemView?) {
        view?.getNote()?.let {
            showDialogConfirmation(
                title = "Remove Note",
                message = "Are you sure to remove this note from saved note list?",
                confirmCallback = {
                    savedNotePresenter?.removeNote(it)
                    showDialogLoading(dismiss = true, "Saving...")
                }
            )
        }
    }
}