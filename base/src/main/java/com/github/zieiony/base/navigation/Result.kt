package com.github.zieiony.base.navigation

import java.io.Serializable


data class Result(val target: Int, val key: String, val value: Serializable?) : Serializable