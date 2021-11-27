package zs.xmx.hi.training.flow.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


/**
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:   模拟仓库层请求数据
 *
 */
object NetRequestRepository {
    /*
       模拟网络请求成功和失败
       实际开发应该放到对应的请求成功和失败的回调块中
    */
    suspend fun getDogImages(isSuccess: Boolean): Flow<ResultWrapper<List<String>>> {
        return flow {
            if (isSuccess) {
                emit(ResultWrapper.Success(getList()))
            } else {
                emit(ResultWrapper.Failure(RuntimeException("网络请求有误,请稍后重试!")))
            }
        }.flowOn(Dispatchers.IO)
    }

    /*
       让宿主自行判断状态写法
     */
    suspend fun getDogImages2(isSuccess: Boolean): Flow<ResultWrapper2> {
        return flow {
            if (isSuccess) {
                emit(ResultWrapper2.Success(getList()))
            } else {
                emit(ResultWrapper2.Failure(RuntimeException("网络请求有误,请稍后重试!")))
            }
        }.flowOn(Dispatchers.IO)
    }

    private fun getList(): List<String> {
        return listOf(
            "https://images.dog.ceo/breeds/retriever-curly/n02099429_935.jpg",
            "https://images.dog.ceo/breeds/terrier-yorkshire/n02094433_3010.jpg",
            "https://images.dog.ceo/breeds/hound-afghan/n02088094_7260.jpg",
            "https://images.dog.ceo/breeds/retriever-curly/n02099429_935.jpg",
            "https://images.dog.ceo/breeds/terrier-yorkshire/n02094433_3010.jpg",
            "https://images.dog.ceo/breeds/hound-afghan/n02088094_7260.jpg",
            "https://images.dog.ceo/breeds/pekinese/n02086079_952.jpg"
        )
    }

}