package com.wiz.alfacart.ui.ext

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wazma.punjabi.base.BaseFragment

fun BaseFragment.setFabScrollListener(recyclerView: RecyclerView, viewFab:FloatingActionButton) {
    recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            // kenapa oneshoot seperti ini?
            // karena untuk mendapatkan findLastVisibleItemPosition dengan nilai yang benar
            // baru diketahui caranya dengan berada di dalam onScrolled ini
            // karena jika tidak maka akan selalu dapat -1 lastVisibleItemPositionnya
            recyclerView.removeOnScrollListener(this)
            processFabScrollListener(recyclerView, viewFab)
        }
    })
}

private fun BaseFragment.processFabScrollListener(recyclerView: RecyclerView, viewFab:FloatingActionButton) {

    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
    val initialLastPosition = layoutManager.findLastVisibleItemPosition()

    recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            coroutineScope.launch(Dispatchers.Default) {
                if (dy > 0 && viewFab.visibility == View.VISIBLE) {
                   hideFab(viewFab)
                }
                else if (dy < 0 && viewFab.visibility != View.VISIBLE) {
                    val lastVisiblePos = layoutManager.findLastVisibleItemPosition()
                    if (lastVisiblePos > initialLastPosition) {
                        showFab(viewFab)
                    } else {
                        hideFab(viewFab)
                    }
                }
            }
        }
    })
}

private fun BaseFragment.showFab(viewFab:FloatingActionButton) {
    coroutineScope.launch {
        viewFab.show()
    }
}

private fun BaseFragment.hideFab(viewFab:FloatingActionButton) {
    coroutineScope.launch {
        viewFab.hide()
    }
}