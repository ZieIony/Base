package com.github.zieiony.base.app

import android.content.Context
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
import java.io.Serializable


abstract class BaseFragment : Fragment, Navigator {

    var title: String? = null

    var icon: Drawable? = null

    private val parentNavigator: Navigator?

    private var coldStart = true

    override fun getParentNavigator(): Navigator? {
        return parentNavigator
    }

    constructor(parentNavigator: Navigator) : super() {
        this.parentNavigator = parentNavigator

        arguments = Bundle()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        childFragmentManager.fragmentFactory = NavigatorFragmentFactory(this)

        super.onCreate(savedInstanceState)
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

    override fun onNavigateTo(
        target: Class<out Fragment>,
        arguments: HashMap<String, Serializable>?
    ): Boolean {
        if (target.isAssignableFrom(DialogFragment::class.java)) {
            val fragment = childFragmentManager.fragmentFactory.instantiate(
                target.classLoader!!,
                target.name
            ) as DialogFragment
            arguments?.let {
                val bundle = Bundle()
                fragment.arguments = bundle
                it.forEach { entry ->
                    bundle.putSerializable(entry.key, entry.value)
                }
            }
            fragment.show(childFragmentManager, DIALOG_TAG)
            return true
        }
        return false
    }

    fun <T : ViewModel> getViewModel(c: Class<T>, factory: ViewModelProvider.Factory? = null) =
        ViewModelProviders.of(this, factory).get(c)

    companion object {
        private const val DIALOG_TAG = "dialog"
    }
}
