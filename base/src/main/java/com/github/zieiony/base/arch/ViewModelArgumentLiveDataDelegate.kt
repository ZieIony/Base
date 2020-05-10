package com.github.zieiony.base.arch

import androidx.lifecycle.MutableLiveData
import java.io.Serializable
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ViewModelArgumentLiveDataDelegate<T : Serializable?> :
    ReadOnlyProperty<BaseViewModel, MutableLiveData<T>> {

    override fun getValue(thisRef: BaseViewModel, property: KProperty<*>): MutableLiveData<T> {
        val key = property.name
        thisRef.liveDatas[key]?.let{
            return it as MutableLiveData<T>
        }
        val liveData = SavingStateLiveData(thisRef.arguments, key, thisRef.arguments.get(key) as T)
        thisRef.liveDatas[key] = liveData as MutableLiveData<Serializable>
        return liveData
    }
}