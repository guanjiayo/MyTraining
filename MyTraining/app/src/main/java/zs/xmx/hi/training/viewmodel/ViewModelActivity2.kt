package zs.xmx.hi.training.viewmodel

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import zs.xmx.hi.training.R

class ViewModelActivity2 : AppCompatActivity() {

    //private val mViewModel by viewModels<CounterViewModel>()


//      private val mViewModel: CounterViewModel by applicationViewModels{
//          ViewModelProvider.AndroidViewModelFactory.getInstance(application)
//      }
      private val mViewModel: CounterViewModel by applicationViewModels()

    //private val mViewModel = ViewModelProvider(MyApplication()).get(CounterViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_model2)

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
        Log.i("TestA", "count---: ${mViewModel.count}")
        mViewModel.counter.observe(this) {
            tvCount.text = "$it"
        }

    }
}