package com.github.zieiony.base.arch

import java.io.Serializable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ViewModelArgumentDelegate<T : Serializable?> : ReadWriteProperty<BaseViewModel, T> {

    override fun getValue(thisRef: BaseViewModel, property: KProperty<*>): T {
        val key = property.name
        return thisRef.arguments.getSerializable(key) as T
    }

    override fun setValue(thisRef: BaseViewModel, property: KProperty<*>, value: T) {
        thisRef.arguments.putSerializable(property.name, value)
    }
}