package com.wiz.alfacart.ui

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import com.jaeger.library.StatusBarUtil
import com.wiz.alfacart.R
import com.wiz.alfacart.base.CONST
import com.wiz.alfacart.ui.ext.navReplaceTo
import com.wiz.alfacart.ui.main.F_Main
import com.wiz.alfacart.util.KeyboardUtil
import com.wiz.alfacart.util.SpesificInFragmentUtil
import wazma.punjabi.base.BaseActivity

class A_Main : BaseActivity() {

    override fun getTag(): String {
        return javaClass.simpleName
    }

    companion object {
        var atFragment = ""
    }

    private var backpressedTime: Long = 0
    private val PERIOD: Long = 2000
    val toastExit by lazy {
        Toast.makeText(this, "Press once again to exit", Toast.LENGTH_SHORT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_main)

        KeyboardUtil.hideKeyboard(findViewById(R.id.navFragment))

        SpesificInFragmentUtil(this, spesificInFragmentListener)
        navReplaceTo(F_Main(), CONST.TAG_MAIN)
    }

    override fun setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, null)
    }

    val spesificInFragmentListener = object : SpesificInFragmentUtil.SpesificInFragmentInterface {
        override fun onSpesificFragment() {
            StatusBarUtil.setLightMode(this@A_Main)
        }

        override fun onCommonFragment() {
            StatusBarUtil.setDarkMode(this@A_Main)
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount <= 1) {
            if (backpressedTime + PERIOD > System.currentTimeMillis()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask()
                } else {
                    finishAffinity()
                }
            }
            else toastExit.show()

            backpressedTime = System.currentTimeMillis()
        } else {
            super.onBackPressed()
        }
    }
}