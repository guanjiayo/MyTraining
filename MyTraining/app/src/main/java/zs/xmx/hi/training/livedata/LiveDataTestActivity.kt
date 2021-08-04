package zs.xmx.hi.training.livedata

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import zs.xmx.hi.training.R
import java.util.*

class LiveDataTestActivity : AppCompatActivity() {

    private val foreverObserver =
        Observer<String> { Log.e("LiveDataTest3Activity", "forever 参数返回： $it") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_data)


        findViewById<AppCompatButton>(R.id.btn).setOnClickListener {
            startActivity(Intent(this, LiveDataTest2Activity::class.java))
        }

        findViewById<AppCompatButton>(R.id.btn_sticky_moni).setOnClickListener {
            startActivity(Intent(this, LiveDataTest3Activity::class.java))
        }

        findViewById<AppCompatButton>(R.id.btn_no_sticky).setOnClickListener {
            LiveDataBus.with<String>("noStickyData").setStickyData("noStickyData Event")
            startActivity(Intent(this, LiveDataTest3Activity::class.java))
        }

        findViewById<AppCompatButton>(R.id.btn_sticky).setOnClickListener {
            LiveDataBus.with<String>("StickyData").setStickyData("发送了一条粘性事件")
            startActivity(Intent(this, LiveDataTest3Activity::class.java))
        }

        findViewById<AppCompatButton>(R.id.btn_sticky_next).setOnClickListener {
            LiveDataBus.with<String>("StickyDataNext").setStickyData("发送一条粘性事件")
            startActivity(Intent(this, LiveDataTest3Activity::class.java))
        }

        LiveDataBus.with<String>("forever").observeForever(foreverObserver)

    }

    override fun onDestroy() {
        super.onDestroy()
        LiveDataBus.with<String>("forever").removeObserver(foreverObserver)
    }

}