package zs.xmx.hi.training.flow

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import zs.xmx.hi.training.R
import zs.xmx.hi.training.flow.network.NetRequestActivity
import zs.xmx.hi.training.flow.operator.FlowOperatorActivity

class FlowTestActivity : AppCompatActivity() {

    private val tag = FlowTestActivity::class.java.simpleName

    private val mStateFlowViewModel by viewModels<StateFlowViewModel>()
    private val mStateFlowMediatorViewModel by viewModels<StateFlowMediatorViewModel>()
    private val mSharedFlowViewModel by viewModels<SharedFlowViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow_test)

        findViewById<Button>(R.id.btn_stateflow).setOnClickListener {
            mStateFlowViewModel.changeData("StateFlow 模仿LiveData 测试")
        }

        findViewById<Button>(R.id.btn_flow1).setOnClickListener {
            mStateFlowMediatorViewModel.flow1pp()
        }

        findViewById<Button>(R.id.btn_flow2).setOnClickListener {
            mStateFlowMediatorViewModel.flow2pp()
        }

        findViewById<Button>(R.id.btn_flow3).setOnClickListener {
            mStateFlowMediatorViewModel.flow3pp()
        }

        findViewById<Button>(R.id.btn_operation).setOnClickListener {
            startActivity(Intent(this, FlowOperatorActivity::class.java))
        }
        findViewById<Button>(R.id.btn_network).setOnClickListener {
            startActivity(Intent(this, NetRequestActivity::class.java))
        }

        /*
        如果需要更新界面，切勿使用 launch 或 launchIn 扩展函数从界面直接收集数据流。
        即使 View 不可见，这些函数也会处理事件。此行为可能会导致应用崩溃。
        为避免这种情况，请使用 repeatOnLifecycle API（如上所示）。
         */

        //1. 在生命周期范围内启动协程
        lifecycleScope.launch {
            //2. repeatOnLifecycle 配置宿主在 STARTED 状态时执行操作,超出这个状态后该协程会自动取消
            //repeatOnLifecycle API 仅在 androidx.lifecycle:lifecycle-runtime-ktx:2.4.0-alpha01 库及更高版本中提供。
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mStateFlowViewModel.uiState.collect {
                        Log.i(tag, "数据:  $it  宿主状态:  ${lifecycle.currentState}")
                    }
                }

                launch {
                    mStateFlowMediatorViewModel.flow.collect {
                        Log.i(tag, "数量:  $it  ${lifecycle.currentState}")
                        findViewById<TextView>(R.id.tv_flow_count).text = "数量: $it"
                    }
                }

            }
        }

        sharedFlowTest()
    }

    private fun sharedFlowTest() {
        // 启动第一个协程，接收初始化的数据
        lifecycleScope.launch {
            val sb = StringBuffer()
            mSharedFlowViewModel.sharedFlow.collect {
                sb.append("<<${it}")
                findViewById<TextView>(R.id.tv_shareFlow1).text = sb.toString()
            }
        }

        findViewById<Button>(R.id.btn_go).setOnClickListener {
            mSharedFlowViewModel.doAsClick()
            // 发送新的数据以后，启动第二个协程
            lifecycleScope.launch {
                val sb = StringBuffer()
                mSharedFlowViewModel.sharedFlow.collect {
                    sb.append("<<${it}")
                    Log.i(tag, "sharedFlowTest: ")
                    findViewById<TextView>(R.id.tv_shareFlow2).text = sb.toString()
                }
            }
        }

    }
}