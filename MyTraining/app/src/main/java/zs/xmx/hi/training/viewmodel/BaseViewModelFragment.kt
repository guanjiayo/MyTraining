package zs.xmx.hi.training.viewmodel

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import zs.xmx.hi.training.MyApplication


/**
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:   方案二, Fragment 方式结合BaseFragment
 *
 */
open class BaseViewModelFragment : Fragment() {

    protected var mActivity: AppCompatActivity? = null
    private var mApplicationProvider: ViewModelProvider? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    protected fun <T : ViewModel> getApplicationScopeViewModel(modelClass: Class<T>): T {
        if (mApplicationProvider == null) {
            mApplicationProvider =
                ViewModelProvider(mActivity!!.applicationContext as MyApplication)
        }
        return mApplicationProvider!![modelClass]
    }
}