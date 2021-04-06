package zs.xmx.hi.training.slide_conflict

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import zs.xmx.hi.training.R
import java.util.*

/*
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:   滑动冲突案例首页
 */
class SlideConflictActivity : AppCompatActivity() {

    private val tag: String = SlideConflictActivity::class.java.simpleName

    private val iv = intArrayOf(
        R.mipmap.iv_0, R.mipmap.iv_1, R.mipmap.iv_2,
        R.mipmap.iv_3, R.mipmap.iv_4, R.mipmap.iv_5,
        R.mipmap.iv_6, R.mipmap.iv_7, R.mipmap.iv_8
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide_conflict)
        val pager: BadViewPager = findViewById(R.id.viewpager)

        val strings: MutableList<Map<String, Int>> = ArrayList()

        var map: MutableMap<String, Int>

        for (i in 0..19) {
            map = HashMap()
            map["key"] = iv[i % 9]
            strings.add(map)
        }

        val adapter = MyPagerAdapter(this, strings)
        pager.adapter = adapter
    }




}