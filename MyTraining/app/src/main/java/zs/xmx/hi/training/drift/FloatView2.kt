package zs.xmx.hi.training.drift

import android.app.Application
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import zs.xmx.hi.training.R
import zs.xmx.hi.training.utils.AppGlobals


/**
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:
 *         注意要有悬浮窗权限才能显示 "android.permission.SYSTEM_ALERT_WINDOW"
 */
class FloatView2 {

    private val application: Application = AppGlobals.get()!!
    private var params = WindowManager.LayoutParams()
    private var windowManager: WindowManager? = null
    private var isShow = false
    private var floatView =
        LayoutInflater.from(application).inflate(R.layout.layout_float_view2, null, false) as View

    init {
        initLayoutParams()
        floatView.findViewById<Button>(R.id.btn_click).setOnClickListener {
            hide()
        }
    }

    /**
     * 初始化LayoutParams
     */
    private fun initLayoutParams() {
        windowManager = application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        }
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        //不设置这个弹出框的透明遮罩显示为黑色
        params.format = PixelFormat.TRANSLUCENT
        //设置顶部居中显示
        params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        //WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，弹出的View收不到Back键的事件
        //FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        //FLAG_ALT_FOCUSABLE_IM 不允许显示软键盘
        params.flags = (WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
                or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        //设置进入退出动画
        //有时候刚写好样式没反应,卸载重装一下app
        params.windowAnimations = R.style.NoticeViewerAnim
    }

    fun show() {
        if (!isShow) {
            if (hasOverlayPermission()) {
                windowManager!!.addView(floatView, params)
                isShow = true
            } else {
                Toast.makeText(application, "未添加悬浮窗权限", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun hide() {
        if (isShow) {
            windowManager!!.removeView(floatView)
            isShow = false
        }
    }

    private fun hasOverlayPermission(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(
            application
        )
    }

}