package com.github.zieiony.base.arch

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.github.zieiony.base.navigation.DeferredNavigator
import com.github.zieiony.base.navigation.Navigator
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel<T : BaseState>(
    internal val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _navigator = DeferredNavigator()
    protected val navigator: Navigator = _navigator

    protected val state by ViewModelStateLiveDataDelegate<T>()
    private val disposables = CompositeDisposable()

    fun initNavigator(navigator: Navigator) {
        _navigator.restoreState(savedStateHandle)
        _navigator.navigator = navigator
    }

    private fun addDisposable(disposable: Disposable) = disposables.add(disposable)

    protected fun Disposable.disposeOnCleared() {
        addDisposable(this)
    }

    fun getState(): LiveData<T> = state

    override fun onCleared() {
        disposables.clear()
        _navigator.saveState(savedStateHandle)
        _navigator.navigator = null
    }

}
