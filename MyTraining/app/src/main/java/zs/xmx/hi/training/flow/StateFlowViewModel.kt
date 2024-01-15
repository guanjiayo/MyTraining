package zs.xmx.hi.training.flow

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


/**
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:   StateFlow 测试
 *
 */
class StateFlowViewModel : ViewModel() {

    /*
        stateFlow 与 LiveData 区别
        1. stateFlow 需要默认值,LiveData 不需要
        2. 当宿主View进入 STOPPED 状态时,LiveData.observe() 会自动取消注册,而从 StateFlow 或者其他数据流手机数据的操作并不会自动停止
        如需实现相同效果,需要从 Lifecycle.repeatOnLifecycle 块collect收集
        3. Flow可以在任何线程collect数据,LiveData只能从UI线程订阅
        4. stateFlow防抖,LiveData不防抖,意思就是 stateFlow如果发送多次相同的数据,只会回调一次(内部有相同数据条件判断)
        相同点:
        1. 值唯一,都会把最新值传给订阅者(粘性)
        2. 可被多个订阅者订阅
        3. 子线程中并发发送数据可能丢失数据
     */

    //由于有默认值,页面一显示就把数据发送出去了
    private val _uiState = MutableStateFlow("默认值")

    val uiState: StateFlow<String> = _uiState
    //第二种写法
    //val uiState= _uiState.asStateFlow()

    fun changeData(data: String) {
        _uiState.value = data//发送数据
    }

    private val _batchData = MutableStateFlow(0)
    val batchDataState: StateFlow<Int> = _batchData
    fun batchData() {
        viewModelScope.launch {
            for (i in 0..200) {
                delay(100)
                // _batchData.emit(i)//看源码两者其实是一样的
                _batchData.value = i
            }
        }
    }



}