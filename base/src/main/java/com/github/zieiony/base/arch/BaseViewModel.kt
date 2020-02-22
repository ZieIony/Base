package com.github.zieiony.base.arch

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.github.zieiony.base.app.Navigator
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.io.Serializable

open class BaseViewModel<T : BaseState>(
    internal val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _navigator: Navigator? = null
    var navigator: Navigator?
        get() = _navigator
        set(value) {
            _navigator = value
            _navigator?.let { navigator ->
                navigationEvents.forEach { event ->
                    when (event) {
                        is NavigationEvent.ResultNavigationEvent -> navigator.setResult(event.result)
                        is NavigationEvent.BackNavigationEvent -> navigator.navigateBack()
                        is NavigationEvent.FragmentNavigationEvent -> {
                            navigator.navigateTo(
                                Class.forName(event.className) as Class<out Fragment>,
                                event.arguments
                            )
                        }
                        is NavigationEvent.IntentNavigationEvent -> navigator.onNavigateTo(event.intent)
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

    fun navigateTo(
        target: Class<out Fragment>,
        arguments: HashMap<String, Serializable?>?
    ) {
        val localNavigator = navigator
        if (localNavigator == null) {
            navigationEvents.add(
                NavigationEvent.FragmentNavigationEvent(target.name, arguments)
            )
        } else {
            localNavigator.navigateTo(target, arguments)
        }
    }

    fun navigateTo(fragment: Fragment) {
        val localNavigator = navigator
        if (localNavigator == null) {
            val bundle = fragment.arguments
            navigationEvents.add(
                NavigationEvent.FragmentNavigationEvent(
                    fragment.javaClass.name, if (bundle == null) {
                        null
                    } else {
                        val arguments = HashMap<String, Serializable?>()
                        bundle.keySet().forEach {
                            arguments[it] = bundle[it] as Serializable?
                        }
                        arguments
                    }
                )
            )
        } else {
            localNavigator.navigateTo(fragment)
        }
    }

    fun navigateTo(intent: Intent) {
        val localNavigator = navigator
        if (localNavigator == null) {
            navigationEvents.add(NavigationEvent.IntentNavigationEvent(intent))
        } else {
            localNavigator.onNavigateTo(intent)
        }
    }

    fun navigateBack() {
        val localNavigator = navigator
        if (localNavigator == null) {
            navigationEvents.add(NavigationEvent.BackNavigationEvent)
        } else {
            localNavigator.navigateBack()
        }
    }

    fun <T : Serializable?> setResult(result: T?) {
        val localNavigator = navigator
        if (localNavigator == null) {
            navigationEvents.add(NavigationEvent.ResultNavigationEvent(result))
        } else {
            localNavigator.setResult(result)
        }
    }
}
