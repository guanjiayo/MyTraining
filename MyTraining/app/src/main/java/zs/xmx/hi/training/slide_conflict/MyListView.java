package zs.xmx.hi.training.slide_conflict;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

public class MyListView extends ListView {

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // 内部拦截法：子view处理事件冲突
    private float downX;
    private float downY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                Log.i("SlideConflict", "dispatchTouchEvent:   down");

                //让ViewGroup父类不拦截事件
                getParent().requestDisallowInterceptTouchEvent(true);
                downX = event.getX();
                downY = event.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                Log.i("SlideConflict", "dispatchTouchEvent:   move");

                //move
                float moveX = event.getX();
                float moveY = event.getY();

                float deltaX = downX - moveX;
                float deltaY = downY - moveY;
                if (Math.abs(deltaX) >= Math.abs(deltaY)) {
                    //左右滑动时,让父类ViewGroup拦截事件
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                //Log.i("SlideConflict", "dispatchTouchEvent:   up");
                break;
            }
            case MotionEvent.ACTION_CANCEL:
                //Log.i("SlideConflict", "dispatchTouchEvent:   cancel");
                break;
            default:
                break;
        }

        boolean a = super.dispatchTouchEvent(event);
        Log.i("SlideConflict", "dispatchTouchEvent:  a=  " + a);

        return a;
    }
}
