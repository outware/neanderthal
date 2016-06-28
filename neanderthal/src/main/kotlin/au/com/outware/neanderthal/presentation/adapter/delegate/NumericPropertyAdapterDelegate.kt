package au.com.outware.neanderthal.presentation.adapter.delegate

import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.view.ViewGroup
import au.com.outware.neanderthal.R
import au.com.outware.neanderthal.data.model.Variant
import au.com.outware.neanderthal.presentation.adapter.SimpleViewHolder
import au.com.outware.neanderthal.presentation.adapter.listener.ConfigurationDataListener
import au.com.outware.neanderthal.util.SimpleTextWatcher
import au.com.outware.neanderthal.util.extensions.inflateLayout
import kotlinx.android.synthetic.main.neanderthal_item_detail_text.view.*
import java.text.DecimalFormat

/**
 * @author timmutton
 */
class NumericPropertyAdapterDelegate(val variant: Variant,
                                     override val viewType: Int): AdapterDelegate {
    companion object {
        private val DECIMAL_FORMAT = DecimalFormat("##.###")
    }

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            SimpleViewHolder(parent.inflateLayout(R.layout.neanderthal_item_detail_text))

    override fun bindViewHolder(name: String, value: Any?, type: Class<Any>, holder: RecyclerView.ViewHolder, configurationDataListener: ConfigurationDataListener) = with(holder.itemView) {

        textKey.text = name;
        editValue.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
        editValue.setText(if(value == null) "" else DECIMAL_FORMAT.format(value))
        editValue.addTextChangedListener(object : SimpleTextWatcher() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                when(type) {
                    Int::class.java -> configurationDataListener.onDataChanged(variant.configuration, name, Integer.valueOf(s.toString()))
                    Float::class.java -> configurationDataListener.onDataChanged(variant.configuration, name, java.lang.Float.valueOf(s.toString()))
                    Double::class.java -> configurationDataListener.onDataChanged(variant.configuration, name, java.lang.Double.valueOf(s.toString()))
                    Short::class.java -> configurationDataListener.onDataChanged(variant.configuration, name, java.lang.Short.valueOf(s.toString()))
                    Long::class.java -> configurationDataListener.onDataChanged(variant.configuration, name, java.lang.Long.valueOf(s.toString()))
                }
            }
        })
    }
}