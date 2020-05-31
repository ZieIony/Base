package com.github.zieiony.base.arch

import android.os.Bundle
import java.io.Serializable

internal class SavingStateLiveData<T : Serializable?>(
    private var bundle: Bundle?,
    private var key: String,
    value: T?,
    notificationMode: NotificationMode = NotificationMode.UNIQUE
) : UniqueMutableLiveData<T>(value, notificationMode) {

    override fun setValue(value: T?) {
        if (bundle != null) {
            bundle!!.putSerializable(key, value)
        }
        super.setValue(value)
    }
}