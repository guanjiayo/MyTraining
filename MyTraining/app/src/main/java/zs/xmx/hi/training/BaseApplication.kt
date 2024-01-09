package zs.xmx.hi.training

import android.app.Application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner


/**
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:  方式二,自定义 Application 继承 ViewModelStoreOwner 接口
 *
 */
open class BaseApplication : Application(), ViewModelStoreOwner {

    private val appViewModelStore: ViewModelStore by lazy {
        ViewModelStore()
    }

    override val viewModelStore: ViewModelStore
        get() = appViewModelStore
}