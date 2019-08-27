package au.com.outware.neanderthal.presentation.adapter.delegate

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import au.com.outware.neanderthal.R
import au.com.outware.neanderthal.data.model.Variant
import au.com.outware.neanderthal.presentation.adapter.SimpleViewHolder
import au.com.outware.neanderthal.util.extensions.inflateLayout
import kotlinx.android.synthetic.main.neanderthal_item_variant_name.view.*
import java.lang.reflect.Field

/**
 * @author timmutton
 */
class NamePropertyAdapterDelegate(val variant: Variant,
                                  val setVariantName: Boolean,
                                  override val viewType: Int): AdapterDelegate<Field> {
    override fun isForViewType(items: List<Field>, position: Int): Boolean {
        if(!setVariantName) {
            return false
        } else {
            return position == 0
        }
    }

    override fun createViewHolder(parent: ViewGroup): androidx.recyclerview.widget.RecyclerView.ViewHolder =
            SimpleViewHolder(parent.inflateLayout(R.layout.neanderthal_item_variant_name))

    override fun bindViewHolder(items: List<Field>, position: Int, holder: androidx.recyclerview.widget.RecyclerView.ViewHolder) {
        holder.itemView.nameValue.setOnTextChangedListener { text ->
            variant.name = text.toString()
        }
    }
}