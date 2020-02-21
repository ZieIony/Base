package com.github.zieiony.base.arch

import java.io.Serializable

class NavigationEvent(
    val className: String? = null,
    val arguments: HashMap<String, Serializable>? = null,
    val result: Serializable? = null
) : Serializable
