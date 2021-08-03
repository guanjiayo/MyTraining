package zs.xmx.hi.training

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import zs.xmx.hi.training.drift.FloatViewActivity
import zs.xmx.hi.training.handler.HandlerActivity
import zs.xmx.hi.training.lifecycle.LifecycleTestActivity
import zs.xmx.hi.training.livedata.LiveDataTestActivity
import zs.xmx.hi.training.slide_conflict.SlideConflictActivity
import zs.xmx.hi.training.webview.H5Activity
import java.util.*

/*
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:   首页
 */
class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    private lateinit var mList: MutableList<String>
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initData()
        initEvent()
    }

    private fun initEvent() {
        listView.adapter = ArrayAdapter(this, R.layout.activity_main, mList)
        listView.onItemClickListener = this
    }

    private fun initData() {
        mList = ArrayList()
        mList.add("Handler")
        mList.add("Sliding conflict")
        mList.add("WebView")
        mList.add("WindowManage 悬浮窗测试")
        mList.add("生命周期")
        mList.add("LiveData测试")
    }

    private fun initView() {
        listView = ListView(this)
        setContentView(
            listView, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                    .LayoutParams.MATCH_PARENT
            )
        )

    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        when (position) {
            0 -> startActivity(Intent(this, HandlerActivity::class.java))
            1 -> startActivity(Intent(this, SlideConflictActivity::class.java))
            2 -> startActivity(Intent(this, H5Activity::class.java))
            3 -> startActivity(Intent(this, FloatViewActivity::class.java))
            4 -> startActivity(Intent(this, LifecycleTestActivity::class.java))
            5 -> startActivity(Intent(this, LiveDataTestActivity::class.java))
        }
    }


}
