package com.wiz.alfacart.util

import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import com.wiz.alfacart.base.CONST
import com.wiz.alfacart.ui.A_Main
import timber.log.Timber

/**
 * ini harus ditaruh sebelum beginTransaction agar semua transaction ketrack
 */
class SpesificInFragmentUtil(private val activity: A_Main, val listener:SpesificInFragmentInterface) {

    lateinit var onBackStackChangedListener: FragmentManager.OnBackStackChangedListener
    val fragmentManager: FragmentManager

    init {
        fragmentManager = activity.supportFragmentManager
        initBackStackListener()
    }

    fun initBackStackListener() {
        onBackStackChangedListener = object : FragmentManager.OnBackStackChangedListener {
            override fun onBackStackChanged() {

                // count of shown fragment
                val shownBackstackCount = fragmentManager.backStackEntryCount
                Timber.d("Count of shown fragment: $shownBackstackCount")

                var isMain = false

                // CURRENT
                // jika pakai backStackEntry, setiap addBackStack mesti dikasih nama
                val currentIndex = fragmentManager.backStackEntryCount - 1
                val currentFragment = fragmentManager.getBackStackEntryAt(currentIndex).name
                if (currentFragment.equals(CONST.TAG_MAIN)) {
                    isMain = true
                    A_Main.atFragment = CONST.TAG_MAIN

                    Timber.d("currentFragment F_MAIN")
                    logShownFragmentIndex()
                }

                if (isMain) {
                    // nothing
                } else {
                    A_Main.atFragment = CONST.TAG_UKNOWN
                }

                if (isMain) {
                    listener.onSpesificFragment()
                } else {
                    listener.onCommonFragment()
                }

                if (currentIndex == 0) return
            }
        }

        fragmentManager.addOnBackStackChangedListener(onBackStackChangedListener)
    }

    fun logShownFragmentIndex() {
        val idx = fragmentManager.backStackEntryCount - 1
        Timber.d("index of shown fragment: $idx")
    }

    interface SpesificInFragmentInterface {
        fun onSpesificFragment()
        fun onCommonFragment()
    }
}