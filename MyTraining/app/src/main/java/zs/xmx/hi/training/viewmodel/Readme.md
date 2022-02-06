## ViewModel

> 正常情况下,ViewModel只能在对应的Activity共享数据,不能跨activity

尝试过的解决方案

1. 在单例类定义LiveData(可行)

```
object UserInfo {
    val mUserModel = MutableLiveData<User>()
}
```

2. 学习课程在application类实现ViewModelStoreOwner

> 原理应该是对的,但是运行时无效,不知道啥原因

```
class MyApplication : Application(), ViewModelStoreOwner {

    private val appViewModelStore: ViewModelStore by lazy {
        ViewModelStore()
    }


    override fun getViewModelStore(): ViewModelStore {
        return appViewModelStore
    }
}

//使用
private val mViewModel = ViewModelProvider(MyApplication()).get(CounterViewModel::class.java)

//原理
ViewModelProvider(定义指定作用域的ViewModelStoreOwner),然后全局都可以调用
```

3. 网上别人的全局方案

> https://blog.csdn.net/Mr_Tony (ViewMode的使用(五)-全局ViewModel)

```
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

//使用

 private val mViewModel: CounterViewModel by applicationViewModels()
 
//或
 
 private val mViewModel: CounterViewModel by applicationViewModels{
    ViewModelProvider.AndroidViewModelFactory.getInstance(application)
 }
     

```

4. EventBus 
> 可行但不建议使用