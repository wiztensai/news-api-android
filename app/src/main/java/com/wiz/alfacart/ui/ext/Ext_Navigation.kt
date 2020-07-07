package com.wiz.alfacart.ui.ext

import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.wiz.alfacart.R
import com.wiz.alfacart.ui.A_Main
import wazma.punjabi.base.BaseActivity
import wazma.punjabi.base.BaseFragment


// REPLACE
fun A_Main.navReplaceTo(fragment: Fragment, tag:String = "") {
    clearBackStack()

    supportFragmentManager.beginTransaction().replace(R.id.navFragment, fragment, tag).addToBackStack(tag).commit()
}

fun BaseFragment.navReplaceTo(fragment: Fragment, tag:String = "") {
    (activity as A_Main).navReplaceTo(fragment, tag)
}

// ADD
fun A_Main.navAddTo(fragment: Fragment, tag:String = "") {
    supportFragmentManager.beginTransaction()
        .setCustomAnimations(
            0,
            R.anim.default_pop_exit,
            0,
            R.anim.default_pop_exit)
        .add(R.id.navFragment, fragment, tag).addToBackStack(tag).commit()
}

fun BaseFragment.navAddTo(fragment: Fragment, tag:String = "") {
    (activity as A_Main).navAddTo(fragment, tag)
}

// POPBACKSTACK
fun BaseFragment.popBackStack(){
    (activity as A_Main).onBackPressed()
}

fun BaseActivity.popBackStack(){
    onBackPressed()
}

/**
 * CLEAR BACKSTACK
 * Remove all entries from the backStack of this fragmentManager
 */
fun BaseActivity.clearBackStack() {
    if (supportFragmentManager.backStackEntryCount > 0) {
        val entry = supportFragmentManager.getBackStackEntryAt(0)
        supportFragmentManager.popBackStack(entry.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}

fun BaseFragment.clearBackStack() {
    (activity as A_Main).clearBackStack()
}

/**
 * tidak bisa digunakan jika ada edittext yang requestfocus
 */
fun BaseFragment.onBackPressed(view: View?,  callback: () -> Unit) {
    if (view == null) {
        return
    }

    view.setFocusableInTouchMode(true)
    view.requestFocus()
    view.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
        if (event.getAction() === KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
            callback()
            true
        } else false
    })
}