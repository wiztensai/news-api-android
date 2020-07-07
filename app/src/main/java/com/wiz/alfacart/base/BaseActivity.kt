package wazma.punjabi.base

import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.jaeger.library.StatusBarUtil
import com.wiz.alfacart.R
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import wazma.punjabi.helper.CoroutineUtil
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity : AppCompatActivity() {

    val coroutineScope = CoroutineScope(
        Dispatchers.Main + SupervisorJob() + CoroutineUtil.getErrorHandler()
    )

    private var TAG: String = javaClass.simpleName
    abstract fun getTag(): String
    var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)

            if (!getTag().isNullOrEmpty()) {
                TAG = getTag()
            }

            Log.i(TAG, "onCreate()")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        setStatusBar()
    }

    fun Disposable.autoDispose() {
        compositeDisposable.add(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.i(TAG, "onNewIntent()")
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume()")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop()")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(TAG, "onRestart()")
    }


    override fun onDestroy() {
        Log.i(TAG, "onDestroy()")
        compositeDisposable.clear()

        coroutineScope.cancel()

        super.onDestroy()
    }

    override fun onBackPressed() {
        Log.i(TAG, "onBackPressed()")
        super.onBackPressed()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    // com.jaeger.statusbarutil, status bar custom untuk fragment
    protected open fun setStatusBar() {
        StatusBarUtil.setColor(this, resources.getColor(R.color.transparent))
    }
}