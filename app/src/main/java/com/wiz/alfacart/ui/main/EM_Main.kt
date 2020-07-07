package com.wiz.alfacart.ui.main

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
import com.wiz.alfacart.domain.CategoryModel

@EpoxyModelClass(layout = R.layout.item_main)
abstract class EM_Main(var context: Context) : EpoxyModelWithHolder<EM_Main.Holder>() {

    @EpoxyAttribute
    lateinit var categoryModel: CategoryModel

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.tvTitle.setText(categoryModel.title.capitalize())
        Glide.with(context).load(categoryModel.imageRes).into(holder.imageView)
    }

    inner class Holder : EpoxyHolderUtil() {
        val tvTitle by bind<TextView>(R.id.tvName)
        val imageView by bind<ImageView>(R.id.imageView)
        val containerLayout by bind<View>(R.id.containerLayout)
    }
}
