package com.github.zieiony.base.app

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.github.zieiony.base.navigation.Navigator
import java.io.Serializable


abstract class BaseActivity : AppCompatActivity(), Navigator {

    open val layoutId: Int = INVALID_ID
    open val titleId: Int = INVALID_ID
    open val iconId: Int = INVALID_ID

    var icon: Drawable? = null

    private val _results = java.util.HashMap<String, Serializable?>()

    private var coldStart = true

    override fun onCreate(savedInstanceState: Bundle?) {
        savedInstanceState?.getSerializable(FRAGMENT_RESULT)?.let {
            _results.putAll(it as HashMap<String, Serializable?>)
        }

        super.onCreate(savedInstanceState)

        if (layoutId != INVALID_ID)
            setContentView(layoutId)
        if (titleId != INVALID_ID)
            title = resources.getString(titleId)
        if (iconId != INVALID_ID)
            icon = resources.getDrawable(iconId)
    }

    open fun onColdStart() {
    }

    override fun onStart() {
        super.onStart()
        if (coldStart) {
            onColdStart()
            coldStart = false
        }
    }

    override fun onResume() {
        super.onResume()
        for (result in results.entries) {
            if (onResult(result.key, result.value))
                clearResult(result.key)
        }
    }

    override fun navigateTo(
        fragmentClass: Class<out Fragment?>,
        arguments: java.util.HashMap<String, Serializable?>?
    ) {
        if (!onNavigateTo(fragmentClass, arguments))
            parentNavigator?.navigateTo(fragmentClass, arguments)
    }

    override fun navigateTo(fragment: Fragment) {
        if (!onNavigateTo(fragment))
            parentNavigator?.navigateTo(fragment)
    }

    override fun navigateTo(intent: Intent) {
        if (!onNavigateTo(intent))
            parentNavigator?.navigateTo(intent)
    }

    open fun onNavigateTo(
        fragmentClass: Class<out Fragment>,
        arguments: java.util.HashMap<String, Serializable?>?
    ): Boolean {
        val fragment = supportFragmentManager.fragmentFactory.instantiate(
            fragmentClass.classLoader!!,
            fragmentClass.name
        )
        arguments?.let {
            val bundle = Bundle()
            fragment.arguments = bundle
            it.forEach { entry ->
                bundle.putSerializable(entry.key, entry.value)
            }
        }
        return onNavigateTo(fragment)
    }

    open fun onNavigateTo(fragment: Fragment): Boolean {
        if (fragment is DialogFragment) {
            fragment.show(supportFragmentManager, DIALOG_TAG)
            return true
        }
        return false
    }

    open fun onNavigateTo(intent: Intent): Boolean {
        startActivity(intent)
        return true
    }

    override fun navigateBack() {
        onNavigateBack()
    }

    open fun onNavigateBack(): Boolean {
        onBackPressed()
        return true
    }

    override fun getResults(): java.util.HashMap<String, Serializable?> {
        return _results
    }

    override fun <T : Serializable?> getResult(key: String): T {
        return _results[key] as T
    }

    final override fun <T : Serializable?> setResult(key: String, result: T) {
        if (onResult(key, result)) {
            clearResult(key)
        } else {
            results[key] = result
        }
    }

    override fun clearResult(key: String) {
        _results.remove(key)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(FRAGMENT_RESULT, _results)
    }

    companion object {
        private const val FRAGMENT_RESULT = "fragmentResult"
        private const val DIALOG_TAG = "dialog"
        private const val INVALID_ID = 0
    }
}
