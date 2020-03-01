package com.github.zieiony.base.arch

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import java.io.Serializable

internal class SavingStateLiveData<T : Serializable?> : MutableLiveData<T> {
    private var key: String
    private var bundle: Bundle?

    constructor(bundle: Bundle?, key: String, value: T) : super(value) {
        this.key = key
        this.bundle = bundle
    }

    constructor(bundle: Bundle?, key: String) : super() {
        this.key = key
        this.bundle = bundle
    }

    override fun setValue(value: T) {
        if (bundle != null) {
            bundle!!.putSerializable(key, value)
        }
        super.setValue(value)
    }
}