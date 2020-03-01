package com.github.zieiony.base.arch

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel<T : BaseState> {
    val arguments = Bundle()

    internal val liveDatas = HashMap<String, MutableLiveData<out BaseState>?>()

    protected val state by ViewModelArgumentLiveDataDelegate<T>()
    private val disposables = CompositeDisposable()

    private fun addDisposable(disposable: Disposable) = disposables.add(disposable)

    protected fun Disposable.disposeOnDestroy() {
        addDisposable(this)
    }

    fun getState(): LiveData<T> = state

    open fun init(bundle: Bundle? = null) {
        arguments.putAll(bundle)

        onInit(arguments)
    }

    open fun onInit(bundle: Bundle) {
    }

    open fun saveState(bundle: Bundle) {
        bundle.putAll(arguments)

        onSaveState(bundle)
    }

    open fun onSaveState(bundle: Bundle) {
    }

    open fun destroy() {
        disposables.clear()

        onDestroy()
    }

    open fun onDestroy() {
    }

}
