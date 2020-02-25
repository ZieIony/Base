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


abstract class BaseFragment() : Fragment(), Navigator {

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

    override fun onResume() {
        super.onResume()
        val result = getResult<Serializable>()
        if (result != null)
            if (onResult(result))
                setResult(null)
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
        val fragment = childFragmentManager.fragmentFactory.instantiate(
            fragmentClass.classLoader!!,
            fragmentClass.name
        )
        arguments?.let {
            val bundle = Bundle()
            fragment.arguments = bundle
            it.forEach { entry ->
                bundle.putSerializable(entry.key, entry.value)
            }
        }
        return onNavigateTo(fragment)
    }

    open fun onNavigateTo(fragment: Fragment): Boolean {
        if (fragment is DialogFragment) {
            fragment.show(childFragmentManager, DIALOG_TAG)
            return true
        }
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
        return false
    }

    fun <T : ViewModel> getViewModel(c: Class<T>, factory: ViewModelProvider.Factory? = null) =
        ViewModelProviders.of(this, factory).get(c)

    companion object {
        private const val DIALOG_TAG = "dialog"
    }
}
