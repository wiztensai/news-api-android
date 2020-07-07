package com.wiz.alfacart.base

import android.app.Application
import android.util.Log
import com.airbnb.epoxy.EpoxyController
import com.bumptech.glide.GlideBuilder
import com.wiz.alfacart.BuildConfig
import com.wiz.alfacart.R
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import timber.log.Timber

class BaseApp: Application() {

    val TAG = "BaseApp"

    companion object {
        lateinit var sInstance: BaseApp
    }

    override fun onCreate() {
        super.onCreate()
        sInstance = this

        EpoxyController.setGlobalExceptionHandler { controller, exception ->
            Timber.e(exception)
        }

        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("OpenSans-Regular.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )

        if (BuildConfig.BUILD_TYPE.equals("debug", true)){
            Log.d(TAG, "timber ON")
            Timber.plant(Timber.DebugTree())
        } else {
            Log.d(TAG, "timber OFF")
        }
    }
}