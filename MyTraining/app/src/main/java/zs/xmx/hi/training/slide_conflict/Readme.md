## View 和 ViewGroup 的关系
1. ViewGroup继承View          代码层面
2. ViewGroup是View的父布局     运行时角度

> 事件分发是按运行时角度来的

## 事件滑动流程
> 操作的是ViewGroup

第一个手指按下开始,最后一个手指抬起结束,中间会经历手指移动

## 事件分发流程

Activity从接收到Action_Down事件开始,DecorView(源码看到其实就是ViewGroup),开始向下分发到LinerLayout


> LinerLayout是因为系统桌面层级,顶部状态栏->我们的布局->虚拟按键这样类似的结构



---

### Test1
ScrollView 嵌套 RecycleView

现象:
RecycleView 把事件控住了,ScrollView没反应了

这是因为 RecycleView 继承了 NestedScrollView , ScrollView 没继承 NestedScrollView,
ScrollView 不能作为 RecycleView 的父类,事件拦截没监听到

### Test2
NestedScrollView 嵌套 RecycleView

现象:
整个布局能正常上下滑动了,但是TabLayout没停住

### Test3

现象:
NestedScrollView 将内部的 TabLayout + ViewPager 部分布局设置充满屏幕来实现仿吸顶效果

但是,滑动ViewPager里面的RecycleView又不能让外部NestedScrollView滑动了

### 最终方案:
1. NestedScrollView 将内部的 TabLayout + ViewPager 部分布局设置充满屏幕来实现仿吸顶效果
2. 重写 onNestedPreScroll() ,因为嵌套滑动会一直找父布局是否能嵌套滑动,我们的NestedScrollView既是父布局,
然后他也认为自己是子布局,因此重写 onNestedPreScroll() 这个找父布局的方法,然后根据场景,让他先滑,
不可见时,再让RecycleView滑
3. 实现惯性滑动(通过换算公式)

参考这两个老哥的RecycleView嵌套ViewPager+RecycleView完成最终方案
https://cloud.tencent.com/developer/article/1730472
https://github.com/JasonGaoH/NestedRecyclerView
