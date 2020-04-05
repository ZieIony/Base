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

    private var _result: Serializable? = null

    private var coldStart = true

    override fun onCreate(savedInstanceState: Bundle?) {
        _result = savedInstanceState?.getSerializable(FRAGMENT_RESULT)

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
        val result = getResult<Serializable>()
        if (result != null)
            if (onResult(result))
                _result = null
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

    final override fun <T : Serializable?> getResult(): T? {
        return _result as T?
    }

    final override fun <T : Serializable?> setResult(result: T) {
        if (result == null || !onResult(result))
            _result = result
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(FRAGMENT_RESULT, _result)
    }

    companion object {
        private const val FRAGMENT_RESULT = "fragmentResult"
        private const val DIALOG_TAG = "dialog"
        private const val INVALID_ID = 0
    }
}
