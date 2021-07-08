## 商城首页嵌套滑动方案:
> 目前用的是这个 RecyclerView嵌套滑动置顶 项目篇

### NestedScrollingParent2实现类 + 多类型RecycleView(最后一个item为TabLayout+ViewPager)
> NestedScrollView + RecycleView + TabLayout + ViewPager + RecyclerView 思路类似
1. TabLayout + ViewPager 部分布局设置充满屏幕来实现仿吸顶效果
2. 主页面布局定义NestedScrollingParent2实现类+RecycleView(主)
3. 在 RecycleView(主) 的Adapter定义两种itemType,普通的和TabLayout+ViewPager
4. 在NestedScrollingParent2实现类里面,同时拿到两个RecycleView,然后重写 onNestedPreScroll() ,
因为嵌套滑动会一直找父布局是否能嵌套滑动,我们的NestedScrollView既是父布局,
然后他也认为自己是子布局,因此重写 onNestedPreScroll() 这个找父布局的方法,然后根据场景,让他先滑,
不可见时,再让RecycleView滑
4. 实现惯性滑动(通过换算公式)

参考:
[嵌套滑动通用解决方案--NestedScrollingParent2](https://cloud.tencent.com/developer/article/1667334?from=article.detail.1805837)

---

### 两个RecyclerView嵌套滑动置顶
分析京东/天猫得出首页布局结构 RecycleView + TabLayout + ViewPager + RecycleView(不套FrameLayout小米商城除外)
基本能得出两个RecycleView嵌套是比较合适的方案
1. TabLayout + ViewPager 部分布局需要充满屏幕才能吸顶
2. 在主页面定义ParentRecyclerView,在Adapter定义TabLayout+ViewPager的item
3. 同时在Parent的Adapter处理TabLayout选中时的逻辑
4. loadMore在各自的RecycleView进行处理
5. ParentRecyclerView监听滑动到底部时,将滑动速率传递给ChildRecyclerView(Fling完美从Parent滑动到Child)
6. ChildRecyclerView反之,然后处理一下事件拦截


参考:
[RecyclerView嵌套滑动置顶 项目篇](https://cloud.tencent.com/developer/article/1805837?from=article.detail.1730472)


