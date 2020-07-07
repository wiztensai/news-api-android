package com.wiz.alfacart.ui.news_source

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.airbnb.epoxy.kotlinsample.helpers.EpoxyHolderUtil
import com.bumptech.glide.Glide
import com.wiz.alfacart.R
import com.wiz.alfacart.domain.NewsSourceModel

@EpoxyModelClass(layout = R.layout.item_news_source)
abstract class EM_NewsSource(var context: Context) : EpoxyModelWithHolder<EM_NewsSource.Holder>() {

    @EpoxyAttribute
    lateinit var model: NewsSourceModel

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.tvTitle.setText(model.name)
        holder.tvDesc.setText(model.description)
    }

    class Holder : EpoxyHolderUtil() {
        val tvTitle by bind<TextView>(R.id.tvName)
        val tvDesc by bind<TextView>(R.id.tvDesc)
        val containerLayout by bind<View>(R.id.containerLayout)
    }
}
