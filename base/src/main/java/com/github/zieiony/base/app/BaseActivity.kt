package com.github.zieiony.base.app

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import java.io.Serializable


abstract class BaseActivity : AppCompatActivity(), Navigator {

    var icon: Drawable? = null

    private var _result: Serializable? = null

    private var coldStart = true

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = NavigatorFragmentFactory(this)

        _result = savedInstanceState?.getSerializable(FRAGMENT_RESULT)

        super.onCreate(savedInstanceState)

        javaClass.getAnnotation(ScreenAnnotation::class.java)?.let {
            if (it.layoutId != 0)
                setContentView(it.layoutId)
            if (it.titleId != 0)
                title = resources.getString(it.titleId)
            if (it.iconId != 0)
                icon = resources.getDrawable(it.iconId)
        }
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

    override fun onNavigateTo(fragment: Fragment): Boolean {
        if (fragment is DialogFragment) {
            fragment.show(supportFragmentManager, DIALOG_TAG)
            return true
        }
        return false
    }

    override fun onNavigateTo(intent: Intent): Boolean {
        startActivity(intent)
        return true
    }

    override fun onNavigateBack(): Boolean {
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
    }
}
