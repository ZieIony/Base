package com.github.zieiony.base.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager


object Keyboard {
    fun show(view: View) {
        KeyboardRunnable(view).run()
    }

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

class KeyboardRunnable(var view: View) : Runnable {

    private var activity: Activity? = null

    init {
        var context = view.context
        while (context is ContextWrapper && context !is Activity)
            context = context.baseContext
        if (context is Activity)
            activity = context
    }

    override fun run() {
        if (activity == null)
            return

        val inputMethodManager = activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        if (!(view.isFocusable && view.isFocusableInTouchMode)) {
            view.isFocusable = true
            view.isFocusableInTouchMode = true
            return
        } else if (!view.requestFocus()) {
            post()
        } else if (!inputMethodManager.isActive(view)) {
            post()
        } else if (!inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)) {
            post()
        }

        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    private fun post() {
        handler.postDelayed(this, INTERVAL_MS)
    }

    companion object {
        private val INTERVAL_MS = 100L
        private val handler = Handler(Looper.getMainLooper())
    }
}
