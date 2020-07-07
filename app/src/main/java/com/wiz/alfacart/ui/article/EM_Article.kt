package com.wiz.alfacart.ui.article

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.airbnb.epoxy.kotlinsample.helpers.EpoxyHolderUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.wiz.alfacart.R
import com.wiz.alfacart.domain.ArticleModel
import java.text.ParseException
import java.text.SimpleDateFormat


@EpoxyModelClass(layout = R.layout.item_article)
abstract class EM_Article(var context: Context) : EpoxyModelWithHolder<EM_Article.Holder>() {

    @EpoxyAttribute
    lateinit var model: ArticleModel

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.tvTitle.setText(model.title)

        try {
            val parseZuluFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
            val date = parseZuluFormat.parse(model.publishedAt)
            val displayFormat = SimpleDateFormat("EEE, dd/MM/yyyy")
            holder.tvDate.setText( displayFormat.format(date))
            holder.tvDate.isVisible = true
        } catch (e:ParseException) {
            holder.tvDate.isVisible = false
        }

        Glide.with(context)
            .load(model.urlToImage)
            .thumbnail(0.1f)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable?>?, isFirstResource: Boolean): Boolean {
                    holder.imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
                    holder.imageView.setImageResource(R.drawable.ic_baseline_broken_image_24)
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                    return false
                }
            })
            .into(holder.imageView)
    }

    inner class Holder : EpoxyHolderUtil() {
        val tvTitle by bind<TextView>(R.id.tvName)
        val tvDate by bind<TextView>(R.id.tvDate)
        val imageView by bind<ImageView>(R.id.imageView)
        val containerLayout by bind<View>(R.id.containerLayout)
    }
}
