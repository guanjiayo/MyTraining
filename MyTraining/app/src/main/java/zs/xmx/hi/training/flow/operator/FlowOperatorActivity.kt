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

        //mViewModel.mapOnFlow()
        //mViewModel.transformOnMap()
        //mViewModel.withIndexOnMap()
        //mViewModel.scanOnFlow()
        //mViewModel.dropOnFlow()
        // mViewModel.dropWhileOnFlow()
        // mViewModel.takeOnFlow()
        //mViewModel.takeWhileOnFlow()
        //mViewModel.debounceOnFlow()
        //mViewModel.distinctUntilChangedByOnFlow()
        //mViewModel.distinctUntilChangedOnFlow()
        //mViewModel.combineOnFlow()
        //mViewModel.combineTransformOnFlow()
        //mViewModel.mergeOnFlow()
        //mViewModel.flattenConcatOnFlow()
        //mViewModel.flattenMergeOnFlow()
        //mViewModel.flatMapConcatOnFlow()
        //mViewModel.flatMapLastOnFlow()
        //mViewModel.flatMapMergeOnFlow()
        //mViewModel.zipOnFlow()
        //mViewModel.bufferOnFlow()
        //mViewModel.conflateOnFlow()
        //mViewModel.stateInOnFlow()
        mViewModel.sharedInOnFlow()
        //mViewModel.flatMapConcat()
        //mViewModel.flowNested()
        //mViewModel.channelFlow()
        //mViewModel.flowMapNested()
        //mViewModel.flattenConcat()
        //mViewModel.catch()
        //mViewModel.lifecycleOnFlow()
        //mViewModel.threadOnFlow()
        //mViewModel.launchInOnFlow()

    }


}