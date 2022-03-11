package com.suitcore.di.component

import com.suitcore.di.module.ApplicationModule
import com.suitcore.di.scope.SuitCoreApplicationScope
import com.suitcore.feature.addnote.AddNotePresenter
import com.suitcore.feature.note.NotePresenter
import com.suitcore.feature.note.detail.NoteDetailPresenter
import com.suitcore.feature.savednote.SavedNotePresenter
import com.suitcore.feature.savednote.detail.SavedNoteDetailPresenter
import com.suitcore.feature.splashscreen.SplashScreenPresenter
import com.suitcore.firebase.remoteconfig.RemoteConfigPresenter
import com.suitcore.onesignal.OneSignalPresenter
import dagger.Component

@SuitCoreApplicationScope
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(splashScreenPresenter: SplashScreenPresenter)

    fun inject(oneSignalPresenter: OneSignalPresenter)

    fun inject(notePresenter: NotePresenter)

    fun inject(savedNotePresenter: SavedNotePresenter)

    fun inject(remoteConfigPresenter: RemoteConfigPresenter)

    fun inject(addNotePresenter: AddNotePresenter)

    fun inject(noteDetailPresenter: NoteDetailPresenter)

    fun inject(savedNoteDetailPresenter: SavedNoteDetailPresenter)
}