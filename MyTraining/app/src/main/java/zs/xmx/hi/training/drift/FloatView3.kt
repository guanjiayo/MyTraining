package zs.xmx.hi.training.drift

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.provider.Settings
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.widget.*
import androidx.appcompat.widget.LinearLayoutCompat
import zs.xmx.hi.training.R
import zs.xmx.hi.training.utils.AppGlobals


/**
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:
 *         注意要有悬浮窗权限才能显示 "android.permission.SYSTEM_ALERT_WINDOW"
 *
 */
class FloatView3 {

    private val application: Application = AppGlobals.get()!!
    private var params = WindowManager.LayoutParams()
    private var windowManager: WindowManager? = null
    private var isShow = false
    private var floatView =
        LayoutInflater.from(application).inflate(R.layout.layout_float_view3, null, false) as View
    private lateinit var mShowAnimatorSet: AnimatorSet
    private lateinit var mHideAnimatorSet: AnimatorSet
    private val mBtn: Button
    private val mTextView: TextView
    private val mRootView: LinearLayoutCompat

    init {
        mTextView = floatView.findViewById<Button>(R.id.tv_title)
        mBtn = floatView.findViewById<Button>(R.id.btn_click)
        mRootView = floatView.findViewById<LinearLayoutCompat>(R.id.root_view)
        mBtn.setOnClickListener {
            hide()
        }
        initAnimator()
        initLayoutParams()
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
    }

    /**
     * 初始化属性动画
     */
    @SuppressLint("Recycle")
    private fun initAnimator() {
        mShowAnimatorSet = AnimatorSet()
        val showAnimator = arrayOfNulls<Animator>(3)
        showAnimator[0] = ObjectAnimator.ofFloat(
            mTextView, "alpha",
            0.0f, 1.0f
        )
        showAnimator[1] = ObjectAnimator.ofFloat(
            mBtn, "alpha",
            0.0f, 1.0f
        )
        showAnimator[2] = ObjectAnimator.ofFloat(
            mRootView, "alpha",
            0.0f, 1.0f
        )
        mShowAnimatorSet.playTogether(*showAnimator)
        mShowAnimatorSet.duration = 1500L

        mHideAnimatorSet = AnimatorSet()
        val hideAnimator = arrayOfNulls<Animator>(3)
        hideAnimator[0] = ObjectAnimator.ofFloat(
            mTextView, "alpha",
            1.0f, 0.0f
        )
        hideAnimator[1] = ObjectAnimator.ofFloat(
            mBtn, "alpha",
            1.0f, 0.0f
        )
        hideAnimator[2] = ObjectAnimator.ofFloat(
            mRootView, "alpha",
            1.0f, 0.0f
        )
        mHideAnimatorSet.playTogether(*hideAnimator)
        mHideAnimatorSet.duration = 1500L
    }

    fun show() {
        if (!isShow) {
            if (hasOverlayPermission()) {
                windowManager!!.addView(floatView, params)
                mShowAnimatorSet.start()
                isShow = true
            } else {
                Toast.makeText(application, "未添加悬浮窗权限", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun hide() {
        if (isShow) {
            mHideAnimatorSet.start()
            mHideAnimatorSet.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    windowManager!!.removeView(floatView)
                    isShow = false
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

            })
        }

    }

    private fun hasOverlayPermission(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(
            application
        )
    }

}