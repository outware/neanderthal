package au.com.outware.neanderthal.presentation.adapter.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import au.com.outware.neanderthal.R
import au.com.outware.neanderthal.data.model.Variant
import au.com.outware.neanderthal.presentation.adapter.SimpleViewHolder
import au.com.outware.neanderthal.presentation.adapter.listener.ConfigurationDataListener
import au.com.outware.neanderthal.util.extensions.inflateLayout
import kotlinx.android.synthetic.main.neanderthal_item_detail_checkbox.view.*

/**
 * @author timmutton
 */
class BooleanSequencePropertyAdapterDelegate(val variant: Variant,
                                             override val viewType: Int): AdapterDelegate {
    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            SimpleViewHolder(parent.inflateLayout(R.layout.neanderthal_item_detail_checkbox))

    override fun bindViewHolder(name: String, value: Any, type: Class<Any>, holder: RecyclerView.ViewHolder, configurationDataListener: ConfigurationDataListener) = with(holder.itemView) {
        checkboxKey.text = name
        checkboxValue.isChecked = value.equals(true)
        checkboxValue.setOnCheckedChangeListener {
            buttonView, isChecked ->
            configurationDataListener.onDataChanged(variant.configuration, name, checkboxValue.isChecked);
        }
    }
}