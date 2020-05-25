package com.github.zieiony.base.arch

import androidx.lifecycle.MutableLiveData
import com.github.zieiony.base.util.Value
import java.io.Serializable
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ViewModelArgumentLiveDataDelegate<T : Serializable?>() :
    ReadOnlyProperty<BaseViewModel, MutableLiveData<T>> {

    private var initialValue: Value<T>? = null

    constructor(initialValue: T) : this() {
        this.initialValue = Value(initialValue)
    }

    override fun getValue(thisRef: BaseViewModel, property: KProperty<*>): MutableLiveData<T> {
        val key = property.name
        thisRef.liveDatas[key]?.let {
            return it as MutableLiveData<T>
        }
        val liveData = SavingStateLiveData(thisRef.arguments, key, thisRef.arguments.get(key) as T)
        initialValue?.let {
            liveData.setValue(it.value)
        }
        thisRef.liveDatas[key] = liveData as MutableLiveData<Serializable>
        return liveData
    }
}