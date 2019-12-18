package com.github.zieiony.base.app

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory


class NavigatorFragmentFactory(val navigator: Navigator) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        val fragmentClass = loadFragmentClass(classLoader, className)
        try {
            return fragmentClass.getConstructor(Navigator::class.java)
                .newInstance(navigator)
        } catch (e: Exception) {
            return fragmentClass.newInstance()
        }
    }
}
