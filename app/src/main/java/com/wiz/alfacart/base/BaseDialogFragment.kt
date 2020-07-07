package com.wiz.alfacart.base

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber
import wazma.punjabi.helper.CoroutineUtil
import kotlin.coroutines.CoroutineContext

abstract class BaseDialogFragment: DialogFragment(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = CoroutineUtil.getErrorHandler() + SupervisorJob()

    abstract fun getTAG():String

    private var TAG=javaClass.simpleName

    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!getTag().isNullOrEmpty()) {
            TAG = getTag()!!
        }

        Timber.i("onCreate()")
    }

    override fun onStop() {
        super.onStop()

        Timber.i("onStop()")
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    class VerticalSpaceItemDecoration(private val verticalSpaceHeight: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.bottom = verticalSpaceHeight
        }
    }
}