package com.wiz.alfacart.ui.article_search

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.TypedEpoxyController
import com.wiz.alfacart.domain.ArticleModel
import com.wiz.alfacart.domain.NetworkState
import com.wiz.alfacart.ui.article.EM_Article
import com.wiz.alfacart.ui.article.EM_Article_
import com.wiz.alfacart.ui.article.EM_Article_Loading_
import com.wiz.alfacart.ui.article.EM_Article_NotFound_

class EC_ArticleSearch(var context: Context, var networkState: NetworkState?, var firstRequest:Boolean?, var listener: MCallback)
    : TypedEpoxyController<MutableList<ArticleModel>>() {

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED && networkState != NetworkState.FAILED

    override fun buildModels(data: MutableList<ArticleModel>?) {
        data ?: return

        EM_Article_NotFound_()
            .id("EANF")
            .addIf(networkState != null &&
                    networkState != NetworkState.LOADING &&
                    data.isEmpty() && firstRequest != null, this)

        // INIT LOADING
        if (data.isEmpty() && networkState!!.status != (NetworkState.FAILED.status)) {
            EM_Article_Loading_()
                .id("IL")
                .addTo(this)
        }

        // CONTENT
        for (i in 0..data.size-1) {

            EM_Article_(context)
                .id(i)
                .model(data[i])
                .onBind { model, view, position ->
                    view.containerLayout.setOnClickListener {
                        listener.onLayoutClick(position, model.model, view)
                    }
                }
                .addTo(this)
        }

        // MORE CONTENT LOADING
        EM_Article_Loading_()
            .id("MCL")
            .addIf(hasExtraRow() && data.isNotEmpty(), this)
    }

    interface MCallback {
        fun onLayoutClick(position: Int, model: ArticleModel, holder: EM_Article.Holder)
    }
}