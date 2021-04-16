package zs.xmx.hi.training.webview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import zs.xmx.hi.training.R


class H5Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        val h5Fragment = H5Fragment()
        supportFragmentManager.beginTransaction().replace(
            R.id.mfl_content_container, h5Fragment
        ).commit()
    }


}

