package au.com.outware.neanderthal.presentation.adapter.delegate

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import au.com.outware.neanderthal.R
import au.com.outware.neanderthal.data.model.Variant
import au.com.outware.neanderthal.presentation.adapter.PropertyAdapter
import au.com.outware.neanderthal.presentation.adapter.SimpleViewHolder
import au.com.outware.neanderthal.util.extensions.inflateLayout
import kotlinx.android.synthetic.main.neanderthal_item_detail_text.view.*
import java.lang.reflect.Field

/**
 * @author timmutton
 */
class CharacterSequencePropertyAdapterDelegate(val variant: Variant,
                                               val setVariantName: Boolean,
                                               override val viewType: Int): AdapterDelegate<Field> {
    override fun isForViewType(items: List<Field>, position: Int): Boolean {
        if(setVariantName && position == 0) {
            return false;
        }

        val propertyType = items[position - if (setVariantName) 1 else 0].type
        return CharSequence::class.java.isAssignableFrom(propertyType)
    }

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            SimpleViewHolder(parent.inflateLayout(R.layout.neanderthal_item_detail_text))

    override fun bindViewHolder(items: List<Field>, position: Int, holder: RecyclerView.ViewHolder) = with(holder.itemView) {
        val configurationProperty = items[position - if (setVariantName) 1 else 0]

        textKey.text = PropertyAdapter.getPropertyName(configurationProperty)
        val propertyValue: CharSequence? = configurationProperty.get(variant.configuration) as String? ?: ""
        editValue.setText(propertyValue)
        editValue.setOnTextChangedListener { text ->
            val newValue = text.toString()
            if(!newValue.equals(propertyValue)) {
                Log.d(this.javaClass.simpleName, newValue)
                configurationProperty.set(variant.configuration, newValue)
            }
        }
    }
}