package br.com.stockmarcketapp.util

import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?) : Resource<T>(data = data)
    class Error<T>(message: String?) : Resource<T>(message = message)
}

inline fun <T : Any> Resource<T>.onSuccess(action: (T?) -> Unit): Resource<T> {
    if (this is Resource.Success) {
        action(this.data)
    }
    return this
}

inline fun <T : Any> Resource<T>.onError(action: (message: String?) -> Unit): Resource<T> {
    if (this is Resource.Error) {
        action(this.message)
    }
    return this
}

suspend fun <T> safeApiCall(apiCall: suspend () -> T): Flow<Resource<T>> {
    return flow {
        try {
            emit(Resource.Success(apiCall.invoke()))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't load data"))
        } catch (e: HttpException) {
            emit(Resource.Error("Couldn't read data"))
        }
    }
}