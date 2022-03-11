package com.suitcore.feature.note.detail

import com.suitcore.base.presenter.MvpView
import com.suitcore.data.model.Note

interface NoteDetailView: MvpView {
    fun onNoteEdited()

    fun onNoteDeleted()

    fun onNoteLoaded(note: Note)

    fun onEditLoading()

    fun onDeleteLoading()

    fun onFailed(message: String)

    fun onGetNoteFailed(message: String)
}