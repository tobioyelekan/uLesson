package com.example.ulesson.data.helper

import java.lang.Exception

sealed class Resource<out T : Any> {
    data class Success<out T : Any>(val data: T?) : Resource<T>()
    data class Error(val message: String, val cause: Exception? = null) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}