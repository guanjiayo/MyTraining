package zs.xmx.hi.training

import android.app.Application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner


/**
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:
 *
 */
class MyApplication : Application(), ViewModelStoreOwner {

    //    private val appViewModelStore: ViewModelStore by lazy {
//        ViewModelStore()
//    }
    private lateinit var mAppViewModelStore: ViewModelStore

    override fun onCreate() {
        super.onCreate()
        mAppViewModelStore = ViewModelStore()
    }

    override fun getViewModelStore(): ViewModelStore {
        //return appViewModelStore
        return mAppViewModelStore
    }
}