package zs.xmx.hi.training.flow

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow


/**
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:   SharedFlow 测试
 *
 */
class SharedFlowViewModel : ViewModel() {

    /*
       当 MutableSharedFlow 中缓存数据量超过阈值时，emit 方法和 tryEmit 方法的处理方式会有不同：
          -  emit 方法：当缓存策略为 BufferOverflow.SUSPEND 时，emit 方法会挂起，直到有新的缓存空间。
          -  tryEmit 方法：tryEmit 会返回一个 Boolean 值，true 代表传递成功，false 代表会产生一个回调，让这次数据发射挂起，直到有新的缓存空间。

     */


    //默认参数 replay=0,extraBufferCapacity=0,BufferOverflow.SUSPEND
    //当 replay为0时,新的订阅者获取不到之前的数据,因此不存在粘性事件问题
    val sharedFlow = MutableSharedFlow<Int>(
        5 // 参数一：当新的订阅者Collect时，发送几个已经发送过的数据给它,默认为零,不能为负数
        , 3 // 参数二：缓冲的值的数量。不能为负数，默认为零。这个值加上replay的总和，构成了SharedFlow的总缓冲区大小
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
            sharedFlow.tryEmit(i)
        }
    }


}