package zs.xmx.hi.training.flow.operator

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


/**
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:
 *
 */
class FlowOperatorViewModel : ViewModel() {

    @FlowPreview
    fun flatMapConcat() = viewModelScope.launch {
        flow {
            delay(1000)
            emit(1)
            delay(1000)
            emit(2)
            delay(1000)
            emit(3)
            delay(1000)
            emit(4)
        }.flatMapConcat {
            flow {
                emit("$it 产生第一个flow值")
                delay(2500)
                emit("$it 产生第二个flow值")
            }
        }.collect { value ->
            Log.i(TAG, value)
        }
    }

    /**
     * Flow 嵌套 (应该不建议这么搞)
     */
    fun flowNested() = viewModelScope.launch {
        flow {
            emit(1000)
        }.collect {
            flow {
                emit("第一个Flow 发射的值  $it")
            }.collect {
                Log.i(TAG, "第二个Flow 发射的值--> $it")
            }
        }

    }

    /**
     * Flow 结合map 嵌套
     *
     * map 块再实现 Flow,其实就把 Flow<T> 变成了 Flow<Flow<T>>
     */
    fun flowMapNested() = viewModelScope.launch {
        flow {
            emit(5)
        }.map {
            flow {
                emit("$it")
            }
        }.collect {
            Log.i(TAG, "flowMapNested() --> $it.")
        }
    }

    /**
     *  flattenConcat()
     *
     * 承接flowMapNested()  ,将Flow<Flow<T>> 拼接起来返回 T
     *
     * flattenConcat 是按顺序拼接的,如果使用 flattenMerge 是并发的
     */
    fun flattenConcat() = viewModelScope.launch {
        flow {
            //模拟网络请求一
            delay(1000)
            emit(5)
        }.map {
            flow {
                //模拟网络请求二
                delay(2000)
                emit("$it")
            }
        }.flattenConcat()
            .collect {
                Log.i(TAG, "flattenConcat() --> $it.")
            }
    }

    /**
    Flow 中无法随意切换调度器,因为emit函数不是线程安全的
    想要生成元素时切换调度器,需要使用 channelFlow 创建 Flow
     */
    @ExperimentalCoroutinesApi
    fun channelFlow() = viewModelScope.launch {
        channelFlow {
            send(1)
            Log.i(TAG, "send1  ${Thread.currentThread().name}")
            withContext(Dispatchers.IO) {
                delay(500)
                send(2)
                Log.i(TAG, "send2   ${Thread.currentThread().name}")
            }
        }.collect {
            Log.i(TAG, "channelFlow  $it --- ${Thread.currentThread().name}")
        }

    }

    /**
     * Flow 异常处理
     *
     * 1. 如果只写 onCompletion() 不写 catch() ,程序会抛异常
     * 2. catch() 不能放在 onCompletion() 后面, 程序会抛异常
     * 3. catch() 只会捕获上游异常
     * 4. flow{}catch{}onCompletion{} 关系类似  try{}catch{}finally{},
     * 5. 不建议在Flow内部使用 try{}catch{}finally{}
     *
     */
    fun catch() = viewModelScope.launch {
        flow {
            emit(1)
            throw RuntimeException("出异常了!")
        }.catch { t: Throwable ->
            Log.i(TAG, "caught error: $t")
        }.onCompletion { t: Throwable? ->
            if (t != null) {
                Log.i(TAG, "finally.  ${t.printStackTrace()}")
            } else {
                Log.i(TAG, "finally.  completion")
            }
        }.collect {
            Log.i(TAG, "catch()  发射的数据:  $it")
        }
    }

    /*  onEach() 在笔记记录下,不演示了
        用于分离 Flow 的消费和触发
        fun createFlow() = flow<Int> {
    (1..3).forEach {
      emit(it)
      delay(100)
    }
  }.onEach { println(it) }

fun main(){
  GlobalScope.launch {
    createFlow().collect()
  }
}

     */



    companion object {
        val TAG = "FlowOperator"
    }



}