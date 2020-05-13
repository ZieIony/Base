package com.github.zieiony.base.navigation

import android.content.Intent
import android.os.Parcel
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable


internal sealed class NavigationEvent : Serializable {
    internal class FragmentNavigationEvent(
        val className: String,
        val arguments: HashMap<String, Serializable?>? = null
    ) : NavigationEvent()

    internal class IntentNavigationEvent(var intent: Intent) : NavigationEvent() {

        @Throws(Exception::class)
        private fun writeObject(oos: ObjectOutputStream) {
            val parcel = Parcel.obtain()
            intent.writeToParcel(parcel, 0)
            oos.write(parcel.marshall())
            parcel.recycle()
        }

        @Throws(Exception::class)
        private fun readObject(ois: ObjectInputStream) {
            val bytes = ois.readBytes()
            val parcel = Parcel.obtain()
            parcel.unmarshall(bytes, 0, bytes.size)
            intent = Intent.CREATOR.createFromParcel(parcel)
            parcel.recycle()
        }

    }

    internal object BackNavigationEvent : NavigationEvent()

    internal class ResultNavigationEvent(val key: String, val result: Serializable?) :
        NavigationEvent()

    internal class ResultNavigationEvent2(val result: Result) : NavigationEvent()
}