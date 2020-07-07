package com.wiz.alfacart.ui.news_source

import android.view.View
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.wiz.alfacart.R

@EpoxyModelClass(layout = R.layout.item_loading_newssource)
abstract class EM_NewsSource_Loading : EpoxyModelWithHolder<EM_NewsSource_Loading.LoadingModelViewHolder>() {

  override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int) = totalSpanCount

  class LoadingModelViewHolder : EpoxyHolder() {
    override fun bindView(itemView: View) {
      // nothing to bind, just display the layout
    }
  }
}