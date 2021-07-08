package zs.xmx.hi.training.nested_rv

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import zs.xmx.hi.training.R
import kotlin.math.abs


/**
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:   内层的RecyclerView
 *
 */
class ChildRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseNestedRecyclerView(context, attrs, defStyleAttr) {

    private var parentRecyclerView: ParentRecyclerView? = null

    private val mTouchSlop: Int
    private var downX: Float = 0f
    private var downY: Float = 0f

    private var dragState: Int = DRAG_IDLE

    companion object {
        private const val DRAG_IDLE = 0
        private const val DRAG_VERTICAL = 1
        private const val DRAG_HORIZONTAL = 2
    }

    init {
        val configuration = ViewConfiguration.get(context)
        //scaledTouchSlop 是一个距离，表示滑动的时候，手的移动要大于这个距离才开始移动控件。
        // 如果小于这个距离就不触发移动控件，如viewpager就是用这个距离来判断用户是否翻页。
        mTouchSlop = configuration.scaledTouchSlop
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)

        // 是否已经停止scrolling
        if (state == SCROLL_STATE_IDLE) {
            // 这里是考虑到当整个childRecyclerView被detach之后，及时上报parentRecyclerView
            val velocityY = getVelocityY()
            if (velocityY < 0 && computeVerticalScrollOffset() == 0 && isScrollTop()) {
                parentRecyclerView?.fling(0, velocityY)
            }
        }
    }

    fun isScrollTop(): Boolean {
        //RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
        return !canScrollVertically(-1)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            // ACTION_DOWN 触摸按下，保存临时变量
            dragState = DRAG_IDLE
            downX = ev.rawX
            downY = ev.rawY
            this.stopFling()
        } else if (ev.action == MotionEvent.ACTION_MOVE) {
            // ACTION_MOVE 判定垂直还是水平滑动
            //获取到距离差
            val xDistance = Math.abs(ev.rawX - downX)
            val yDistance = Math.abs(ev.rawY - downY)
            if (xDistance > yDistance && xDistance > mTouchSlop) {
                // 水平滑动
                return true
            } else if (xDistance == 0f && yDistance == 0f) {
                // 点击
                return super.onInterceptTouchEvent(ev)
            } else if (yDistance >= xDistance && yDistance > 8f) {
                // 垂直滑动
                return true
            }
        }
//        Log.e("super.onInter",super.onInterceptTouchEvent(ev).toString())
        return super.onInterceptTouchEvent(ev)
    }

    /**
     * 这段逻辑主要是RecyclerView最底部，垂直上拉后居然还能左右滑动，不能忍
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            // 一上来就禁止ParentRecyclerView拦截Touch事件
            parent.requestDisallowInterceptTouchEvent(true)
        } else if (ev.action == MotionEvent.ACTION_MOVE) {
            // ACTION_MOVE 判定垂直还是水平滑动
//            if (dragState == DRAG_IDLE) {
            //获取到距离差
            val xDistance = Math.abs(ev.rawX - downX)
            val yDistance = Math.abs(ev.rawY - downY)

            if (xDistance > yDistance && xDistance > mTouchSlop) {
                // 水平滑动
                dragState = DRAG_HORIZONTAL
                // touch事件允许 ViewPager / ViewPager2 处理
                parent.requestDisallowInterceptTouchEvent(false)
            } else if (yDistance > xDistance && yDistance > mTouchSlop) {
                // 垂直滑动
                dragState = DRAG_VERTICAL
                parent.requestDisallowInterceptTouchEvent(true)
            }
//            }
        }
        return super.onTouchEvent(ev)
    }

    //用户可以操作界面了
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        connectToParent()
    }

    /**
     * 跟ParentView建立连接
     * 1. 将放置了ChildRecyclerView的布局View报告给ParentRecyclerView
     * 2. 遍历 childRecycleView 的 parentView,通过tag将自己(childRecycleView)关联到currentItem的View中
     * 2. 将ViewPager/ViewPager2报告给ParentRecyclerView
     */
    private fun connectToParent() {
        var viewPager: ViewPager? = null
        var viewPager2: ViewPager2? = null
        var lastTraverseView: View = this

        var parentView: View? = this.parent as View
        //遍历所有的 childRecycleView 的 parentView
        while (parentView != null) {
            val parentClassName = parentView::class.java.canonicalName
            if ("androidx.viewpager2.widget.ViewPager2.RecyclerViewImpl" == parentClassName) {
                //假设 ViewPager里面的Page是FrameLayout+ChildRecyclerView,这里就是给FrameLayout设置TAG
                //然后TAG的值就是ChildRecyclerView,ParentRecyclerView就能找到ChildRecyclerView了
                lastTraverseView.setTag(R.id.tag_saved_child_recycler_view, this)
            } else if (parentView is ViewPager) {
                if (lastTraverseView != this) {
                    // 这个tag会在ParentRecyclerView中用到
                    lastTraverseView.setTag(R.id.tag_saved_child_recycler_view, this)
                }
                // 碰到ViewPager，需要上报给ParentRecyclerView
                viewPager = parentView
            } else if (parentView is ViewPager2) {
                // 碰到ViewPager2，需要上报给ParentRecyclerView
                viewPager2 = parentView
            } else if (parentView is ParentRecyclerView) {
                // 碰到ParentRecyclerView，设置结束
                parentView.setInnerViewPager(viewPager)
                parentView.setInnerViewPager2(viewPager2)
                parentView.setChildPagerContainer(lastTraverseView)
                this.parentRecyclerView = parentView
                return
            }

            lastTraverseView = parentView
            parentView = parentView.parent as View
        }
    }

    override fun dispatchTouchEvent(e: MotionEvent): Boolean {
        val x = e.rawX
        val y = e.rawY
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                //将按下时的坐标存储
                downX = x
                downY = y
                // true 表示让ParentRecyclerView不要拦截
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                //获取到距离差
                val dx: Float = x - downX
                val dy: Float = y - downY
                //通过距离差判断方向
                val orientation = getOrientation(dx, dy)
                val location = intArrayOf(0, 0)
                getLocationOnScreen(location)
                when (orientation) {
                    "down" ->
                        if (!isScrollTop()) {
                            // 如果向下滑动时让ParentRecyclerView不要拦截
                            parent.requestDisallowInterceptTouchEvent(true)
                        } else {
                            // 内层RecyclerView下拉到最顶部时
                            if (dy < 24f) {
                                // 如果滑动的距离小于这个值依然让Parent不拦截
                                parent.requestDisallowInterceptTouchEvent(true)
                            } else {
                                // 将滑动事件抛给Parent，这样可以随着Parent一起滑动
                                parent.requestDisallowInterceptTouchEvent(false)
                            }
                        }
                    "up" -> {
                        // 向上滑动时，始终由ChildRecyclerView处理
                        parent.requestDisallowInterceptTouchEvent(true)
                    }
                    "right" -> {
//                        Log.e("ChildRecyclerView", "r  不要拦截")
//                        parent.requestDisallowInterceptTouchEvent(true)
                    }
                    "left" -> {
//                        Log.e("ChildRecyclerView", "l  不要拦截")
//                        parent.requestDisallowInterceptTouchEvent(true)
                    }
                }
            }
        }
        return super.dispatchTouchEvent(e)
    }

    private fun getOrientation(dx: Float, dy: Float): String {
        return if (abs(dx) > abs(dy)) {
            //X轴移动
            if (dx > 0) "right" else "left" //右,左
        } else {
            //Y轴移动
            if (dy > 0) "down" else "up" //下//上
        }
    }

}