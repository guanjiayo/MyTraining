package zs.xmx.hi.training.drift

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import zs.xmx.hi.training.R

/**
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:   Window.addView,显示动画异常测试
 *         注意要有悬浮窗权限才能显示 "android.permission.SYSTEM_ALERT_WINDOW"
 */
class FloatViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_float_view)
        test1()
        test2()
        test3()
    }

    private fun test1() {
        val floatView = FloatView1()
        findViewById<Button>(R.id.btn_show1).setOnClickListener {
            floatView.show()
        }
        findViewById<Button>(R.id.btn_hide1).setOnClickListener {
            floatView.hide()
        }
    }

    private fun test2() {
        val floatView2 = FloatView2()
        findViewById<Button>(R.id.btn_show2).setOnClickListener {
            floatView2.show()
        }
        findViewById<Button>(R.id.btn_hide2).setOnClickListener {
            floatView2.hide()
        }
    }

    private fun test3() {
        val floatView3 = FloatView3()
        findViewById<Button>(R.id.btn_show3).setOnClickListener {
            floatView3.show()
        }
        findViewById<Button>(R.id.btn_hide3).setOnClickListener {
            floatView3.hide()
        }
    }
}