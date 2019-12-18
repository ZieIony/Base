package com.github.zieiony.base.app

import androidx.annotation.LayoutRes
import androidx.annotation.StringRes

annotation class ScreenAnnotation(@LayoutRes val layout: Int = 0, @StringRes val title: Int = 0)
