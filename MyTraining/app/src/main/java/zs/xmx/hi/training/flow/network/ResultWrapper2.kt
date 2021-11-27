package zs.xmx.hi.training.flow.network


/**
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:   相当于 Kotlin自带的 Result,ViewModel
 *
 */
sealed class ResultWrapper2 {
    data class Success<T>(val value: T) : ResultWrapper2()
    data class Failure(val throwable: Throwable?) : ResultWrapper2()
}

