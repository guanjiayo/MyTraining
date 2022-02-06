package zs.xmx.hi.training.viewmodel

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore


/**
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:   https://blog.csdn.net/Mr_Tony (ViewMode的使用(五)-全局ViewModel)
 *
 */
//一个全局的ViewModel
@MainThread
public inline fun <reified VM : ViewModel> ComponentActivity.applicationViewModels(
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: {
        defaultViewModelProviderFactory
    }
    return ViewModelLazy(VM::class, { mViewModelStore }, factoryPromise)
}

val mViewModelStore: ViewModelStore by lazy {
    ViewModelStore()
}


