package zs.xmx.hi.training.flow.operator

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import zs.xmx.hi.training.R

class FlowOperatorActivity : AppCompatActivity() {

    private val mViewModel by viewModels<FlowOperatorViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow_operator)

        //mViewModel.flatMapConcat()
        //mViewModel.flowNested()
        //mViewModel.channelFlow()
        //mViewModel.flowMapNested()
        //mViewModel.flattenConcat()
        mViewModel.catch()

    }


}