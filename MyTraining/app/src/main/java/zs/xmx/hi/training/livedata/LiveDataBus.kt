package zs.xmx.hi.training.livedata

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.ConcurrentHashMap


/**
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:   LiveData封装,避免粘性事件
 *
 */
object LiveDataBus {

    private val eventMap = ConcurrentHashMap<String, StickyLiveData<*>>()

    /**
     * 基于事件名称 订阅/分发消息
     * 由于一个livedata 只能发送 一种数据类型
     * 因此 不同的event事件,需要使用不同的livedata实例去分发
     */
    fun <T> with(eventName: String): StickyLiveData<T> {
        var liveData = eventMap[eventName]
        if (liveData == null) {
            liveData = StickyLiveData<T>(eventName)
            eventMap[eventName] = liveData
        }
        return liveData as StickyLiveData<T>
    }

    class StickyLiveData<T>(private val eventName: String) : LiveData<T>() {
        internal var mStickyData: T? = null
        internal var mVersion = 0

        fun setStickyData(stickyData: T) {
            mStickyData = stickyData
            //就是在主线程去发送数据
            setValue(stickyData)
        }

        fun postStickyData(stickyData: T) {
            mStickyData = stickyData
            //不受线程的限制
            postValue(stickyData)
        }

        override fun setValue(value: T) {
            mVersion++
            super.setValue(value)
        }

        override fun postValue(value: T) {
            mVersion++
            super.postValue(value)
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            observerSticky(owner, false, observer)
        }

        /**
         * 对外暴露的observer() 方法
         */
        fun observerSticky(owner: LifecycleOwner, sticky: Boolean, observer: Observer<in T>) {
            //允许指定注册的观察则 是否需要关心黏性事件
            //sticky =true, 如果之前存在已经发送的数据，那么这个observer会受到之前的黏性事件消息
            owner.lifecycle.addObserver(LifecycleEventObserver { source, event ->
                //监听 宿主 发生销毁事件，主动把livedata 移除掉。

                /*
                   这种移除方式对粘性事件有影响,场景
                   Activity A先发消息，进入Activity B，此时Activity B能够收到消息，
                   但是LiveDataBus监听了Activity B宿主生命周期，当Activity B销毁时，LiveData也一并被移除了。
                   此时再进入Activity C则无法收到粘性消息了。
                   解决: 对宿主生命周期的监听去除,由分发消息的页面选择何时移除LiveData实例
                 */
                if (event == Lifecycle.Event.ON_DESTROY) {
                    eventMap.remove(eventName)
                }
            })
            super.observe(owner, StickyObserver(this, sticky, observer))
        }

        class StickyObserver<T>(
            private val stickyLiveData: StickyLiveData<T>,
            private val sticky: Boolean,
            private val observer: Observer<in T>
        ) : Observer<T> {
            //lastVersion 和livedata的version 对齐的原因，就是为控制黏性事件的分发。
            //sticky 不等于true , 只能接收到注册之后发送的消息，如果要接收黏性事件，则sticky需要传递为true
            private var lastVersion = stickyLiveData.mVersion
            override fun onChanged(t: T) {
                Log.i(
                    "LiveDataTest4Activity",
                    "onChanged: lastVersion:  $lastVersion --- mVersion: ${stickyLiveData.mVersion} "
                )
                //判断说明stickyLiveData  没有更新的数据需要发送。
                //observer的数据和LiveData的数据一样,没有最新的了,与源码类似
                if (lastVersion >= stickyLiveData.mVersion) {
                    if (sticky && stickyLiveData.mStickyData != null) {
                        observer.onChanged(stickyLiveData.mStickyData)
                    }
                    return
                }

                lastVersion = stickyLiveData.mVersion
                observer.onChanged(t)
            }

        }


    }

}