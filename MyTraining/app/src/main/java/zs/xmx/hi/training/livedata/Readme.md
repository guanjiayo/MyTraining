1. 笔记里面的页面不可见时,发送Handler消息
2. postValue()和setValue()使用区别,还有多次postValue()只能接受到最后一条演示
   > postValue() 漏消息处理(自已重写以下这个方法,直接Handler推Runnable)
   > 有空同时写一下处理后的代码
3. LiveData源码 changeActiveCounter()多次调用该方法,那个 mChangingActiveState 状态的变化
(目前结论是,多次调用时,前面的会return,卡住后面的,试试能不能实现该效果)
4. 粘性事件解决,LiveDataBus
5. A->B->C->D 跨多个页面时,粘性 LiveData 被销毁问题
> 测试完发现这个场景,如果是每个页面跳转都发 LiveDataBus.with<String>("StickyDataNext").setStickyData("发送一条粘性事件") 没问题

```
A  LiveDataBus.with<String>("StickyDataNext").setStickyData("发送一条粘性事件")  跳转到B (不finish)

B接受粘性事件

B  LiveDataBus.with<String>("StickyDataNext").setStickyData("发送一条粘性事件")  跳转到C (finish)

C接受粘性事件  

C  LiveDataBus.with<String>("StickyDataNext").setStickyData("发送一条粘性事件")  跳转到D (finish)

D接受粘性事件

A->B  B正常收到事件

B->C 正常收到事件(打日志,日志是C先收到事件,B再onDestory()的),所以没问题

C->D 正常收到事件(虽然说我们代码移除了,但C->D发送,再用with发送,不是相当于又创建了一个新的LiveData,只是ebentName刚好和之前的一样罢了,所以没有问题)

if (event == Lifecycle.Event.ON_DESTROY) {

                    Log.e("跨页面测试", "$event  ${source.javaClass.simpleName}")

                    eventMap.remove(eventName)

                }
```  
场景二: 一个页面发送,多个页面接收
> 

```
A->B 发送粘性事件
B接受A的粘性事件
B->C 不发送粘性
C接受A的粘性事件

A->B finish 返回 A
然后 A->C,那么就接收不到 A发送的粘性事件了,因为我们这个判断将对应的eventName移除了
         if (event == Lifecycle.Event.ON_DESTROY) {           
                    eventMap.remove(eventName)
                }
```

实际开发,暂时建议Github拿stat比较多的那几个去使用

6. ViewPager + Fragment 结合使用 LiveDataBus 有可能有问题,看情况验证下 

#### LiveData 练手
[LiveData奇思妙用的 11 个场景总结~](https://mp.weixin.qq.com/s/013AABBND0XVXsOfY7dGuA)