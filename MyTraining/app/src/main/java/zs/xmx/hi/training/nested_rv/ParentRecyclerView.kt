package zs.xmx.hi.training.nested_rv

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.NestedScrollingParent3
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import zs.xmx.hi.training.R


/**
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:   外层RecyclerView
 *
 */
class ParentRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseNestedRecyclerView(context, attrs, defStyleAttr), NestedScrollingParent3 {

    private var childPagerContainer: View? = null

    private var innerViewPager: ViewPager? = null

    private var innerViewPager2: ViewPager2? = null

    private var doNotInterceptTouchEvent: Boolean = false

    private var stickyListener: ((isAtTop: Boolean) -> Unit)? = null

    private var innerIsStickyTop = false

    /**
     * 顶部悬停的高度
     */
    private var stickyHeight = 0

    init {
        //设置不允许过渡滑动该视图
        this.overScrollMode = View.OVER_SCROLL_NEVER
        //当FOCUS_BLOCK_DESCENDANTS时，仅ViewGroup自身尝试获取焦点
        //当FOCUS_BEFORE_DESCENDANTS时，先ViewGroup自身尝试获取焦点，获取焦点失败后遍历子view尝试获取焦点
        //当FOCUS_AFTER_DESCENDANTS时，先遍历子view尝试获取焦点，当没有子view获取焦点时ViewGroup自身尝试获取焦点
        this.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
    }

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        if (e!!.action == MotionEvent.ACTION_DOWN) {
            val childRecyclerView = findChildRecyclerView()

            // 1. 是否禁止拦截
            doNotInterceptTouchEvent = doNotInterceptTouch(e.rawY, childRecyclerView)

            // 2. 停止Fling
            this.stopFling()
            childRecyclerView?.stopFling()
        }

        return if (doNotInterceptTouchEvent) {
            false
        } else {
            super.onInterceptTouchEvent(e)
        }
    }

    /**
     * 是否禁止拦截TouchEvent事件
     */
    private fun doNotInterceptTouch(rawY: Float, childRecyclerView: ChildRecyclerView?): Boolean {
        if (null != childRecyclerView &&
            null != childPagerContainer &&
            ViewCompat.isAttachedToWindow(childPagerContainer!!)
        ) {
            val coorValue = IntArray(2)
            childRecyclerView.getLocationOnScreen(coorValue)

            val childRecyclerViewY = coorValue[1]
            if (rawY > childRecyclerViewY) {
                return true
            }

            if (childPagerContainer!!.top == stickyHeight) {
                return true
            }
        }

        // 默认不禁止
        return false
    }

    /**
     * 在嵌套滑动的子View未滑动之前，判断父view是否优先与子view处理(也就是父view可以先消耗，然后给子view消耗）
     *
     * @param target   具体嵌套滑动的那个子类
     * @param dx       水平方向嵌套滑动的子View想要变化的距离
     * @param dy       垂直方向嵌套滑动的子View想要变化的距离 dy<0向下滑动 dy>0 向上滑动
     * @param consumed 这个参数要我们在实现这个函数的时候指定，回头告诉子View当前父View消耗的距离
     *                 consumed[0] 水平消耗的距离，consumed[1] 垂直消耗的距离 好让子view做出相应的调整
     * @param type     滑动类型，ViewCompat.TYPE_NON_TOUCH fling效果,ViewCompat.TYPE_TOUCH 手势滑动
     */
    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        if (target is ChildRecyclerView) {
            var consumeY = dy
            val childScrollY = target.computeVerticalScrollOffset()
            //TabLayout 以上布局显示时
            if (childPagerContainer!!.top > stickyHeight) {
                if (childScrollY > 0 && dy < 0) {
                    //操作 childRecyclerView 下滑时赋值
                    consumeY = 0
                } else if (childPagerContainer!!.top - dy < stickyHeight) {
                    //操作 childRecyclerView 上滑临界点赋值
                    consumeY = childPagerContainer!!.top - stickyHeight
                }
                //TabLayout 以下布局显示时
            } else if (childPagerContainer!!.top == stickyHeight) {
                consumeY = if (-dy < childScrollY) {
                    //操作 childRecyclerView  上滑时赋值
                    0
                } else {
                    //操作 childRecyclerView 下滑临界点赋值
                    dy + childScrollY
                }
            }
            //通知一下已消耗的距离,不然TopView也会跟着滑动
            if (consumeY != 0) {
                consumed[1] = consumeY
                this.scrollBy(0, consumeY)
            }
        }
    }

    override fun onScrollStateChanged(state: Int) {
        if (state == SCROLL_STATE_IDLE) {
            val velocityY = getVelocityY()
            if (velocityY > 0 && isScrollEnd()) {
                // 滑动到最底部时，骤然停止，这时需要把速率传递给ChildRecyclerView
                val childRecyclerView = findChildRecyclerView()
                childRecyclerView?.fling(0, velocityY)
            }
        }
    }

    private fun isScrollEnd(): Boolean {
        //RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部
        return !canScrollVertically(1)
    }

    /**
     * 开始NestedScroll时调用，返回true就意味着后面可以接受到NestedScroll事件，否则就无法接收。
     *
     * @param child            该view的直接子view
     * @param target           发出NestedScroll事件的子view，和child不一定是同一个
     * @param axes 滑动的方向，为ViewCompat#SCROLL_AXIS_HORIZONTAL或者ViewCompat#SCROLL_AXIS_VERTICAL，亦或两者都有。
     * @return 返回true代表要消耗这个NestedScroll事件，否则就是false。
     */
    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        return target is ChildRecyclerView
    }

    /**
     * 在target滑不动的时候会调用这个方法，这时就通知本view可以进行滑动。如果目标view可以一直滑动，那么这个方法就不会被调用
     *
     * @param target       发出NestedScroll事件的子view
     * @param dxConsumed   target在x方向上已经消耗的滑动距离
     * @param dxUnconsumed 这次滑动事件在x方向除去target已经消耗的还剩下的距离，通常如果我们需要滑动的话就使用这个值。
     * @param dyConsumed   同上
     * @param dyUnconsumed 同上
     */
    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        // do nothing
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        // do nothing
    }

    /**
     * 在onStartNestedScroll之后调用，参数意义和onStartNestedScroll一样，可什么都不做
     */
    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        // do nothing
    }

    /**
     * 结束NestedScroll事件时调用，可什么都不做
     */
    override fun onStopNestedScroll(target: View, type: Int) {
        // do nothing
    }

    /**
     * 获取当前的ChildRecyclerView
     */
    private fun findChildRecyclerView(): ChildRecyclerView? {
        if (innerViewPager != null) {
            val currentItem = innerViewPager!!.currentItem
            for (i in 0 until innerViewPager!!.childCount) {
                val itemChildView = innerViewPager!!.getChildAt(i)
                val layoutParams = itemChildView.layoutParams as ViewPager.LayoutParams
                val positionField = layoutParams.javaClass.getDeclaredField("position")
                positionField.isAccessible = true
                val position = positionField.get(layoutParams) as Int

                if (!layoutParams.isDecor && currentItem == position) {
                    if (itemChildView is ChildRecyclerView) {
                        return itemChildView
                    } else {
                        val tagView = itemChildView?.getTag(R.id.tag_saved_child_recycler_view)
                        if (tagView is ChildRecyclerView) {
                            return tagView
                        }
                    }
                }
            }
        } else if (innerViewPager2 != null) {
            //ViewPager2内部原理就是RecyclerView+LinearLayoutManager因此用这种方式找到ChildRecyclerView
            val layoutManagerFiled = ViewPager2::class.java.getDeclaredField("mLayoutManager")
            layoutManagerFiled.isAccessible = true
            val pagerLayoutManager = layoutManagerFiled.get(innerViewPager2) as LinearLayoutManager
            val currentChild = pagerLayoutManager.findViewByPosition(innerViewPager2!!.currentItem)

            if (currentChild is ChildRecyclerView) {
                return currentChild
            } else {
                val tagView = currentChild?.getTag(R.id.tag_saved_child_recycler_view)
                if (tagView is ChildRecyclerView) {
                    return tagView
                }
            }
        }
        return null
    }

    fun setInnerViewPager(viewPager: ViewPager?) {
        this.innerViewPager = viewPager
    }

    fun setInnerViewPager2(viewPager2: ViewPager2?) {
        this.innerViewPager2 = viewPager2
    }

    /**
     * 由ChildRecyclerView上报ViewPager(ViewPager2)的父容器，用做内联滑动逻辑判断，及Touch拦截等
     */
    fun setChildPagerContainer(childPagerContainer: View) {
        if (this.childPagerContainer != childPagerContainer) {
            this.childPagerContainer = childPagerContainer
            this.post {
                adjustChildPagerContainerHeight()
            }
        }
    }


    /**
     * 调整Child容器的高度为父容器高度，使之填充布局，避免父容器滚动后出现空白
     * ViewPager + 子View 的高度
     */
    private fun adjustChildPagerContainerHeight() {
        if (null != childPagerContainer) {
            val layoutParams = childPagerContainer!!.layoutParams
            //height是当时屏幕的大小,measuredHeight是View实际大小,与屏幕无关
            //measuredHeight可以超出当前显示的屏幕外
            val newHeight = this.height - stickyHeight
            if (newHeight != layoutParams.height) {
                layoutParams.height = newHeight
                childPagerContainer!!.layoutParams = layoutParams
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    // 吸顶回调 (回调是否置顶) start
    //----------------------------------------------------------------------------------------------

    /**
     * Activity调用方法
     */
    fun setStickyHeight(stickyHeight: Int) {
        val scrollOffset = this.stickyHeight - stickyHeight
        this.stickyHeight = stickyHeight
        this.adjustChildPagerContainerHeight()

        this.post {
            this.scrollBy(0, scrollOffset)
        }
    }

    fun setStickyListener(stickyListener: (isAtTop: Boolean) -> Unit) {
        this.stickyListener = stickyListener
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)

        var currentStickyTop = false
        //ViewPager的父容器(TabLayout+ViewPager以下的布局)吸顶时
        if (childPagerContainer != null && childPagerContainer?.top == stickyHeight) {
            currentStickyTop = true
        }

        if (currentStickyTop != innerIsStickyTop) {
            innerIsStickyTop = currentStickyTop
            //这种写法就是给回调传参
            stickyListener?.invoke(innerIsStickyTop)
        }
    }

    //----------------------------------------------------------------------------------------------
    // 吸顶回调  end
    //----------------------------------------------------------------------------------------------

    /**
     * 在target判断为fling并且执行fling之前调用，我们可以通过返回true来拦截目标的fling，这样它就不会执行滑动。
     *
     * @param target    目标view
     * @param velocityX 在x方向的起始速度
     * @param velocityY 在y方向的起始速度
     * @return 我们是否消耗此次fling，返回true代表拦截，返回false，目标view就进行正常的fling
     */
    @SuppressLint("ObsoleteSdkInt")
    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        try {
            //兼容Android 4.4 惯性滑动崩溃处理(API不兼容问题)
            if (android.os.Build.VERSION.SDK_INT <= 19) {
                return true
            }
            return super.onNestedPreFling(target, velocityX, velocityY)
        } catch (e: Exception) {
            return true
        }
    }
}