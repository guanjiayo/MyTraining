package zs.xmx.hi.training.livedata

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import zs.xmx.hi.training.R

class LiveDataTestActivity : AppCompatActivity() {

    private val foreverObserver =
        Observer<String> { Log.e("LiveDataTestCActivity", "forever 参数返回： $it") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_data)


        findViewById<AppCompatButton>(R.id.btn).setOnClickListener {
            startActivity(Intent(this, LiveDataTestBActivity::class.java))
        }

        findViewById<AppCompatButton>(R.id.btn_no_sticky).setOnClickListener {
            LiveDataBus.with<String>("noStickyData").setStickyData("noStickyData Event")
            startActivity(Intent(this, LiveDataTestCActivity::class.java))
        }

        findViewById<AppCompatButton>(R.id.btn_sticky).setOnClickListener {
            LiveDataBus.with<String>("StickyData").setStickyData("StickyData Event(粘性)")
            startActivity(Intent(this, LiveDataTestCActivity::class.java))
        }

        findViewById<AppCompatButton>(R.id.btn_sticky_next).setOnClickListener {
            LiveDataBus.with<String>("StickyDataNext").setStickyData("发送一条粘性事件")
            startActivity(Intent(this, LiveDataTestCActivity::class.java))
        }

        LiveDataBus.with<String>("forever").observeForever(foreverObserver)

    }

    override fun onDestroy() {
        super.onDestroy()
        LiveDataBus.with<String>("forever").removeObserver(foreverObserver)
    }

}