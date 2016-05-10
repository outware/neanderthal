package au.com.outware.neanderthal.presentation.adapter.delegate

import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.view.ViewGroup
import au.com.outware.neanderthal.R
import au.com.outware.neanderthal.data.model.Variant
import au.com.outware.neanderthal.presentation.adapter.PropertyAdapter
import au.com.outware.neanderthal.presentation.adapter.SimpleViewHolder
import au.com.outware.neanderthal.util.SimpleTextWatcher
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

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            SimpleViewHolder(parent.inflateLayout(R.layout.neanderthal_item_detail_text))

    override fun bindViewHolder(items: List<Field>, position: Int, holder: RecyclerView.ViewHolder) = with(holder.itemView) {
        val configurationProperty = items[position - if (setVariantName) 1 else 0]
        val valueNumber = configurationProperty.get(variant.configuration) ?: 0

        textKey.text = PropertyAdapter.getPropertyName(configurationProperty)

        editValue.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
        editValue.setText(DECIMAL_FORMAT.format(valueNumber))
        editValue.addTextChangedListener(object : SimpleTextWatcher() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                when(configurationProperty.type) {
                    Int::class -> configurationProperty.setInt(variant.configuration, Integer.valueOf(s.toString()))
                    Float::class -> configurationProperty.setFloat(variant.configuration, java.lang.Float.valueOf(s.toString()))
                    Double::class -> configurationProperty.setDouble(variant.configuration, java.lang.Double.valueOf(s.toString()))
                    Short::class -> configurationProperty.setShort(variant.configuration, java.lang.Short.valueOf(s.toString()))
                    Long::class -> configurationProperty.setLong(variant.configuration, java.lang.Long.valueOf(s.toString()))
                }
            }
        })
    }
}