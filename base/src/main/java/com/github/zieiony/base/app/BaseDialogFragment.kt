package com.github.zieiony.base.app

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.github.zieiony.base.navigation.Navigator
import java.io.Serializable


abstract class BaseDialogFragment() : DialogFragment(), Navigator {

    open val layoutId: Int = INVALID_ID
    open val titleId: Int = INVALID_ID
    open val iconId: Int = INVALID_ID

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
        if (layoutId != INVALID_ID)
            return inflater.inflate(layoutId, container, false)
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

        if (titleId != INVALID_ID)
            title = context.resources.getString(titleId)
        if (iconId != INVALID_ID)
            icon = context.resources.getDrawable(iconId)
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

    override fun navigateTo(
        fragmentClass: Class<out Fragment?>,
        arguments: java.util.HashMap<String, Serializable?>?
    ) {
        if (!onNavigateTo(fragmentClass, arguments))
            parentNavigator?.navigateTo(fragmentClass, arguments)
    }

    override fun navigateTo(fragment: Fragment) {
        if (!onNavigateTo(fragment))
            parentNavigator?.navigateTo(fragment)
    }

    override fun navigateTo(intent: Intent) {
        if (!onNavigateTo(intent))
            parentNavigator?.navigateTo(intent)
    }

    open fun onNavigateTo(
        fragmentClass: Class<out Fragment>,
        arguments: java.util.HashMap<String, Serializable?>?
    ): Boolean {
        return false
    }

    open fun onNavigateTo(fragment: Fragment): Boolean {
        return false
    }

    open fun onNavigateTo(intent: Intent): Boolean {
        return false
    }

    override fun navigateBack() {
        if (!onNavigateBack())
            parentNavigator?.navigateBack()
    }

    open fun onNavigateBack(): Boolean {
        dismiss()
        return true
    }

    fun <T : ViewModel> getViewModel(c: Class<T>, factory: ViewModelProvider.Factory? = null) =
        ViewModelProviders.of(this, factory).get(c)

    companion object {
        private const val INVALID_ID = 0
    }
}
