package com.github.zieiony.base.navigation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import java.io.Serializable


internal class DeferredNavigator : Navigator {
    private var _navigator: Navigator? = null
    var navigator: Navigator?
        get() = _navigator
        set(value) {
            _navigator = value
            _navigator?.let { navigator ->
                events.forEach { event ->
                    when (event) {
                        is NavigationEvent.ResultNavigationEvent -> navigator.setResult(
                            event.key,
                            event.result
                        )
                        is NavigationEvent.ResultNavigationEvent2 -> navigator.setResult(
                            event.result
                        )
                        is NavigationEvent.BackNavigationEvent -> navigator.navigateBack()
                        is NavigationEvent.FragmentNavigationEvent -> {
                            navigator.navigateTo(
                                Class.forName(event.className) as Class<out Fragment>,
                                event.arguments
                            )
                        }
                        is NavigationEvent.IntentNavigationEvent -> navigator.navigateTo(event.intent)
                    }
                    events.remove(event)
                }
            }
        }

    private var events = ArrayList<NavigationEvent>()

    override fun getNavigatorId(): Int {
        throw RuntimeException("Not supported")
    }

    override fun navigateTo(
        target: Class<out Fragment>,
        arguments: HashMap<String, Serializable?>?
    ) {
        val localNavigator = navigator
        if (localNavigator == null) {
            events.add(
                NavigationEvent.FragmentNavigationEvent(target.name, arguments)
            )
        } else {
            localNavigator.navigateTo(target, arguments)
        }
    }

    override fun navigateTo(fragment: Fragment) {
        val localNavigator = navigator
        if (localNavigator == null) {
            val bundle = fragment.arguments
            events.add(
                NavigationEvent.FragmentNavigationEvent(
                    fragment.javaClass.name, if (bundle == null) {
                        null
                    } else {
                        val arguments = HashMap<String, Serializable?>()
                        bundle.keySet().forEach {
                            arguments[it] = bundle[it] as Serializable?
                        }
                        arguments
                    }
                )
            )
        } else {
            localNavigator.navigateTo(fragment)
        }
    }

    override fun navigateTo(originalNavigator: Navigator, fragment: Fragment) {
        throw RuntimeException("Not supported")
    }

    override fun navigateTo(intent: Intent) {
        val localNavigator = navigator
        if (localNavigator == null) {
            events.add(NavigationEvent.IntentNavigationEvent(intent))
        } else {
            localNavigator.navigateTo(intent)
        }
    }

    override fun navigateBack() {
        val localNavigator = navigator
        if (localNavigator == null) {
            events.add(NavigationEvent.BackNavigationEvent)
        } else {
            localNavigator.navigateBack()
        }
    }

    override fun setResult(key: String, result: Serializable?) {
        val localNavigator = navigator
        if (localNavigator == null) {
            events.add(NavigationEvent.ResultNavigationEvent(key, result))
        } else {
            localNavigator.setResult(key, result)
        }
    }

    override fun setResult(result: Result) {
        val localNavigator = navigator
        if (localNavigator == null) {
            events.add(NavigationEvent.ResultNavigationEvent2(result))
        } else {
            localNavigator.setResult(result)
        }
    }

    override fun setResultTarget(resultTarget: Int) {
        throw RuntimeException("Not supported")
    }

    override fun getResultTarget(): Int {
        throw RuntimeException("Not supported")
    }

    fun saveState(bundle: Bundle) {
        bundle.putSerializable(EVENTS, events)
    }

    fun restoreState(bundle: Bundle) {
        bundle.getSerializable(EVENTS)?.let {
            events = it as ArrayList<NavigationEvent>
        }
    }

    companion object {
        const val EVENTS = "events"
    }
}