package com.suitcore.feature.note

import androidx.lifecycle.LifecycleOwner
import com.suitcore.BaseApplication
import com.suitcore.base.presenter.BasePresenter
import com.suitcore.data.local.RealmHelper
import com.suitcore.data.model.Note
import com.suitcore.data.remote.services.APIService
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class NotePresenter : BasePresenter<NoteView>, CoroutineScope {
    @Inject
    lateinit var apiService: APIService
    private var mvpView: NoteView? = null
    private var mRealm: RealmHelper<Note>? = RealmHelper()
    private var mCompositeDisposable: CompositeDisposable? = CompositeDisposable()
    override val coroutineContext: CoroutineContext get() =  Dispatchers.IO + job
    private var job: Job = Job()

    init {
        BaseApplication.applicationComponent.inject(this)
    }

    fun getNotes() = launch(Dispatchers.Main) {
        runCatching {
            apiService.getNotes().await()
        }.onSuccess {  data ->
            if (data.arrayData?.isNotEmpty()!!) {
                val rest = data.arrayData
                mvpView?.onNoteLoaded(rest)
            } else {
                mvpView?.onNoteEmpty()
            }
        }.onFailure {
            mvpView?.onFailed(it)
        }
    }

    fun saveNote(note: Note) = launch(Dispatchers.Main) {
        if (mRealm?.getSingleData(note) == null) {
            runCatching {
                mRealm?.saveObject(note)
            }.onSuccess {
                mvpView?.onNoteSaved()
            }.onFailure {
                mvpView?.onFailed("error when saving note")
            }
        } else {
            mvpView?.onNoteSaveFailed()
        }
    }

    override fun onDestroy() {
        detachView()
    }

    override fun attachView(view: NoteView) {
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