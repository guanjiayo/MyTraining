package zs.xmx.hi.training.livedata

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import zs.xmx.hi.training.R


/**
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:   LiveDataBus测试
 */
class LiveDataTest3Activity : AppCompatActivity() {
    private val TAG = "LiveDataTest3Activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_data3)

        findViewById<AppCompatButton>(R.id.btn_no_sticky).setOnClickListener {
            LiveDataBus.with<String>("noStickyData").setStickyData("当前页面发送普通事件")
        }

        findViewById<AppCompatButton>(R.id.btn_sticky).setOnClickListener {
            LiveDataBus.with<String>("StickyData").setStickyData("当前页面发送了粘性事件")
        }
        findViewById<AppCompatButton>(R.id.btn_forever).setOnClickListener {
            LiveDataBus.with<String>("forever").setStickyData("当前页面发送forever事件")
        }

        LiveDataBus.with<String>("noStickyData")
            .observerSticky(this, false,
                { t -> Log.e(TAG, "onCreate noStickyData 参数返回： $t") })

        LiveDataBus.with<String>("StickyData")
            .observerSticky(this, true,
                { t -> Log.e(TAG, "onCreate StickyData 参数返回： $t") })
    }

    override fun onResume() {
        super.onResume()
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            LiveDataBus.with<String>("noStickyData").observerSticky(this, false, Observer {
                Log.e(TAG, "onResume noStickyData 参数返回： $it")
            })
            LiveDataBus.with<String>("StickyData").observerSticky(this, true, Observer {
                Log.e(TAG, "onResume StickyData 参数返回： $it")
            })
        }, 3000)

    }
}