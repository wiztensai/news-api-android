package com.airbnb.epoxy.kotlinsample.helpers

import android.view.View
import com.airbnb.epoxy.EpoxyHolder
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * A pattern for easier view binding with an [EpoxyHolder]
 *
 * See [com.airbnb.epoxy.kotlinsample.models.ItemEpoxyHolder] for a usage example.
 */
abstract class EpoxyHolderUtil : EpoxyHolder() {
    private lateinit var view: View

    override fun bindView(itemView: View) {
        view = itemView
    }

    protected fun <V : View> bind(id: Int): ReadOnlyProperty<EpoxyHolderUtil, V> =
        Lazy { holder: EpoxyHolderUtil, prop ->
            holder.view.findViewById(id) as V?
                ?: throw IllegalStateException("View ID $id for '${prop.name}' not found.")
        }

    /**
     * Taken from Kotterknife.
     * https://github.com/JakeWharton/kotterknife
     */
    private class Lazy<V>(
        private val initializer: (EpoxyHolderUtil, KProperty<*>) -> V
    ) : ReadOnlyProperty<EpoxyHolderUtil, V> {
        private object EMPTY

        private var value: Any? = EMPTY

        override fun getValue(thisRef: EpoxyHolderUtil, property: KProperty<*>): V {
            if (value == EMPTY) {
                value = initializer(thisRef, property)
            }
            @Suppress("UNCHECKED_CAST")
            return value as V
        }
    }
}
