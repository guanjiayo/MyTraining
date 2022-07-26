package zs.xmx.hi.training.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import zs.xmx.hi.training.MyApplication


/**
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:   方案二,结合BaseActivity增加通用性
 *
 */
open class BaseViewModelActivity : AppCompatActivity() {

    private var mApplicationProvider: ViewModelProvider? = null

    protected open fun <T : ViewModel> getApplicationScopeViewModel(modelClass: Class<T>): T {
        if (mApplicationProvider == null) {
            mApplicationProvider = ViewModelProvider(this.applicationContext as MyApplication)
        }
        return mApplicationProvider!![modelClass]
    }

}