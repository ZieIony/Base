package com.github.zieiony.base.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.io.Serializable


abstract class BaseActivity : AppCompatActivity(),
    Navigator {
    private var _result: Serializable? = null
    override fun <T : Serializable?> getResult(): T? {
        return _result as T?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory =
            NavigatorFragmentFactory(this)

        _result = savedInstanceState?.getSerializable(FRAGMENT_RESULT)

        super.onCreate(savedInstanceState)

        javaClass.getAnnotation(ScreenAnnotation::class.java)?.let {
            if (it.layout != 0)
                setContentView(it.layout)
            if (it.title != 0)
                title = title
        }
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
        const val FRAGMENT_RESULT = "fragmentResult"
    }
}