package zs.xmx.hi.training.flow

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.*


/**
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:   StateFlow 实现类似 MediatorViewModel 效果 (支持不同基本类型)
 *
 */
class StateFlowMediatorViewModel : ViewModel() {

    private var count1 = 0//第一个按钮点击的次数
    private var count2 = 0//第二个按钮点击的次数
    private val flow1 = MutableStateFlow(0)
    private val flow2 = MutableStateFlow(0)
    private val flow3 = MutableStateFlow("")

    //combine 将两个flow融合,就类似 MediatorViewModel把多个LiveData结合起来的效果了
    val flow = combine(flow1, flow2, flow3) { data1, data2, data3 ->
        //return@combine 可以省略不写
        return@combine "${data1 + data2} ---  $data3" //将两个flow融合，分别点击的数量相加
    }

    fun flow1pp() {
        flow1.value = ++count1//点击第一个按钮数量加1
    }

    fun flow2pp() {
        flow2.value = --count2//点击第二个按钮数量减1
    }

    fun flow3pp() {
        flow3.value = "测试文本"
    }

}