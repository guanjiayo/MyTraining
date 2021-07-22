package zs.xmx.hi.training.lifecycle

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner


/**
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:   一个类监听Activity或Fragment生命周期
 *         回调方案
 */
class LocationObserver : LifecycleEventObserver {

    private val tag: String = LocationObserver::class.java.simpleName

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                Log.i(tag, "onCreate")
            }
            Lifecycle.Event.ON_START -> {
                Log.i(tag, "onStart")
            }
            Lifecycle.Event.ON_RESUME -> {
                Log.i(tag, "onResume")
            }
            Lifecycle.Event.ON_PAUSE -> {
                Log.i(tag, "onPause")
            }
            Lifecycle.Event.ON_STOP -> {
                Log.i(tag, "onStop")
            }
            Lifecycle.Event.ON_DESTROY -> {
                Log.i(tag, "onDestroy")
            }
            else -> {
                Log.i(tag, "Other")
            }
        }
    }
}