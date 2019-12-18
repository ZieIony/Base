package com.github.zieiony.base.util

interface Logger {
    fun log(text: String)

    fun log(exception: Exception)
}
