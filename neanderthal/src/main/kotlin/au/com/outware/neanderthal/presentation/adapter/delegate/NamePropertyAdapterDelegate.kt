package au.com.outware.neanderthal.presentation.adapter.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import au.com.outware.neanderthal.R
import au.com.outware.neanderthal.data.model.Variant
import au.com.outware.neanderthal.presentation.adapter.PropertyAdapter
import au.com.outware.neanderthal.presentation.adapter.SimpleViewHolder
import au.com.outware.neanderthal.presentation.adapter.listener.ConfigurationDataListener
import au.com.outware.neanderthal.util.SimpleTextWatcher
import au.com.outware.neanderthal.util.extensions.inflateLayout
import kotlinx.android.synthetic.main.neanderthal_item_variant_name.view.*

/**
 * @author timmutton
 */
class NamePropertyAdapterDelegate(val variant: Variant,
                                  override val viewType: Int): AdapterDelegate {
    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            SimpleViewHolder(parent.inflateLayout(R.layout.neanderthal_item_variant_name))

    override fun bindViewHolder(name: String, value: Any, type: Class<Any>, holder: RecyclerView.ViewHolder, configurationDataListener: ConfigurationDataListener) {
        holder.itemView.nameValue.addTextChangedListener(object : SimpleTextWatcher() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                variant.name = s.toString()

                if(s.length > 0) {
                    holder.itemView.nameValueLayout.isErrorEnabled = false
                }
            }
        })
    }
}