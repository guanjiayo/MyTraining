1. 笔记里面的页面不可见时,发送Handler消息
2. postValue()和setValue()使用区别,还有多次postValue()只能接受到最后一条演示
3. LiveData源码 changeActiveCounter()多次调用该方法,那个 mChangingActiveState 状态的变化
(目前结论是,多次调用时,前面的会return,卡住后面的,试试能不能实现该效果)
4. 粘性事件解决,LiveDataBus
5. postValue() 漏消息处理
6. A->B->C 跨多个页面时,粘性 LiveData 被销毁问题

实际开发,暂时建议Github拿stat比较多的那几个去使用