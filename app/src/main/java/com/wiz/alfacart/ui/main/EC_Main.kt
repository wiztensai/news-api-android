package com.wiz.alfacart.ui.main

import android.content.Context
import com.airbnb.epoxy.TypedEpoxyController
import com.wiz.alfacart.R
import com.wiz.alfacart.domain.CategoryModel
import com.wiz.alfacart.domain.NetworkState
import com.wiz.alfacart.ui.news_source.EM_NewsSource_Loading_

class EC_Main(var context: Context, var networkState: NetworkState?, var listener: MCallback): TypedEpoxyController<MutableList<CategoryModel>>() {

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED && networkState != NetworkState.FAILED

    override fun buildModels(data: MutableList<CategoryModel>?) {
        data?:return

        // CONTENT
        for (i in 0..data.size-1) {
            EM_Main_(context)
                .categoryModel(data[i])
                .id(i).onBind { model, view, position ->
                    view.containerLayout.setOnClickListener {
                        listener.onLayoutClick(position, model.categoryModel, view)
                    }
                }
                .addTo(this)
        }
    }

    interface MCallback {
        fun onLayoutClick(position:Int, model:CategoryModel, holder: EM_Main.Holder)
    }
}