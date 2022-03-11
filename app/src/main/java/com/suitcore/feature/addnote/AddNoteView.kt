package com.suitcore.feature.addnote

import com.suitcore.base.presenter.MvpView

interface AddNoteView : MvpView {

    fun onNotePosted()

    fun onLoading()

    fun onFailed(message: String)
}