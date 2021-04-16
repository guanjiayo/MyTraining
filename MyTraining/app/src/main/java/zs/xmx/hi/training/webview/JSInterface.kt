package zs.xmx.hi.training.webview

import android.util.Log
import android.webkit.JavascriptInterface

/**
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:   提供让 JS 使用的Java接口,由于这个方法在子线程执行,需要转到主线程
 *         这里定义了一个JSBridge接口
 *
 */
class JSInterface(private var mInstance: JSBridge) {

    private val TAG = "JSInterface"

    /**
     * 这个方法不在主线程
     */
    @JavascriptInterface
    fun setValue(value: String?) {
        mInstance.setTextViewValue(value)
    }

}