package zs.xmx.hi.training.flow.network


/**
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:   相当于 Kotlin自带的 Result,ViewModel
 *
 */
sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()

    data class Failure(val throwable: Throwable?) : ResultWrapper<Nothing>()
}

inline fun <reified T> ResultWrapper<T>.doSuccess(success: (T) -> Unit) {
    if (this is ResultWrapper.Success) {
        success(value)
    }
}

inline fun <reified T> ResultWrapper<T>.doFailure(failure: (Throwable?) -> Unit) {
    if (this is ResultWrapper.Failure) {
        failure(throwable)
    }
}
