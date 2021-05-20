package com.more.sandboxapp.models

sealed class State<out T> {
    object  Loading : State<Nothing>()
    object  Error : State<Nothing>()
    data class Content<out T>(val data: T): State<T>()
}