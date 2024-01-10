package zs.xmx.hi.training.flow.operator

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.runningReduce
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.withIndex
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import zs.xmx.hi.training.model.Person


/**
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:
 *
 */
class FlowOperatorViewModel : ViewModel() {

    //-------------------------Flow 变换操作符---------------------------------------------

    /**
     * map 变换操作符,转换数据类型
     */
    fun mapOnFlow() = viewModelScope.launch {
        flow {
            emit(100)
        }.map {
            "变成String $it"
        }.collect {
            Log.i(TAG, it)
        }
    }

    /**
     * transform 变换操作符,转换数据类型
     *  区别map,接收者是FlowCollector,可以多次发送
     */
    fun transformOnMap() = viewModelScope.launch {
        val flow = flowOf("Apple", "Banana", "Cherry", "Durian", "Elderberry")

        val transformedFlow = flow.transform { value ->
            if (value.length <= 5) {
                emit(value.length) // 小于等于5的字符串的长度
            }
        }
        val totalLength = transformedFlow.reduce { accumulator, value ->
            accumulator + value // 统计长度的总和
        }

        Log.i(TAG, "Total length: $totalLength")
    }

    /**
     * withIndex 变换操作符,给上游数据添加下标
     *  区别 collectIndexed, 前者在中游处理,后者在下游处理
     */
    fun withIndexOnMap() = viewModelScope.launch {
        flow {
            emit("a")
            emit("b")
        }.withIndex()
            .collect {
                Log.i(TAG, "withIndex: ${it.index}  :  ${it.value}")
            }
    }

    /**
     * scan 变换操作符
     * 把每一步操作的结果,和发送的值返回(需要穿初始值)
     * 与 fold 相似,但scan操作中游,fold操作下游
     */
    fun scanOnFlow() = viewModelScope.launch {
        flowOf(1, 2, 3).scan(0) { acc, value ->
            //acc 是上一步操作的结果， value 是发射的值
            Log.i(TAG, "acc: $acc  ,  value: $value")
            acc + value
        }.collect {
            Log.i(TAG, "collect: $it")
        }
    }

    /**
     * runningFold 变换操作符
     * 把每一步操作的结果,和发送的值返回(需要穿初始值)
     * 与 scan 相似,只是返回的是flow
     */
    fun runningFoldOnFlow() = viewModelScope.launch {
        flowOf(1, 2, 3).runningFold(0) { acc, value ->
            //acc 是上一步操作的结果， value 是发射的值
            Log.i(TAG, "acc: $acc  ,  value: $value")
            acc + value
        }.collect {
            Log.i(TAG, "collect: $it")
        }
    }

    /**
     * runningReduce 变换操作符
     * 把每一步操作的结果,和发送的值返回(不需要穿初始值)
     * 与 runningFold 相似,只是不需要返回值
     */
    fun runningReduceOnFlow() = viewModelScope.launch {
        flowOf(1, 2, 3).runningReduce { acc, value ->
            //acc 是上一步操作的结果， value 是发射的值
            Log.i(TAG, "acc: $acc  ,  value: $value")
            acc + value
        }.collect {
            Log.i(TAG, "collect: $it")
        }
    }

    //-----------------------Flow 过滤操作符-----------------------------------------------

    /**
     * filter 过滤操作符
     * 筛选符合条件的值
     */
    fun filterOnFlow() = viewModelScope.launch {
        flowOf(1, 2, 3, 4, 5, 6)
            .filter { it > 3 }
            .collect {
                Log.i(TAG, "collect: $it")
            }
    }

    /**
     * filter 过滤操作符
     * 筛选不符合条件的值
     */
    fun filterNotOnFlow() = viewModelScope.launch {
        flowOf(1, 2, 3, 4, 5, 6)
            .filterNot { it > 3 }
            .collect {
                Log.i(TAG, "collect: $it")
            }
    }

    /**
     * filter 过滤操作符
     * 筛选对应类型的值
     */
    fun filterIsInstanceOnFlow() = viewModelScope.launch {
        flowOf(1, 2, 3, 4, 5, 6, "啊", "b", "c")
            .filterIsInstance<String>()
            .collect {
                Log.i(TAG, "collect: $it")
            }
    }

    /**
     * filter 过滤操作符
     * 筛选不为null的值
     */
    fun filterNotNullOnFlow() = viewModelScope.launch {
        flow {
            emit("a")
            emit(null)
            emit("b")
        }
            .filterNotNull()
            .collect {
                Log.i(TAG, "collect: $it")
            }
    }


    /**
     * drop 过滤操作符
     * 丢弃前 n 个的值
     */
    fun dropOnFlow() = viewModelScope.launch {
        flow {
            emit(1)
            emit(2)
            emit(3)
            emit(4)
            emit(5)
        }.drop(4)
            .collect {
                Log.i(TAG, "collect: $it")
            }
    }

    /**
     * dropWhile 过滤操作符
     * 找到第一个不满足条件的,返回其和其之后的值
     */
    fun dropWhileOnFlow() = viewModelScope.launch {
        flow {
            emit(3)
            emit(3)
            emit(1)
            emit(5)
            emit(4)
        }.dropWhile { it == 3 }
            .collect {
                Log.i(TAG, "collect: $it")
            }
    }

    /**
     * take 过滤操作符
     * 返回前 n 个元素
     */
    fun takeOnFlow() = viewModelScope.launch {
        flow {
            emit(1)
            emit(2)
            emit(3)
        }.take(2).collect {
            Log.i(TAG, "collect: $it")
        }
    }

    /**
     * takeWhile 过滤操作符
     * 找到第一个不满足条件的项,但取之前的值
     */
    fun takeWhileOnFlow() = viewModelScope.launch {
        flow {
            emit(5)
            emit(1)
            emit(2)
            emit(3)
            emit(4)
            emit(5)
        }.takeWhile { it < 3 }.collect {
            Log.i(TAG, "collect: $it")
        }
    }

    /**
     * debounce 过滤操作符
     * 防止抖动,一定时间内只接受最新的一个,其他过滤掉(一般用在搜索联想)
     */
    @OptIn(FlowPreview::class)
    fun debounceOnFlow() = viewModelScope.launch {
        flow {
            emit(1)
            delay(90)
            emit(2)
            delay(90)
            emit(3)
            delay(1010)
            emit(4)
            delay(1010)
            emit(5)
        }.debounce(1000)
            .collect {
                Log.i(TAG, "collect: $it")
            }
    }

    /**
     * debounce 过滤操作符
     * 防止抖动,一定时间内只接受最新的一个,其他过滤掉(一般用在搜索联想)
     * 这里演示动态设置时间
     */
    @OptIn(FlowPreview::class)
    fun debounce2OnFlow() = viewModelScope.launch {
        flow {
            emit(1)
            delay(90)
            emit(2)
            delay(90)
            emit(3)
            delay(1010)
            emit(4)
            delay(1010)
            emit(5)
        }.debounce {
            if (it == 1) {
                0L
            } else {
                1000L
            }
        }
            .collect {
                Log.i(TAG, "collect: $it")
            }
    }


    /**
     * sample 过滤操作符
     * 给定一个时间周期,仅获取周期内最新发出的值
     */
    @OptIn(FlowPreview::class)
    fun sampleOnFlow() = viewModelScope.launch {
        flow {
            repeat(10) {
                emit(it)
                delay(110)
            }
        }.sample(200)
    }

    /**
     * distinctUntilChangedBy 过滤操作符
     * 判断连续的两个值是否重复,重复的去重
     */
    fun distinctUntilChangedByOnFlow() = viewModelScope.launch {
        flowOf(
            Person("张三", 18),
            Person("李四", 18),
            Person("王五", 20)
        ).distinctUntilChangedBy {
            it.age
        }.collect {
            Log.i(TAG, "collect: $it")
        }
    }

    /**
     * distinctUntilChanged 过滤操作符
     * 判断连续的两个值是否重复,重复的去重
     * distinctUntilChangedByOnFlow的简化版
     */
    fun distinctUntilChangedOnFlow() = viewModelScope.launch {
        flowOf(
            1, 1, 2, 2, 3, 5, 7
        ).distinctUntilChanged()
            .collect {
                Log.i(TAG, "collect: $it")
            }
    }

    //-----------------------Flow 组合操作符-----------------------------------------------

    /**
     * combine 组合操作符
     * 组合每个流最新发出的值(受时间影响)
     */
    fun combineOnFlow() = viewModelScope.launch {
        val flow = flowOf(1, 2).onEach { delay(10) }
        val flow2 = flowOf("a", "b", "c").onEach { delay(15) }
        flow.combine(flow2) { i, s ->
            "$i$s"
        }.collect {
            //1a 2a 2b 2c
            Log.i(TAG, "collect: $it")
        }
    }

    /**
     * combineTransform 组合操作符
     * 组合每个流最新发出的值(受时间影响)
     * 与 combine 差不多,只是数据处理有点区别
     */
    fun combineTransformOnFlow() = viewModelScope.launch {
        val flow = flowOf(1, 2).onEach { delay(10) }
        val flow2 = flowOf("a", "b", "c").onEach { delay(15) }
        flow.combineTransform(flow2) { i, s ->
            emit("$i$s")
        }.collect {
            //1a 2a 2b 2c
            Log.i(TAG, "collect: $it")
        }
    }

    /**
     * merge 组合操作符
     * 合并多个流,并行,顺序不固定
     */
    fun mergeOnFlow() = viewModelScope.launch {
        val numberFlow = flowOf(1, 2).onEach { delay(10) }
        val stringFlow = flowOf("a", "b", "c").onEach { delay(15) }

        listOf(numberFlow, stringFlow).merge().collect {
            Log.i(TAG, "collect: $it")
        }
    }

    /**
     * flattenConcat 组合操作符
     * 按顺序合并流
     */
    @OptIn(FlowPreview::class)
    fun flattenConcatOnFlow() = viewModelScope.launch {
        val flow = flowOf(1, 2, 3)
        val flow2 = flowOf("a", "b", "c")
        flowOf(flow, flow2)
            .flattenConcat()
            .collect {
                Log.i(TAG, "collect: $it")
            }

        listOf(flow, flow2).merge()
    }

    /**
     * flattenMerge 组合操作符
     * 当等于1时,和flattenConcat一样
     * 大于1时,并发收集,顺序不固定
     */
    @OptIn(FlowPreview::class)
    fun flattenMergeOnFlow() = viewModelScope.launch {
        flow {
            emit(flowOf(1, 2, 3).flowOn(Dispatchers.IO))
            emit(flowOf(4, 5, 6).flowOn(Dispatchers.IO))
            emit(flowOf(7, 8, 9).flowOn(Dispatchers.IO))
        }.flattenMerge(3).collect {
            Log.i(TAG, "collect: $it")
        }
    }

    /**
     * flatMapConcat 组合操作符
     * map(transform).flattenConcat()的快捷方式
     * 先转换,再合并flow
     */
    @OptIn(FlowPreview::class)
    fun flatMapConcatOnFlow() = viewModelScope.launch {
        flowOf(1, 2, 3).flatMapConcat {
            flowOf("$it map")
        }.collect {
            Log.i(TAG, "collect: $it")
        }
    }

    /**
     * flatMapLatest 组合操作符
     * 内部就是transformLatest + emitAll
     * 如果下个值来了,之前的变换没结束,就取消掉
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun flatMapLastOnFlow() = viewModelScope.launch {
        flow {
            emit("a")
            delay(100)
            emit("b")
        }.flatMapLatest { value ->
            flow {
                emit(value)
                delay(200)
                emit(value + "_last")
            }
        }.collect { value ->
            //a b b_last
            Log.i(TAG, "collect: $value")
        }
    }

    /**
     * flatMapMerge 组合操作符
     * map + flattenMerge的简化写法
     *
     */
    @OptIn(FlowPreview::class)
    fun flatMapMergeOnFlow() = viewModelScope.launch {
        flowOf(1, 2, 3, 4)
            .flatMapMerge(3) { value ->
                flowOf("$value map").flowOn(Dispatchers.IO)
            }.collect { value ->
                Log.i(TAG, "collect: $value")
            }
    }

    /**
     * zip 组合操作符
     * 对两个流进行组合,一旦一个流结束了,那整个过程就结束了
     */
    fun zipOnFlow() = viewModelScope.launch {
        val flow = flowOf(1, 2, 3).onEach { delay(10) }

        val flow2 = flowOf("a", "b", "c", "d").onEach { delay(15) }

        flow.zip(flow2) { i, s -> i.toString() + s }.collect {
            //1a 2b 3c
            Log.i(TAG, "collect: $it")
        }

    }


    //----------------------------------------------------------------------

    /**
     * Flow 嵌套 (可以但不建议这么搞,可读性较差)
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
     * Flow 结合map 嵌套 (可以但不建议这么搞,可读性较差)
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
    Flow 中无法随意切换调度器,因为emit函数不是线程安全的
    想要生成元素时切换调度器,需要使用 channelFlow 创建 Flow
    (一般不要在 Flow 内部用withContext切换线程,要用flowOn或launchIn)
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

    //-------------------------Flow 异常处理--------------------------------------

    /**
     * Flow 异常处理
     *
     * 1. 如果只写 onCompletion() 不写 catch() ,程序会抛异常
     * 2. catch() 不能放在 onCompletion() 后面, 程序会抛异常
     * 3. catch() 只会捕获上游异常,如果是下游,或者想外部处理异常,智能在flow外部代码,try{}catch{}
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

    //-------------------------Flow 生命周期--------------------------------------

    /**
     * onEach 流程操作符,可以理解为遍历某个阶段的接果
     * 可以继续再后面加操作符,而collect()就完全结束了
     */
    fun lifecycleOnFlow() = viewModelScope.launch {
        flow {
            (1..3).forEach {
                Log.i(TAG, "Emitting $it")
                emit(it)
                delay(1000)
            }
        }
            .onStart { Log.i(TAG, "onStart") }
            .onEach {
                Log.i(TAG, "onEach $it")
            }.onCompletion {
                Log.i(TAG, "onCompletion")
            }.collect {
                Log.d(TAG, "collect  $it")
            }
    }

    //-------------------------Flow 切换线程--------------------------------------

    /**
     * flowOn 主要用于切换上游的线程
     */
    fun threadOnFlow() = viewModelScope.launch {
        flow {
            Log.i(TAG, "start ${Thread.currentThread()}")
            emit(1)
            Log.i(TAG, "Emit: 1 ${Thread.currentThread()}")
            emit(2)
            Log.i(TAG, "Emit: 2 ${Thread.currentThread()}")
            emit(3)
            Log.i(TAG, "Emit: 3 ${Thread.currentThread()}")
        }.filter {
            Log.i(TAG, "Filter: $it ${Thread.currentThread()}")
            it > 2
        }.flowOn(Dispatchers.IO)//上游的都是子线程
            .collect {
                Log.i(TAG, "collect: $it ${Thread.currentThread()}")//主线程
            }
    }

    /**
     * launchIn 上下游都切换线程
     */
    fun launchInOnFlow() {
        val scope = CoroutineScope(Dispatchers.Main)
        flow {
            Log.i(TAG, "start ${Thread.currentThread()}")
            emit(1)
            Log.i(TAG, "Emit: 1 ${Thread.currentThread()}")
            emit(2)
            Log.i(TAG, "Emit: 2 ${Thread.currentThread()}")
            emit(3)
            Log.i(TAG, "Emit: 3 ${Thread.currentThread()}")
        }.flowOn(Dispatchers.Main)  //flow 在main线程
            .filter {
                Log.i(TAG, "Filter: $it ${Thread.currentThread()}")
                it > 2
            }
            .flowOn(Dispatchers.IO)//filter 在子线程
            .onEach {
                Log.i(TAG, "onEach: $it ${Thread.currentThread()}")
            }
            .launchIn(scope)//onEach在主线程
    }

    //-------------------------Flow 缓冲区--------------------------------------

    /**
     * buffer
     * 提供缓存区等待数据处理,
     * 如果操作比较耗时,可用buffer在执行期间创建一个单独的协程
     */
    fun bufferOnFlow() = viewModelScope.launch {
        //这个例子可见是一发一收
        flowOf("A", "B", "C")
            .onEach {
                delay(100)
                Log.i(TAG, "onEach 1$it")
            }
            .collect {
                delay(300)
                Log.i(TAG, "collect 2$it")
            }

        //从这个例子可见,直接发送2个元素到缓冲区,当缓存区元素被消费了,又把后续的元素放到缓存区
        flowOf("a", "b", "c")
            .onEach {
                delay(100)
                Log.d(TAG, "--- onEach 1$it")
            }
            .buffer(2)
            .collect {
                delay(300)
                Log.d(TAG, "--- collect 2$it")
            }

    }

    /**
     * conflate
     * 内部就是 buffer(CONFLATED)
     * 当生产者比消费者慢时,消费者跳过这些元素,只处理最新,从而加快处理速度
     */
    fun conflateOnFlow() = viewModelScope.launch {
        flow {
            repeat(30) {
                delay(100)
                emit(it)
            }
        }.conflate().onEach { delay(1000) }.collect { value ->
            Log.i(TAG, "collect $value")
        }
    }


    companion object {
        val TAG = "FlowOperator"
    }


}