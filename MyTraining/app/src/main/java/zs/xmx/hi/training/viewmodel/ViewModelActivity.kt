package zs.xmx.hi.training.viewmodel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import zs.xmx.hi.training.R

class ViewModelActivity : AppCompatActivity() {

    private val mViewModel by viewModels<CounterViewModel>()

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
        mViewModel.counter.observe(this, {
            tvCount.text = "$it"
        })

    }
}