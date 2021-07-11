## WindowManager实现悬浮窗

1. 直接对View设置传统动画无效
> 帧动画和补间动画(alpha, translate, scale, rotate)

```
ScaleAnimation animation = new ScaleAnimation(0.0f, 1f, 0.0f, 1f, Animation.ABSOLUTE, 100, Animation.ABSOLUTE, 100);
animation.setDuration(100);
view.setAnimation(animation);
animation.start();
mWindowManager.addView(defaultSplashLayout, lp);
```

这样设置,动画无效,原因是动画执行的条件是不能直接添加到最顶层的window,而是需要一个容器
如果添加一个容器，则只能对容器内的view进行动画设置，还是无法对容器进行动画设置。

```
class FloatView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var floatView =
        LayoutInflater.from(application).inflate(R.layout.layout_float_view, this) as View

}
```

2. 对WindowManager.LayoutParams的windowAnimations设置动画

```
params.windowAnimations = R.style.custom_style
```

这样设置,虽然整个View都会执行动画,但是动画只能写死在style.xml文件,无法进行动态设置

3. 对View使用属性动画
> 属性动画对最顶层的view是可以执行的。

参考资料:
[EasyFloat](https://www.jianshu.com/p/7d1a7c82094a)
[FloatWindow](https://www.jianshu.com/p/a3414ae63a7c)
[Android悬浮窗实现](https://blog.csdn.net/dongzhong1990/article/details/80512706)
