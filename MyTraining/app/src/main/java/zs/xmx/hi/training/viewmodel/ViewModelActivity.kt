package zs.xmx.hi.training.viewmodel

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import zs.xmx.hi.training.R

class ViewModelActivity : AppCompatActivity() {

    //private val mViewModel by viewModels<CounterViewModel>()
    private val mViewModel: CounterViewModel by applicationViewModels()
   // private val mViewModel = ViewModelProvider(MyApplication()).get(CounterViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_model)

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
        Log.i("TestA", "count: ${mViewModel.count}")
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