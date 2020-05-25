package com.github.zieiony.base.app

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.zieiony.base.util.Value
import java.io.Serializable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class FragmentArgumentDelegate<T : Serializable?>() : ReadWriteProperty<Fragment, T> {

    private var initialValue: Value<T>? = null

    constructor(initialValue: T) : this() {
        this.initialValue = Value(initialValue)
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        val key = property.name
        val arguments = thisRef.arguments ?: throw IllegalStateException("Arguments cannot be null")
        if (!arguments.containsKey(key)) {
            if (property.returnType.isMarkedNullable)
                return null as T
            initialValue?.let {
                arguments.putSerializable(key, it.value)
                return it.value
            }
            throw java.lang.IllegalStateException("$key not present in arguments")
        }
        return arguments.get(key) as T
    }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        val args = thisRef.arguments ?: Bundle().also(thisRef::setArguments)
        val key = property.name
        args.putSerializable(key, value)
    }
}
