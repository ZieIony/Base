package com.github.zieiony.base.util

import android.content.Context
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager


object Keyboard {
    fun hide(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun hide(window: Window) {
        window.currentFocus?.let {
            hide(it)
        }
    }
}