package zs.xmx.hi.training.viewmodel

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import zs.xmx.hi.training.R


class ViewModelActivity : BaseViewModelActivity() {

    /**
     * 方案三: kotlin扩展
     */
    private val mViewModel: CounterViewModel by applicationViewModels()

    companion object {
        //方案一,跟随某个Activity的作用域
        var mViewModelActivity: ViewModelActivity? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_model)

        //方案一,使用
        /*mViewModelActivity = this
        val mViewModel = ViewModelProvider(
            mViewModelActivity!!
        )[CounterViewModel::class.java]*/


        //方案二,使用
        //val mViewModel = getApplicationScopeViewModel(CounterViewModel::class.java)

        findViewById<Button>(R.id.plus).setOnClickListener {
            mViewModel.plusOne()
        }

        findViewById<Button>(R.id.clear).setOnClickListener {
            mViewModel.clear()
        }

        findViewById<Button>(R.id.to2).setOnClickListener {
            startActivity(Intent(this, ViewModelActivity2::class.java))
        }

        val tvCount = findViewById<TextView>(R.id.tvCount)
        Log.i("TestA", "count: ${mViewModel}")
        mViewModel.counter.observe(this) {
            //横竖屏切换时会继续有回调
            tvCount.text = "$it"
        }

        //单例类定义LiveData方案
        UserInfo.mUserModel.observe(this) {
            Log.i("TestB", "age: ${it.age}")
        }

    }
}