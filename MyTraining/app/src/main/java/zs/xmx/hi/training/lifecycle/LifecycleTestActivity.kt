package zs.xmx.hi.training.lifecycle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import zs.xmx.hi.training.R

class LifecycleTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lifecycle_test)
    }

    override fun onStart() {
        super.onStart()
        lifecycle.addObserver(LocationObserver())
    }
}