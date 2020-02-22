package com.github.zieiony.base.app

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders


abstract class BaseDialogFragment() : DialogFragment(), Navigator {

    var title: String? = null

    var icon: Drawable? = null

    private var parentNavigator: Navigator? = null

    private var coldStart = true

    override fun getParentNavigator() = parentNavigator

    init {
        arguments = Bundle()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (savedInstanceState != null)
            coldStart = false
        javaClass.getAnnotation(ScreenAnnotation::class.java)?.let {
            if (it.layoutId != 0)
                return inflater.inflate(it.layoutId, container, false)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        var parent = parentFragment
        while (parent != null) {
            if (parent is Navigator) {
                parentNavigator = parent
                break
            }
            parent = parent.parentFragment
        }
        if (parentNavigator == null && activity is Navigator)
            parentNavigator = activity as Navigator

        javaClass.getAnnotation(ScreenAnnotation::class.java)?.let {
            if (it.titleId != 0)
                title = context.resources.getString(it.titleId)
            if (it.iconId != 0)
                icon = context.resources.getDrawable(it.iconId)
        }
    }

    open fun onColdStart() {
    }

    override fun onStart() {
        super.onStart()
        if (coldStart) {
            onColdStart()
            coldStart = false
        }
    }

    override fun onDetach() {
        super.onDetach()
        parentNavigator = null
    }

    override fun onNavigateBack(): Boolean {
        dismiss()
        return true
    }

    fun <T : ViewModel> getViewModel(c: Class<T>, factory: ViewModelProvider.Factory? = null) =
        ViewModelProviders.of(this, factory).get(c)
}
