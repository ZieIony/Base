package com.github.zieiony.base.app

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders


abstract class BaseFragment : Fragment, Navigator {

    var title: String? = null

    private val parentNavigator: Navigator?

    override fun getParentNavigator(): Navigator? {
        return parentNavigator
    }

    constructor(parentNavigator: Navigator) : super() {
        this.parentNavigator = parentNavigator

        arguments = Bundle()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        javaClass.getAnnotation(ScreenAnnotation::class.java)?.let {
            if (it.layout != 0)
                return inflater.inflate(it.layout, container, false)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        javaClass.getAnnotation(ScreenAnnotation::class.java)?.let {
            if (it.title != 0)
                title = context.resources.getString(it.title)
        }
    }

    override fun navigateTo(fragment: BaseFragment) {
        if (!onNavigateTo(fragment))
            getParentNavigator()!!.navigateTo(fragment)
    }

    fun <T : ViewModel> getViewModel(c: Class<T>, factory: ViewModelProvider.Factory? = null): T {
        return ViewModelProviders.of(this, factory).get(c)
    }
}