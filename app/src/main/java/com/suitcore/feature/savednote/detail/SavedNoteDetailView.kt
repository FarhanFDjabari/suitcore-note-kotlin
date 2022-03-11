package com.suitcore.feature.savednote.detail

import com.suitcore.base.presenter.MvpView
import com.suitcore.data.model.Note

interface SavedNoteDetailView: MvpView {
    fun onNoteEdited()

    fun onNoteDeleted()

    fun onNoteLoaded(note: Note)

    fun onEditLoading()

    fun onDeleteLoading()

    fun onFailed(message: String)

    fun onGetNoteFailed(message: String)
}