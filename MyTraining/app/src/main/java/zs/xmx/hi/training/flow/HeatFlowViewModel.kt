package zs.xmx.hi.training.flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HeatFlowViewModel : ViewModel() {

    //-----------------------Flow 冷流转热流-----------------------------------------------

    //todo shareIn  , stateIn

    /**
     * stateIn , 将Flow冷流转换成 StateFlow类型的热流对象
     */

    private val _uiState = MutableStateFlow(0)

    val uiState: StateFlow<Int> = _uiState
    fun stateInOnFlow() = viewModelScope.launch {
        val flow = flowOf(1, 2, 3, 4, 5)


         flow.stateIn(viewModelScope)

    }


    /**
     * shareIn , 将Flow冷流转换成 SharedFlow类型的热流对象
     */


}