package zs.xmx.hi.training.viewmodel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatTextView
import zs.xmx.hi.training.R


/**
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:   方案三测试,是否能拿到ApplicationViewModel
 *
 */
class ViewModelFragment : BaseViewModelFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_view_model, container, false)
        initEvent(rootView)
        return rootView
    }

    private fun initEvent(rootView: View) {
        //方案二使用
        val mViewModel = getApplicationScopeViewModel(CounterViewModel::class.java)

        rootView.findViewById<Button>(R.id.plus).setOnClickListener {
            mViewModel.plusOne()
        }

        rootView.findViewById<Button>(R.id.clear).setOnClickListener {
            mViewModel.clear()
        }

        mViewModel.counter.observe(viewLifecycleOwner) {
            rootView.findViewById<AppCompatTextView>(R.id.tv_count).text = "$it"
        }
    }


}