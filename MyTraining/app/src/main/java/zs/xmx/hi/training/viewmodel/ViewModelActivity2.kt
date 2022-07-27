package zs.xmx.hi.training.viewmodel

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import zs.xmx.hi.training.R

class ViewModelActivity2 : BaseViewModelActivity() {


    /**
     * 方案三: kotlin扩展
     */
    private val mViewModel: CounterViewModel by applicationViewModels()

    /**
     *  方案一,跟随某个Activity的作用域
     */
    /* val mViewModel = ViewModelProvider(
         ViewModelActivity.mViewModelActivity!!
     ).get(CounterViewModel::class.java)*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_model2)

        //方案二使用
//        val mViewModel = getApplicationScopeViewModel(CounterViewModel::class.java)

        findViewById<Button>(R.id.plus).setOnClickListener {
            mViewModel.plusOne()
        }

        findViewById<Button>(R.id.clear).setOnClickListener {
            mViewModel.clear()
        }
        findViewById<Button>(R.id.btn_back).setOnClickListener {
            val userModel = User("Kotlin", 123)
            UserInfo.mUserModel.value = userModel
            finish()
        }

        val tvCount = findViewById<TextView>(R.id.tvCount)
        Log.i("TestA", "count---: ${mViewModel}")
        mViewModel.counter.observe(this) {
            tvCount.text = "$it"
        }

    }
}