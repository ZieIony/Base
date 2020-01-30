package com.github.zieiony.base.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import java.io.Serializable


abstract class BaseActivity : AppCompatActivity(), Navigator {

    private var _result: Serializable? = null
    override fun <T : Serializable?> getResult(): T? {
        return _result as T?
    }

    private var coldStart = true

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = NavigatorFragmentFactory(this)

        _result = savedInstanceState?.getSerializable(FRAGMENT_RESULT)

        super.onCreate(savedInstanceState)

        javaClass.getAnnotation(ScreenAnnotation::class.java)?.let {
            if (it.layout != 0)
                setContentView(it.layout)
            if (it.title != 0)
                title = title
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
        _result = null
        onBackPressed()
        return true
    }

    override fun <T : Serializable> onNavigateBack(result: T?): Boolean {
        _result = result
        onBackPressed()
        return true
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
