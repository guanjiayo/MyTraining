package zs.xmx.hi.training.flow.network

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import zs.xmx.hi.training.R

class NetRequestActivity : AppCompatActivity() {

    private val mViewModel by viewModels<NetRequestViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_net_request)
        initObserver()
        initEvent()
    }

    private fun initEvent() {
        findViewById<Button>(R.id.btn_request1).setOnClickListener {
            mViewModel.getDogImages1(true)
        }

        findViewById<Button>(R.id.btn_request2).setOnClickListener {
            mViewModel.getDogImages2(true)
        }
        findViewById<Button>(R.id.btn_request3).setOnClickListener {
            mViewModel.getDogImages3(true)
        }
        findViewById<Button>(R.id.btn_request4).setOnClickListener {
            mViewModel.getDogImages4(true)
        }
    }

    private fun initObserver() {
        mViewModel.livedataObs.observe(this) {
            Toast.makeText(this, "请求成功,  size: ${it.size}", Toast.LENGTH_SHORT).show()
        }

        mViewModel.livedataExtObs.observe(this) {
            Toast.makeText(this, "请求成功,  size: ${it.size}", Toast.LENGTH_SHORT).show()
        }


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mViewModel.stateFlowObs.collect {
                        if (it.isNotEmpty()) {
                            Toast.makeText(
                                this@NetRequestActivity,
                                "请求成功,  size: ${it.size}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                launch {
                    mViewModel.uiState.collect {
                        when (it) {
                            is ResultWrapper2.Failure -> {
                                Toast.makeText(
                                    this@NetRequestActivity,
                                    "请求失败,  size: ${it.throwable}",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                            is ResultWrapper2.Success<*> -> {
                                val result = it.value as? List<String>
                                 if (result!!.isNotEmpty()) {
                                     Toast.makeText(
                                         this@NetRequestActivity,
                                         "请求成功,  size: ${result.size}",
                                         Toast.LENGTH_SHORT
                                     ).show()
                                 }
                            }
                        }
                    }
                }

            }
        }

        mViewModel.failure.observe(this, {
            Toast.makeText(this, "请求失败,  msg: $it", Toast.LENGTH_SHORT).show()
        })
    }
}