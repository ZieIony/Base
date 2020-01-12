package com.github.zieiony.base.arch

import java.io.Serializable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ViewModelStateDelegate<T : Serializable?> : ReadWriteProperty<BaseViewModel<*>, T> {

    override fun getValue(thisRef: BaseViewModel<*>, property: KProperty<*>): T {
        val key = property.name
        return thisRef.savedStateHandle.get<T>(key) as T
    }

    override fun setValue(thisRef: BaseViewModel<*>, property: KProperty<*>, value: T) {
        thisRef.savedStateHandle.set(property.name, value)
    }
}