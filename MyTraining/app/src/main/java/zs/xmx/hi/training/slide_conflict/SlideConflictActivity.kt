package zs.xmx.hi.training.slide_conflict

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.HandlerCompat
import zs.xmx.hi.training.R

/*
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:   滑动冲突案例首页
 */
class SlideConflictActivity : AppCompatActivity() {

    private val tag: String = SlideConflictActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide_conflict)

        //todo 在子线程创建Handler向主线程发送消息的方式

        //HandlerThread().start()

    }

    override fun onResume() {
        super.onResume()
        HandlerThread().start()
    }


    inner class HandlerThread : Thread() {
        private lateinit var mHandler: Handler

        override fun run() {
            super.run()
            Looper.prepare()
            createHandler()
            val msg = Message.obtain()
            msg.what = 1
            mHandler.sendMessage(msg)
            Log.i(tag, "sendMessage: " + currentThread().name)
            Looper.loop()
        }

        private fun createHandler() {
            mHandler = @SuppressLint("HandlerLeak")
            object : Handler() {
                override fun handleMessage(msg: Message) {
                    super.handleMessage(msg)
                    if (msg.what == 1) {
                        Log.i(
                            tag,
                            "what 1  handleMessage: ${currentThread().name}  ${Looper.myLooper()?.thread?.isAlive}"
                        )
                    } else {
                        Log.i(tag, "what else  handleMessage: ${currentThread().name}")
                    }
                    Looper.myLooper()?.quitSafely()
                    Log.i(
                        tag,
                        "final  handleMessage: ${currentThread().name}  ${Looper.myLooper()?.isCurrentThread}"
                    )
                }
            }


        }


    }


}