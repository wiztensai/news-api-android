package com.wiz.alfacart.ui.news_source

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.TypedEpoxyController
import com.wiz.alfacart.R
import com.wiz.alfacart.domain.NetworkState
import com.wiz.alfacart.domain.NewsSourceModel

class EC_NewsSource(var context: Context, var networkState: NetworkState?, var listener: MCallback)
    : TypedEpoxyController<MutableList<NewsSourceModel>>() {

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED && networkState != NetworkState.FAILED

    override fun buildModels(data: MutableList<NewsSourceModel>?) {
        data ?: return

        // INIT LOADING
        if (data.isEmpty() && networkState!!.status != (NetworkState.FAILED.status)) {
            for (i in 0..2) {
                EM_NewsSource_Loading_()
                    .id("IL$i")
                    .addTo(this)
            }
        }

        // CONTENT
        for (i in 0..data.size-1) {
            EM_NewsSource_(context)
                .id(data[i].id)
                .model(data[i])
                .onBind { model, view, position ->
                    view.containerLayout.setOnClickListener {
                        listener.onLayoutClick(position, model.model, view)
                    }
                }
                .addTo(this)
        }
    }

    interface MCallback {
        fun onLayoutClick(position: Int, model: NewsSourceModel, holder: EM_NewsSource.Holder)
    }
}