package com.github.zieiony.base.arch

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.github.zieiony.base.util.Logger
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel<T : BaseState>(
    logger: Logger,
    internal val savedStateHandle: SavedStateHandle? = null
) : ViewModel() {

    protected val state = StateLiveData<T>(logger)
    private val disposables = CompositeDisposable()

    private fun addDisposable(disposable: Disposable) = disposables.add(disposable)

    protected fun Disposable.disposeOnCleared() {
        addDisposable(this)
    }

    fun getState(): LiveData<T> = state

    override fun onCleared() = disposables.clear()
}
