package zs.xmx.hi.training.flow.network

import androidx.lifecycle.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


/**
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:   模拟实际开发网络请求
 *
 */
class NetRequestViewModel : ViewModel() {

    private val _failure = MutableLiveData<String>()
    val failure: LiveData<String> = _failure

    //------------Flow 和 LiveData结合使用写法(不用热流的推荐写法)----------------------------------------

    // 私有的 MutableLiveData 可变的，对内访问
    private val _livedataObs = MutableLiveData<List<String>>()

    // 对外暴露不可变的 LiveData，只能查询
    val livedataObs: LiveData<List<String>> = _livedataObs

    fun getDogImages1(isSuccess: Boolean) = viewModelScope.launch {
        NetRequestRepository.getDogImages(isSuccess)
            .onStart {
                //flow 调用数据之前回调,
                // 可用作 正在加载数据... Loading视图
            }.catch {
                //捕获上游出现的异常,这里指请求框架
            }
            .onCompletion {
                //请求完成
                //可用作 正在加载数据... Loading视图关闭
            }
            .collectLatest { result ->
                //在一段时间内发送多次数据，只会接受最新的一次发射过来的数据
                result.doFailure { throwable ->
                    _failure.value = throwable?.message ?: "failure"
                }
                result.doSuccess { value ->
                    //这个相当于 LiveData.setValue()
                    _livedataObs.postValue(value)
                }
            }
    }

    //-----------------------Flow 直接转 LiveData写法----------------------------------------

    private val _livedataExtObs = MutableLiveData<Boolean>()

    val livedataExtObs = Transformations.switchMap(_livedataExtObs) {
        getDogImagesLiveData(it)
    }

    fun getDogImages2(isSuccess: Boolean) {
        _livedataExtObs.value = isSuccess
    }

    //这种写法就直接监听方法了,如果写在点击事件里面,有多次监听的问题(不建议)
    //mViewModel.getDogImages1(true).observe(this, Observer {   })
    //也可以写成 NetRequestRepository.getDogImages(isSuccess).onCompletion{}.asLiveData()  一样效果
    private fun getDogImagesLiveData(isSuccess: Boolean) = liveData {
        NetRequestRepository.getDogImages(isSuccess)
            .onStart {
                //flow 调用数据之前回调,
                // 可用作 正在加载数据... Loading视图
            }.catch {
                //捕获上游出现的异常,这里指请求框架
            }
            .onCompletion {
                //请求完成
                //可用作 正在加载数据... Loading视图关闭
            }
            .collectLatest { result ->
                //在一段时间内发送多次数据，只会接受最新的一次发射过来的数据
                result.doFailure { throwable ->
                    _failure.value = throwable?.message ?: "failure"
                }
                result.doSuccess {
                    //这个相当于 LiveData.setValue()
                    emit(it)
                }
            }

    }

    //-----------------------StateFlow 仿LiveData 写法(使用StateFlow 的推荐写法,但不建议网络请求用,有默认值)----------------------------------------

    //把数据的处理也交给仓库层了,
    private val _stateFlowObs = MutableStateFlow<List<String>>(emptyList())

    val stateFlowObs: StateFlow<List<String>> = _stateFlowObs

    fun getDogImages3(isSuccess: Boolean) = viewModelScope.launch {

        //NetRequestRepository.getDogFire(isSuccess)
        NetRequestRepository.getDogImages(isSuccess)
            .onStart {
                //flow 调用数据之前回调,
                // 可用作 正在加载数据... Loading视图
            }.catch {
                //捕获上游出现的异常,这里指请求框架
            }
            .onCompletion {
                //请求完成
                //可用作 正在加载数据... Loading视图关闭
            }
            .collectLatest { result ->
                //在一段时间内发送多次数据，只会接受最新的一次发射过来的数据
                result.doFailure { throwable ->
                    _failure.value = throwable?.message ?: "failure"
                }
                result.doSuccess { value ->
                    //这个相当于 LiveData.setValue()
                    _stateFlowObs.value = value
                }

            }
    }

    //-----------------------StateFlow 仿LiveData 写法(直接把密封类发送出去,让宿主自己处理)----------------------------------------

    private val _uiState =
        MutableStateFlow<ResultWrapper2>(ResultWrapper2.Success<List<String>>(emptyList()))

    val uiState: StateFlow<ResultWrapper2> = _uiState

    fun getDogImages4(isSuccess: Boolean) = viewModelScope.launch {
        NetRequestRepository.getDogImages2(isSuccess)
            .onStart {
                //flow 调用数据之前回调,
                // 可用作 正在加载数据... Loading视图
            }.catch {
                //捕获上游出现的异常,这里指请求框架
            }
            .onCompletion {
                //请求完成
                //可用作 正在加载数据... Loading视图关闭
            }
            .collectLatest { result ->
                _uiState.value = result
            }

    }


}