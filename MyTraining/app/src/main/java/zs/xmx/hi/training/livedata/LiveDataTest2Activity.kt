package zs.xmx.hi.training.livedata

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import zs.xmx.hi.training.R

/**
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:   LiveData,Handler发送消息对比测试
 *         当页面不可见,Handler/LiveData是否会继续发送消息
 */
class LiveDataTest2Activity : AppCompatActivity() {
    private val TAG = "跨页面测试"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_data2)
        LiveDataBus.with<String>("StickyDataNext")
            .observerSticky(this, true,
                { t -> Log.e(TAG, "onCreate StickyDataNext 参数返回： $t") })

        findViewById<AppCompatButton>(R.id.btn_sticky_next).setOnClickListener {
            LiveDataBus.with<String>("StickyDataNext").setStickyData("再发送一条粘性事件2")
            startActivity(Intent(this, LiveDataTest3Activity::class.java))
            finish()
        }
    }
}