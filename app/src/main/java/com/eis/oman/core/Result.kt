package com.eis.oman.core

sealed interface EisResult<out T> {
    data class Success<T>(val data: T) : EisResult<T>
    data class Failure(val error: Throwable) : EisResult<Nothing>
    data object Loading : EisResult<Nothing>
}

inline fun <T, R> EisResult<T>.map(transform: (T) -> R): EisResult<R> = when (this) {
    is EisResult.Success -> EisResult.Success(transform(data))
    is EisResult.Failure -> this
    EisResult.Loading -> EisResult.Loading
}

inline fun <T> EisResult<T>.onSuccess(action: (T) -> Unit): EisResult<T> {
    if (this is EisResult.Success) action(data)
    return this
}

inline fun <T> EisResult<T>.onFailure(action: (Throwable) -> Unit): EisResult<T> {
    if (this is EisResult.Failure) action(error)
    return this
}
