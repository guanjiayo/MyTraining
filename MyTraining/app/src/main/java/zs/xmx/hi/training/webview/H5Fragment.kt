package zs.xmx.hi.training.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import zs.xmx.hi.training.R


/**
 * Author: 默小铭
 * Blog:   https://blog.csdn.net/u012792686
 * Desc:
 *
 */
class H5Fragment : Fragment(), JSBridge {

    private var mHanlder: Handler? = null
    private lateinit var mText: TextView
    private lateinit var mEditText: EditText
    private lateinit var mBtn: Button
    private lateinit var mWebView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_web, container, false)
        mWebView = view.findViewById(R.id.mWebView)
        mText = view.findViewById(R.id.textView)
        mEditText = view.findViewById(R.id.editText)
        mBtn = view.findViewById(R.id.btn)

        val webSettings = mWebView.settings
        webSettings.apply {
            javaScriptEnabled = true
        }

        mWebView.apply {
            addJavascriptInterface(JSInterface(this@H5Fragment), "javaFunc")
            loadUrl("file:///android_asset/index.html")
        }

        mBtn.setOnClickListener {
            val str = mEditText.text.toString()
            mWebView.loadUrl("javascript:if(window.remote){window.remote('${str}')}")
        }

        mHanlder = Handler()
        
        return view
    }

    override fun setTextViewValue(value: String?) {
        mHanlder?.post {
            mText.text = value
        }
    }

}