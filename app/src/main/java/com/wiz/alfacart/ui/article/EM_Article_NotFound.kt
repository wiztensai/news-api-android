package com.wiz.alfacart.ui.article

import android.view.View
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.wiz.alfacart.R

@EpoxyModelClass(layout = R.layout.item_empty_article)
abstract class EM_Article_NotFound : EpoxyModelWithHolder<EM_Article_NotFound.LoadingModelViewHolder>() {
    override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int) = totalSpanCount

    class LoadingModelViewHolder : EpoxyHolder() {
        override fun bindView(itemView: View) {
            // nothing to bind, just display the layout
        }
    }
}