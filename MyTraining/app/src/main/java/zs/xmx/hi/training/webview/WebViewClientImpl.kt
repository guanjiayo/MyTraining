package zs.xmx.hi.training.webview

import android.annotation.TargetApi
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.webkit.*


/**
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:   WebViewClient实现类,  辅助 WebView 处理各种通知、请求事件等
 *
 */
class WebViewClientImpl : WebViewClient() {

    /**
     * 拦截当前webview加载的Url
     * 1. return true 当前webView处理
     * 2. return false 系统浏览器处理
     */
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        Log.i("WebView", "shouldOverrideUrlLoading: " + request?.url)
        return true
    }

    /**
     * 在webview开始加载页面的时候回调该方法
     */
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        //建议在这里实现本地显示的加载效果显示
    }

    /**
     * 在webview加载页面结束的时候回调该方法
     */
    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        syncCookie()
        //建议在这里实现本地显示的加载效果关闭

    }

    //获取浏览器cookie,然后保存到本地Sp中
    private fun syncCookie() {
        val manager = CookieManager.getInstance()
        /*
          注意，这里的Cookie和API请求的Cookie是不一样的，这个在网页不可见
         */
//        val webHost: String = Latte.getConfiguration(ConfigKeys.WEB_HOST)
//        if (webHost != null) {
//            if (manager.hasCookies()) {
//                val cookieStr = manager.getCookie(webHost)
//                if (cookieStr != null && cookieStr != "") {
//                    LattePreference.addCustomAppProfile("cookie", cookieStr)
//                }
//            }
//        }
    }

    // 旧版本，会在新版本中也可能被调用，所以加上一个判断，防止重复显示
    @Suppress("DEPRECATION")
    override fun onReceivedError(
        view: WebView?,
        errorCode: Int,
        description: String?,
        failingUrl: String?
    ) {
        super.onReceivedError(view, errorCode, description, failingUrl)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return
        }
        showErrorPage(view, errorCode)
    }

    // 新版本，只会在Android6及以上调用
    @TargetApi(Build.VERSION_CODES.M)
    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        if (request.isForMainFrame) { // 或者： if(request.getUrl().toString() .equals(getUrl()))
            showErrorPage(view,error?.errorCode)
        }
    }

    /**
     *  显示自定义错误页
     *  1. 显示一个本地的静态H5错误页面
     *  2. 自定义一个通用的错误页面使用
     */
    private fun showErrorPage(view: WebView?, errorCode: Int?) {

    }


}