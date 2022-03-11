package com.suitcore.feature.savednote

import com.suitcore.base.presenter.MvpView
import com.suitcore.data.model.Note

interface SavedNoteView : MvpView {
    fun onNoteLoaded(notes: List<Note>?)

    fun onNoteEmpty()

    fun onFailed(error: Any?)

    fun onNoteRemoved()

    fun onNoteRemoveFailed()
}