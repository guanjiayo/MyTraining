package zs.xmx.hi.training.flow

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import zs.xmx.hi.training.flow.operator.FlowOperatorViewModel


/**
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:   SharedFlow 测试
 *
 */
class SharedFlowViewModel : ViewModel() {

    private val TAG = "SharedFlowViewModel"

    /*
        StateFlow 与 SharedFlow 区别
        1. SharedFlow 不需要初始值,StateFlow必须传初始值
        2. SharedFlow 可以保留历史数据,新订阅者可以获取到之前发射过的一系列数据,StateFlow只保留最新数据
        3. SharedFlow 可以传入一个 replay 参数，它表示可以对新订阅者重新发送 replay 个历史数据，默认值为 0, 即非粘性
        4. StateFlow 可以看成是一个 replay = 1 且没有缓冲区的 SharedFlow
        5. SharedFlow 在子线程中多次 emit() 不会丢失数据
     */

    /*
       当 MutableSharedFlow 中缓存数据量超过阈值时，emit 方法和 tryEmit 方法的处理方式会有不同：
          -  emit 方法：当缓存策略为 BufferOverflow.SUSPEND 时，emit 方法会挂起，直到有新的缓存空间。
          -  tryEmit 方法：tryEmit 会返回一个 Boolean 值，true 代表传递成功，false 代表会产生一个回调，让这次数据发射挂起，直到有新的缓存空间。
        emit 是挂起函数,数据处理完毕,保证数据完整和顺序才下发
        tryEmit 不是挂起函数,发送完就返回布尔值,发射的数据,订阅者没及时处理可能丢失
     */


    //默认参数 replay=0,extraBufferCapacity=0,BufferOverflow.SUSPEND
    //当 replay为0时,新的订阅者获取不到之前的数据,因此不存在粘性事件问题
    val sharedFlow = MutableSharedFlow<Int>(
        5 // 参数一：回放的数据数量,当新的订阅者Collect时，发送几个已经发送过的数据给它,默认为零,不能为负数
        , 3 // 参数二：额外的缓冲容量。不能为负数，默认为零。这个值加上replay的总和，构成了SharedFlow的总缓冲区大小
        , BufferOverflow.DROP_OLDEST // 参数三：缓存策略，达到缓冲区溢出时采取的行为,  三种 丢掉最新值、丢掉最旧值和挂起
    )

    // 初始化时调用
    init {
        for (i in 0..10) {
            sharedFlow.tryEmit(i)
        }
    }

    // 在按钮中调用
    fun doAsClick() {
        for (i in 11..20) {
            Log.d(TAG, "---- shareFlow emit: $i ")
            sharedFlow.tryEmit(i)
            //可知replayCache 缓存的数据
            Log.d(TAG, "---- shareFlow replayCache: ${sharedFlow.replayCache.joinToString()}")
        }
    }

}