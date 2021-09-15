package zs.xmx.hi.training.viewmodel

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import zs.xmx.hi.training.R

class ViewModelActivity2 : AppCompatActivity() {

    private val mViewModel by viewModels<CounterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_model2)

        findViewById<Button>(R.id.plus).setOnClickListener {
            mViewModel.plusOne()
        }

        findViewById<Button>(R.id.clear).setOnClickListener {
            mViewModel.clear()
        }

        val tvCount = findViewById<TextView>(R.id.tvCount)
        mViewModel.counter.observe(this, {
            tvCount.text = "$it"
        })

    }
}