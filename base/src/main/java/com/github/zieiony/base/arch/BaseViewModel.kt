package com.github.zieiony.base.arch

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.cancel

open class BaseViewModel : ViewModel() {
    val arguments = Bundle()

    internal val liveDatas = HashMap<String, MutableLiveData<out BaseState>?>()

    private val disposables = CompositeDisposable()

    private fun addDisposable(disposable: Disposable) = disposables.add(disposable)

    protected fun Disposable.disposeOnDestroy() {
        addDisposable(this)
    }

    open fun init(bundle: Bundle? = null) {
        if (bundle != null)
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

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
        viewModelScope.coroutineContext.cancel()
    }

}
