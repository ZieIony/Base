package com.github.zieiony.base.app

import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes

annotation class ScreenAnnotation(
    @LayoutRes val layoutId: Int = 0,
    @StringRes val titleId: Int = 0,
    @DrawableRes val iconId: Int = 0
)
