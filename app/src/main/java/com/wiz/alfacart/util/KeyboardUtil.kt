package com.wiz.alfacart.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyboardUtil {

    interface OnKeyboardVisibiltyListener {
        fun onVisibilityChange(var1: Boolean)
    }

    fun hideKeyboard(anyView: View) {
        if (anyView.context is Activity) {
            if (anyView != null) {
                val imm = anyView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(anyView.windowToken, 0)
            }
        }
    }

    fun showKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(2, 0)
    }

    fun showKeyboard(context: Context) {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(2, 0)
    }
}
