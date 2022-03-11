package com.suitcore.feature.tabmenu

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.suitcore.feature.note.NoteFragment
import com.suitcore.feature.savednote.SavedNoteFragment

class MainPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var noteFragment: Fragment? = null
    private var savedNoteFragment: Fragment? = null

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> generateNoteFragment()
        1 -> generateSavedNoteFragment()
        else -> generateNoteFragment()
    }
    override fun getCount(): Int = 2

    private fun generateNoteFragment(): Fragment = if (noteFragment == null) {
        NoteFragment.newInstance()
    }else{
        noteFragment!!
    }

    private fun generateSavedNoteFragment(): Fragment = if (savedNoteFragment == null) {
        SavedNoteFragment.newInstance()
    }else{
        savedNoteFragment!!
    }

}