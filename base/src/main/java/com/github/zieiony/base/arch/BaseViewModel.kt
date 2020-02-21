package com.github.zieiony.base.arch

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.github.zieiony.base.app.Navigator
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.io.Serializable

open class BaseViewModel<T : BaseState>(
    internal val savedStateHandle: SavedStateHandle
) : ViewModel(), Navigator {

    private var _navigator: Navigator? = null
    var navigator: Navigator?
        get() = _navigator
        set(value) {
            _navigator = value
            _navigator?.let { navigator ->
                navigationEvents.forEach { event ->
                    when {
                        event.result != null -> navigator.setResult(event.result)
                        event.className == null -> navigator.navigateBack()
                        else -> navigator.navigateTo(
                            Class.forName(event.className),
                            event.arguments
                        )
                    }
                    navigationEvents.remove(event)
                }
            }
        }

    private var navigationEvents by ViewModelStateDelegate<ArrayList<NavigationEvent>>()
    protected val state by ViewModelStateLiveDataDelegate<T>()
    private val disposables = CompositeDisposable()

    private fun addDisposable(disposable: Disposable) = disposables.add(disposable)

    protected fun Disposable.disposeOnCleared() {
        addDisposable(this)
    }

    fun getState(): LiveData<T> = state

    override fun onCleared() {
        disposables.clear()
        navigator = null
    }


    override fun getParentNavigator(): Navigator? {
        return navigator?.parentNavigator
    }

    override fun navigateTo(target: Class<out Any>, arguments: HashMap<String, Serializable>?) {
        if (!onNavigateTo(target, arguments)) {
            val localNavigator = parentNavigator
            if (localNavigator == null) {
                navigationEvents.add(NavigationEvent(target.name, arguments))
            } else {
                localNavigator.navigateTo(target, arguments)
            }
        }
    }

    override fun onNavigateTo(
        target: Class<out Any>,
        arguments: HashMap<String, Serializable>?
    ): Boolean {
        val localNavigator = navigator
        if (localNavigator == null) {
            navigationEvents.add(NavigationEvent(target.name, arguments))
            return true
        } else {
            return localNavigator.onNavigateTo(target, arguments)
        }
    }

    override fun navigateBack() {
        val localNavigator = navigator
        if (localNavigator == null) {
            navigationEvents.add(NavigationEvent())
        } else {
            localNavigator.navigateBack()
        }
    }

    override fun onNavigateBack(): Boolean {
        val localNavigator = navigator
        if (localNavigator == null) {
            navigationEvents.add(NavigationEvent())
            return true
        } else {
            return localNavigator.onNavigateBack()
        }
    }

    override fun <T : Serializable?> getResult(): T? {
        return null
    }

    override fun <T : Serializable?> setResult(result: T?) {
        val localNavigator = navigator
        if (localNavigator == null) {
            navigationEvents.add(NavigationEvent(result = result))
        } else {
            localNavigator.setResult(result)
        }
    }

    override fun onResult(result: Serializable?): Boolean {
        return false
    }
}
