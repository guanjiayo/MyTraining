package zs.xmx.hi.training.livedata

import android.content.Intent
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
 *         粘性事件,就是说,我们同时注册多个观察者,后面新注册的会收到之前注册的观察者接收到的消息 当前源码 onResume()可以模拟出来
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

        findViewById<AppCompatButton>(R.id.btn_sticky_next).setOnClickListener {
            LiveDataBus.with<String>("StickyDataNext").setStickyData("再发送一条粘性事件")
            startActivity(Intent(this, LiveDataTest2Activity::class.java))
            finish()
        }

        LiveDataBus.with<String>("StickyDataNext")
            .observerSticky(this, true,
                { t -> Log.e("跨页面测试", "onCreate StickyDataNext 参数返回： $t") })

        /*
           测试跨页面测试时,把以下noStickyData,StickyData,还有onResume()的注释以下，不然影响测试
         */
        LiveDataBus.with<String>("noStickyData")
            .observerSticky(this, false,
                { t -> Log.e(TAG, "onCreate noStickyData 参数返回： $t") })

        LiveDataBus.with<String>("StickyData")
            .observerSticky(this, true,
                { t -> Log.e(TAG, "onCreate StickyData 参数返回： $t") })
    }

    override fun onResume() {
        super.onResume()
        //注册多少个观察者,就能接受多少个消息
        /*Handler(Looper.getMainLooper()).postDelayed(Runnable {
            LiveDataBus.with<String>("noStickyData").observerSticky(this, false, Observer {
                Log.e(TAG, "onResume noStickyData 参数返回： $it")
            })
            LiveDataBus.with<String>("StickyData").observerSticky(this, true, Observer {
                Log.e(TAG, "onResume StickyData 参数返回： $it")
            })
        }, 3000)*/
    }
}