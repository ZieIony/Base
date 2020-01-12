package com.github.zieiony.base.arch

import androidx.lifecycle.MutableLiveData
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ViewModelStateLiveDataDelegate<T : BaseState?> :
    ReadOnlyProperty<BaseViewModel<*>, MutableLiveData<T>> {

    override fun getValue(thisRef: BaseViewModel<*>, property: KProperty<*>): MutableLiveData<T> {
        val key = property.name
        return thisRef.savedStateHandle.getLiveData<T>(key)
    }
}