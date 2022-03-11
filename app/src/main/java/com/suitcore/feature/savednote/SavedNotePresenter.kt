package com.suitcore.feature.savednote

import androidx.lifecycle.LifecycleOwner
import com.suitcore.BaseApplication
import com.suitcore.base.presenter.BasePresenter
import com.suitcore.data.local.RealmHelper
import com.suitcore.data.model.Note
import com.suitcore.data.remote.services.APIService
import io.reactivex.disposables.CompositeDisposable
import io.realm.Realm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SavedNotePresenter : BasePresenter<SavedNoteView>, CoroutineScope {
    @Inject
    lateinit var apiService: APIService
    private var mvpView: SavedNoteView? = null
    private var mRealm: RealmHelper<Note>? = RealmHelper()
    private var mCompositeDisposable: CompositeDisposable? = CompositeDisposable()
    override val coroutineContext: CoroutineContext get() =  Dispatchers.IO + job
    private var job: Job = Job()

    init {
        BaseApplication.applicationComponent.inject(this)
    }

    fun getNotes() = launch(Dispatchers.Main) {
        runCatching {
            val realm: Realm = Realm.getDefaultInstance()
            realm.where(Note::class.java).findAll()
        }.onSuccess { data ->
            if (!data?.toList().isNullOrEmpty()) {
                val rest = data?.toList()
                mvpView?.onNoteLoaded(rest)
            } else {
                mvpView?.onNoteEmpty()
            }
        }.onFailure {
            mvpView?.onFailed(it)
        }
    }

    fun removeNote(note: Note) = launch(Dispatchers.Main) {
//        if (mRealm?.getSingleData(note) != null) {
            runCatching {
                mRealm?.deleteData(note)
            }.onSuccess {
                mvpView?.onNoteRemoved()
            }.onFailure {
                mvpView?.onFailed(it)
            }
//        } else {
//            mvpView?.onNoteRemoveFailed()
//        }
    }

    override fun onDestroy() {
        detachView()
    }

    override fun attachView(view: SavedNoteView) {
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