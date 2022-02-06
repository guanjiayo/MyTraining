package zs.xmx.hi.training.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


/**
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:
 *
 */
class CounterViewModel : ViewModel() {

    /*
      计数器案例验证:
      测试两个Activity直接使用同一个ViewModel能不能共享数据?
      结论:
      会作为单独的ViewModel使用
     */
    val counter: LiveData<Int>
        get() = _counter
    private val _counter = MutableLiveData<Int>()

    var count = 0

    fun plusOne() {
        count = _counter.value ?: 0
        _counter.value = count + 1
    }

    fun clear() {
        _counter.value = 0
    }

}