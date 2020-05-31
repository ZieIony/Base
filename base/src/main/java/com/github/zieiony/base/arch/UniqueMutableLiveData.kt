package com.github.zieiony.base.arch

import androidx.lifecycle.MutableLiveData
import java.lang.ref.WeakReference


open class UniqueMutableLiveData<T> : MutableLiveData<T> {
    enum class NotificationMode {
        // notify on all changes
        ALL,

        // notify on unique values only
        UNIQUE
    }

    val notificationMode: NotificationMode
    private var prevValue: WeakReference<T>? = null

    constructor(notificationMode: NotificationMode = NotificationMode.UNIQUE) : super() {
        this.notificationMode = notificationMode
    }

    constructor(
        value: T?,
        notificationMode: NotificationMode = NotificationMode.UNIQUE
    ) : super(value) {
        this.notificationMode = notificationMode
    }

    override fun setValue(value: T?) {
        if (notificationMode == NotificationMode.ALL || value != prevValue?.get()) {
            super.setValue(value)
            prevValue = if (value == null) null else WeakReference(value)
        }
    }
}