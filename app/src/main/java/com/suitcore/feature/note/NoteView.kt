package com.suitcore.feature.note

import com.suitcore.base.presenter.MvpView
import com.suitcore.data.model.Note

interface NoteView : MvpView {
    fun onNoteLoaded(notes: List<Note>?)

    fun onNoteEmpty()

    fun onFailed(error: Any?)

    fun onNoteSaved()

    fun onNoteSaveFailed()
}