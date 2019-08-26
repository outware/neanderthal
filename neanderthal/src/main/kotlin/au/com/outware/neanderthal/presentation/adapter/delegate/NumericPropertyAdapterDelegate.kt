package au.com.outware.neanderthal.presentation.adapter.delegate

import androidx.recyclerview.widget.RecyclerView
import android.text.InputType
import android.text.TextUtils
import android.view.ViewGroup
import au.com.outware.neanderthal.R
import au.com.outware.neanderthal.data.model.Variant
import au.com.outware.neanderthal.presentation.adapter.PropertyAdapter
import au.com.outware.neanderthal.presentation.adapter.SimpleViewHolder
import au.com.outware.neanderthal.util.extensions.inflateLayout
import kotlinx.android.synthetic.main.neanderthal_item_detail_text.view.*
import java.lang.reflect.Field
import java.text.DecimalFormat

/**
 * @author timmutton
 */
class NumericPropertyAdapterDelegate(val variant: Variant,
                                     val setVariantName: Boolean,
                                     override val viewType: Int): AdapterDelegate<Field> {
    companion object {
        private val DECIMAL_FORMAT = DecimalFormat("##.###")
    }

    override fun isForViewType(items: List<Field>, position: Int): Boolean {
        if(setVariantName && position == 0) {
            return false;
        }

        val propertyType = items[position - if (setVariantName) 1 else 0].type
        return (Number::class.java.isAssignableFrom(propertyType) ||
                propertyType == Float::class.java  ||
                propertyType == Int::class.java    ||
                propertyType == Double::class.java ||
                propertyType == Short::class.java  ||
                propertyType == Long::class.java)
    }

    override fun createViewHolder(parent: ViewGroup): androidx.recyclerview.widget.RecyclerView.ViewHolder =
            SimpleViewHolder(parent.inflateLayout(R.layout.neanderthal_item_detail_text))

    override fun bindViewHolder(items: List<Field>, position: Int, holder: androidx.recyclerview.widget.RecyclerView.ViewHolder) = with(holder.itemView) {
        val configurationProperty = items[position - if (setVariantName) 1 else 0]
        val valueNumber = configurationProperty.get(variant.configuration) ?: 0

        textKey.text = PropertyAdapter.getPropertyName(configurationProperty)

        editValue.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
        editValue.setOnTextChangedListener { text ->
            if(!TextUtils.isEmpty(text) && TextUtils.isDigitsOnly(text)){
                when(configurationProperty.get(variant.configuration)) {
                    is Int -> configurationProperty.setInt(variant.configuration, Integer.valueOf(text.toString()))
                    is Float -> configurationProperty.setFloat(variant.configuration, java.lang.Float.valueOf(text.toString()))
                    is Double -> configurationProperty.setDouble(variant.configuration, java.lang.Double.valueOf(text.toString()))
                    is Short -> configurationProperty.setShort(variant.configuration, java.lang.Short.valueOf(text.toString()))
                    is Long -> configurationProperty.setLong(variant.configuration, java.lang.Long.valueOf(text.toString()))
                }
            }
        }
        editValue.setText(DECIMAL_FORMAT.format(valueNumber))
    }
}