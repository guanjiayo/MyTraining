package zs.xmx.hi.training.livedata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import zs.xmx.hi.training.R

/**
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:   LiveData,Handler发送消息对比测试
 *         当页面不可见,Handler/LiveData是否会继续发送消息
 */
class LiveDataTest2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_data2)
    }
}