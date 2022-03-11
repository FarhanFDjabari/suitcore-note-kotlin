package com.suitcore.feature.addnote

import androidx.lifecycle.LifecycleOwner
import com.suitcore.BaseApplication
import com.suitcore.base.presenter.BasePresenter
import com.suitcore.data.model.NoteRequest
import com.suitcore.data.remote.services.APIService
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AddNotePresenter : BasePresenter<AddNoteView>, CoroutineScope {

    @Inject
    lateinit var apiService: APIService
    private var mvpView: AddNoteView? = null
    private var mCompositeDisposable: CompositeDisposable? = CompositeDisposable()
    override val coroutineContext: CoroutineContext get() =  Dispatchers.IO + job
    private var job: Job = Job()

    init {
        BaseApplication.applicationComponent.inject(this)
    }

    fun addNote(title: String, body: String, tags: List<String>) = launch(Dispatchers.Main) {
        val newNote = NoteRequest(title, body, tags, null)
        mvpView?.onLoading()
        runCatching {
            apiService.postNewNote(newNote).await()
        }.onSuccess {
            mvpView?.onNotePosted()
        }.onFailure {
            it.message?.let { it1 -> mvpView?.onFailed(it1) }
        }
    }

    override fun onDestroy() {
        detachView()
    }

    override fun attachView(view: AddNoteView) {
        mvpView = view
        if (mvpView is LifecycleOwner) {
            (mvpView as LifecycleOwner).lifecycle.addObserver(this)
        }
    }

    override fun detachView() {
        mvpView = null
        mCompositeDisposable.let { mCompositeDisposable?.clear() }
    }
}