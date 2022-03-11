package com.suitcore.feature.savednote.detail

import androidx.lifecycle.LifecycleOwner
import com.suitcore.BaseApplication
import com.suitcore.base.presenter.BasePresenter
import com.suitcore.data.local.RealmHelper
import com.suitcore.data.model.Note
import io.reactivex.disposables.CompositeDisposable
import io.realm.Realm
import io.realm.RealmList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SavedNoteDetailPresenter: BasePresenter<SavedNoteDetailView>, CoroutineScope {

    private var mvpView: SavedNoteDetailView? = null
    private var mRealm: RealmHelper<Note>? = RealmHelper()
    private var mCompositeDisposable: CompositeDisposable? = CompositeDisposable()
    override val coroutineContext: CoroutineContext get() =  Dispatchers.IO + job
    private var job: Job = Job()

    init {
        BaseApplication.applicationComponent.inject(this)
    }

    fun getNoteById(id: String) = launch(Dispatchers.Main) {
        runCatching {
            val realm: Realm = Realm.getDefaultInstance()
            realm.where(Note::class.java).equalTo("id", id).findFirst()
        }.onSuccess { data ->
            data?.let {
                mvpView?.onNoteLoaded(it)
            }
        }.onFailure {
            it.message?.let { it1 -> mvpView?.onFailed(it1) }
        }
    }

    fun updateNote(id: String, title: String, body: String, tags: List<String>) = launch(Dispatchers.Main) {
        mvpView?.onEditLoading()
        runCatching {
            val realm: Realm = Realm.getDefaultInstance()
            val noteData = realm.where(Note::class.java).equalTo("id", id).findFirst()
            val realmTags = RealmList<String>()
            realmTags.addAll(tags)

            if (noteData != null) {
                realm.beginTransaction()
                noteData.title = title
                noteData.body = body
                noteData.tags = realmTags
                realm.copyToRealmOrUpdate(noteData)
                realm.commitTransaction()
            }

        }.onSuccess {
            mvpView?.onNoteEdited()
        }.onFailure {
            it.message?.let { it1 -> mvpView?.onFailed(it1) }
        }
    }

    fun deleteNote(id: String) = launch(Dispatchers.Main) {
        mvpView?.onDeleteLoading()
        runCatching {
            val realm: Realm = Realm.getDefaultInstance()
            val noteData = realm.where(Note::class.java).equalTo("id", id).findFirst()
            if (noteData != null) {
                mRealm?.deleteData(noteData)
            }
        }.onSuccess {
            mvpView?.onNoteDeleted()
        }.onFailure {
            it.message?.let { it1 -> mvpView?.onFailed(it1) }
        }
    }

    override fun onDestroy() {
        detachView()
    }

    override fun attachView(view: SavedNoteDetailView) {
        mvpView = view
        if (mvpView is LifecycleOwner) {
            (mvpView as LifecycleOwner).lifecycle.addObserver(this)
        }
    }

    override fun detachView() {
        mvpView = null
        mCompositeDisposable?.let { mCompositeDisposable?.clear() }
    }

}