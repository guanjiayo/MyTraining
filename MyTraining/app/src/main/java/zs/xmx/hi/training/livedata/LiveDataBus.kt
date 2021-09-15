package zs.xmx.hi.training.livedata

import android.util.Log
import androidx.lifecycle.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.set


/**
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:   LiveData封装,
 *         1. 避免粘性事件
 *         2. 实现了事件总线功能(其实就是用的粘性事件和forever)
 *
 */
@Suppress("UNCHECKED_CAST", "unused")
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

        //postValue最终其实还是执行setValue方式
        /*override fun postValue(value: T) {
            mVersion++
            super.postValue(value)
        }*/

        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            observerSticky(owner, false, observer)
        }

        /**
         * 对外暴露的observer() 方法
         */
        fun observerSticky(owner: LifecycleOwner, sticky: Boolean, observer: Observer<in T>) {
            //Log.e("跨页面测试","observerSticky()  owner: ${owner.javaClass.simpleName}")
            //允许指定注册的观察则 是否需要关心黏性事件
            //sticky =true, 如果之前存在已经发送的数据，那么这个observer会收到之前的黏性事件消息
            //如果是粘性事件,仿照EventBus谁观察就谁接收
            if (!sticky) {
                owner.lifecycle.addObserver(LifecycleEventObserver { source, event ->
                    //监听 宿主 发生销毁事件，主动把livedata 移除掉。
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        Log.e("跨页面测试", "$event  ${source.javaClass.simpleName}")
                        eventMap.remove(eventName)
                    }
                })
            }
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